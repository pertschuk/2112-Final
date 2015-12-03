package controller;

import view.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.Gson;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import json.Bundles;
import json.LoginBundle;
import json.LoginResponseBundle;
import simulator.*;

import client.*;

public class ApplicationController implements Observable<Event> {
	
	private Stage primaryStage;
	private Simulator s;
	private LinkedList<Observer<Event>> observers = new LinkedList<Observer<Event>>();
	private int step;
	private int speed; //in steps/second
	@FXML private Pane hexmap;
	
	@FXML private Label species; 
	@FXML private TextArea program;
	@FXML private ListView<String> mem;
	@FXML private Slider slider;
	@FXML private Label speedlabel;
	@FXML private Button runbutton;
	@FXML private Button steponce;
	@FXML private Label currentstep;
	@FXML private Label numcritters;
	@FXML private Label errors;
	@FXML private Label lastrule;
	@FXML private Button login;
	//components of login stage
	@FXML private Button submit;
	@FXML private TextField url;
	@FXML private PasswordField password;
	@FXML private TextField level;
	
	private boolean running;
	
	private int auth = 0; // default no auth credentials
	private String remote;
	private CloseableHttpClient client = HttpClients.createDefault();
	private int lastupdated;
	private Gson gson = new Gson();

	
	private int zoom;
	
	private Random random = new Random();
	
	@FXML public void initialize(){
		Stage newStage = new Stage();
		VBox comp = new VBox(8);
		
		url = new TextField();
		password = new PasswordField();
		level = new TextField();
		
		url.setPromptText("Enter URL");
		level.setPromptText("Level");
		password.setPromptText("Password");

		Button submit = new Button();
		submit.setPadding(new Insets(10,10,10,10));
		submit.setText("Login");
		submit.setAlignment(Pos.CENTER);
		comp.getChildren().add(url);
		comp.getChildren().add(level);
		comp.getChildren().add(password);
		comp.getChildren().add(submit);
		comp.setPadding(new Insets(10,10,10,10));
		submit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					login();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		Scene stageScene = new Scene(comp, 300, 160);
		newStage.setScene(stageScene);
		newStage.show();
	}
	
	public Pane hexmap (){
		return hexmap;
	}

	public ApplicationController(){
		s = new Simulator(); // generates world with random rocks
		step = 0;
		speed = 0;
		zoom = 300;
	}
	
	public boolean isValidCoord(int c, int r){
		return s.isValidCoord(c, r);
	}
	
	/**
	 * Steps through n times
	 * @param n
	 */
	public void step(int n){
		for (int i = 0; i < n; i ++){
			s.step();
			step++;
		}
		Event e = new UpdateMapEvent(s.getMap());
		numcritters.setText("Critters: " +Integer.toString(s.getNumCritters()));
		currentstep.setText("Step: " +Integer.toString(step));
		this.notifyAllObservers(e);
	}

	@Override
	public void registerObserver(Observer<Event> observer) {
		observers.add(observer);
		
	}

	@Override
	public void notifyAllObservers(Event e) {
		for (Observer<Event> o: observers){
			o.notify(e);
		}
	}
	/**
	 * the map ivar of the simulator object in this
	 * @return multidimensional rep of map
	 */
	public int[][] getMap() {
		return s.getMap();
	}
	
	/**
	 * Returns the mem array of the critter at hex (c,r)
	 * @param c  column
	 * @param r  row
	 * @return  the mem array of the critter at (c,r), or null if no critter is there
	 */
	public int[] getCritterMem(int c, int r){
		Critter crit = s.getCritter(c, r);
		if (crit != null)
			return crit.getMemArray();
		return null;
	}
	
	/**
	 * Gets the species name and pretty-printed program of critter at c,r
	 * @param c  col
	 * @param r  row
	 * @return  String array of length two of format {species, program}
	 * 			null if no critter at c,r
	 */
	@FXML public void getCritterSpeciesProgram(int c, int r) {
		Critter crit = s.getCritter(c, r);
		if (crit != null){
			String[] info = crit.getProperties();
			species.setText(info[0] + " at hex " + crit.getCol() + ", " + crit.getRow());
			lastrule.setText("Last Rule " + info[2]);
			program.setText(info[1]);
			int[] m = crit.getMemArray();
			ObservableList<String> contents = FXCollections.observableArrayList("Mem Length: " + m[0], 
					"Defense: " + m[1],
					"Offense: " + m[2],
					"Size: " + m [3],
					"Energy: " + m[4],
					"Pass: " + m[5],
					"Tag: " + m[6],
					"Posture: " + m[7]);
			mem.setItems(contents);
		}
	}
	
	/**
	 * Start world running continuously
	 * @throws InterruptedException 
	 */
	@FXML public synchronized void startWorld() throws InterruptedException{
		if (running) { running = false; }
		else { running = true; }
		runContinuously();
	}
	
	public void runContinuously(){
		ApplicationController controller = this;
		new Thread() { // Create a new background process
		    public void run() {
		        	Long time = System.nanoTime();
		        	for (int i= 0; i < speed; i++){
		        		controller.s.step();
		        		step++;
		        	}
		        	Long after = System.nanoTime();
		        	try {
		        		if (after-time < 1000){
		        			sleep(1000 - (after-time));
		        		}
		        		else {
		        			sleep(10);
		        		}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	
		        Platform.runLater(new Runnable() { // Go back to UI/application thread
		            public synchronized void run() {
		            	notifyAllObservers(new UpdateMapEvent(getMap()));
		    	        currentstep.setText(Integer.toString(step));
		    	        numcritters.setText(Integer.toString(s.getNumCritters()));
		    	        if (running){
		    	        	runContinuously();
		    	        }
		            }
		        } );
		    }
		}.start(); // Starts the background thread!
	}
	
	/**
	 * Returns the current timestep of the world
	 */
	public int getStep(){
		return this.step;
	}

	
	/**
	 * Loads n critters specified by the filename to the world
	 * @param filename  the file to be read
	 * @param n  the number of this type of critter to be added to the simulation
	 * Effect:  adds n new critters to the simulation
	 * @throws IOException if filename cannot be read
	 */
	public void loadCritter(String filename, int n) throws IOException{
		for (int i = 0; i < n; i++) {
			s.addCritter(Critter.loadCritter(filename, 0, s, 0, 0));
		}
	}
	
	/**
	 * Set speed of simulation to v steps/second as long as this doesn't violate
	 * the speed invariant (0<=speed<=30 steps/second)
	 * @param v  new speed of simulation in steps/second
	 */
	@FXML public void setSpeed() {
		double value = slider.getValue()/slider.getMax();
		speed = (int) (value * 60);
		speedlabel.setText(Integer.toString(speed));
	}
	public int getSpeed() { return speed; }
	
	/**
	 * Steps n iterations (according to slider)
	 */
	@FXML public void runOnce(){
		//step((int)(slider.getValue()/slider.getMax()) * 60);
		step(1);
		errors.setText("");
		errors.toFront();
	}
	
	/**
	 * 
	 */
	@FXML public void critterDialogue(){
		Stage newStage = new Stage();
		File file = fileDialogue(newStage); // load file
		VBox comp = new VBox(8);
		TextField col = new TextField();
		TextField row = new TextField();
		row.setPromptText("Row");
		col.setPromptText("Column");
		Button submit = new Button();
		submit.setPadding(new Insets(10,10,10,10));
		submit.setText("Add Critter");
		submit.setAlignment(Pos.CENTER);
		comp.getChildren().add(col);
		comp.getChildren().add(row);
		comp.getChildren().add(submit);
		comp.setPadding(new Insets(10,10,10,10));
		submit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					Critter c = Critter.loadCritter(file.getPath(), random.nextInt(6), s, Integer.parseInt(col.getText()), Integer.parseInt(row.getText()));
					ArrayList<Critter> critters = new ArrayList<Critter>();
					critters.add(c);
					Bundles.CritterBundle bundle = new Bundles.CritterBundle(critters, auth); // add auth
					Post.CritterPost(bundle); // sends the critter bundle to the server
					//step(0);
					newStage.hide();
				} catch (NumberFormatException e) {
					errors.setText("Invalid Number");
					newStage.hide();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					errors.setText("Invalid File");
					newStage.hide();
				}
				newStage.hide();
			}
			
		});
		Scene stageScene = new Scene(comp, 300, 160);
		newStage.setScene(stageScene);
		newStage.show();
	}
	/**
	 * Loads world
	 */
	@FXML public void worldDialogue() {
		Stage newStage = new Stage();
		newStage.show();
		File file = fileDialogue(newStage); // load file
		try {
			String line;
			BufferedReader br = new BufferedReader(new FileReader(file));
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine())!= null){
				sb.append(line);
			}
			Bundles.WorldBundle world = new Bundles.WorldBundle(sb.toString(), auth);
			Post.WorldPost(world);
			newStage.hide();
		}
		catch (IOException e) {
			errors.setText("Invalid File");
			newStage.hide();
		}
	}
	public void loadMap(ApplicationView view){
		hexmap.getChildren().clear();
		view.drawMap(getMap(), this.hexmap);
		view.UpdateMap(getMap(), this.hexmap);
	}
	/**
	 * Dialogue for placing a random critter in the world
	 */
	@FXML public void randomCritterDialogue(){
		Stage newStage = new Stage();
		File file = fileDialogue(newStage);
		VBox comp = new VBox(8);
		TextField num = new TextField();
		num.setPromptText("Number of Critters");
		Button submit = new Button();
		submit.setPadding(new Insets(10,10,10,10));
		submit.setText("Add Critters");
		submit.setAlignment(Pos.CENTER);
		comp.getChildren().add(num);
		comp.getChildren().add(submit);
		comp.setPadding(new Insets(10,10,10,10));
		submit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					ArrayList<Critter> critters = new ArrayList<Critter>();
					for (int i = 0; i < Integer.parseInt(num.getText()); i ++){
						critters.add(Critter.loadCritter(file.getPath(), random.nextInt(6), s, 1, 2));
					}
					Bundles.CritterBundle bundle = new Bundles.CritterBundle(critters, auth); // add auth
					Post.CritterPost(bundle);
				} catch (NumberFormatException e) {
					errors.setText("Invalid Number");
					newStage.hide();
				} catch (IOException e) {
					errors.setText("Invalid File");
					newStage.hide();
				}
				newStage.hide();
			}
			
		});
		Scene stageScene = new Scene(comp, 300, 160);
		newStage.setScene(stageScene);
		newStage.show();
	}
	@FXML public void zoomIn(){
		zoom += 100;
		this.notifyAllObservers(new LoadMapEvent(this.getMap()));
		this.hexmap.setPrefHeight(zoom);
	}
	@FXML public void zoomOut(){
		zoom -= 100;
		this.notifyAllObservers(new LoadMapEvent(this.getMap()));
		this.hexmap.autosize();
		this.hexmap.setPrefHeight(zoom);
	}
	public int getZoom(){
		return zoom;
	}
	private File fileDialogue(Stage stage){
		FileChooser critterfile = new FileChooser();
		FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Text files only (*.txt)", "*.txt");
		critterfile.getExtensionFilters().add(filter);
		File file = critterfile.showOpenDialog(stage);
		return file;
	}
	
	@FXML public void login() throws UnsupportedEncodingException{
		String url = this.url.getText() + "/login";
		String password = this.password.getText();
		HttpPost post = new HttpPost(url);
		LoginBundle login = new LoginBundle(password, level.getText());
		post.setEntity(new StringEntity(gson.toJson(login)));
		post.addHeader("Content-Type", "application/json");
		try {
			CloseableHttpResponse response = client.execute(post);
			String body = IOUtils.toString(response.getEntity().getContent());
			LoginResponseBundle res = gson.fromJson(body, LoginResponseBundle.class);
			auth = res.session_id;
			this.remote = this.url.getText();
			this.primaryStage.show();
			UpdateState();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void UpdateState() {
		HttpGet get = new HttpGet(this.remote + "/world?session_id=" + this.auth + "?update_since" + this.lastupdated);
		try {
			HttpResponse res = this.client.execute(get);
			String body = IOUtils.toString(res.getEntity().getContent());
			Bundles.WorldBundle world = gson.fromJson(body, Bundles.WorldBundle.class);
			//this.lastupdated = 
			//update current simulator
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setStage(Stage stage){
		this.primaryStage = stage;
	}
}
