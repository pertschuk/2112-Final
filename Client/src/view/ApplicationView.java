package view;

import javafx.application.*;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.util.Collection;

import controller.*;

public class ApplicationView extends Application implements Observer<Event>{
	private ApplicationController controller;
	
	private Pane[][] mapview;
	private Polygon[][] hexes;
	private int zoom;
	private double size; // the size of one size of hexagons
	
	public static void main(String[] args){
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("a6_layout.fxml"));
		
		primaryStage.setTitle("Critter Simulator");
		Parent root = loader.load(); // creates instance of application controller
		controller = loader.getController();
		zoom = controller.getZoom();
		Scene scene = new Scene(root, 1000, 500);
		System.out.println(controller);
		
		controller.registerObserver(this);
		
		int[][] map = controller.getMap();
		mapview = new Pane[map.length][map[0].length];
		hexes = new Polygon[map.length][map[0].length];
		drawMap(map, controller.hexmap());
		primaryStage.setScene(scene);
		//loader = new FXMLLoader(getClass().getResource("login_layout.fxml"));
		controller.setStage(primaryStage);
	}

	@Override
	public void notify(Event event) {
		if (event instanceof LoadMapEvent){
			controller.loadMap(this); // loads the map
		}
		else{
			UpdateMap(((UpdateMapEvent) event).getMap(), controller.hexmap());
		}
	}
	
	/**
	 * Draw map
	 */
	public void drawMap(int[][] map, Pane g){
		zoom = controller.getZoom();
		this.hexes = new Polygon[map.length][map[0].length];
		this.mapview = new Pane[map.length][map[0].length];
		int columns = map.length;
		int rows = map[0].length;
		
		int numrows = (2* rows - columns)/ 2 -1; //height of print out
		
		double hexheight = zoom / numrows;
		double size = hexheight / Math.sqrt(3); // the side length
		this.size = size;
		int row, col;
		double x, y = 0;
		x = size;
		for (col = 0; col < columns; col++){
			if (!(col %2 == 0)){
				y = hexheight/2.0;
			} else { y = hexheight; }
			x += size * (1.5);
			Pane p;
			for (row = rows; row >=0; row --){
				if (!controller.isValidCoord(col, row)) {
					continue;
				}
				y += hexheight;
				//if its a rock
				if (map[col][row]== -1) {
					p = EmptyHex(x,y,size, Color.GRAY, col, row);
					this.mapview[col][row] = p;
					g.getChildren().add(p);
				}
				//if its empty
				else{
					p = EmptyHex(x,y,size, Color.WHITE, col, row);
					this.mapview[col][row] = p;
					g.getChildren().add(p);
				}
			}
		}
	}
	/**
	 * 
	 */
	public synchronized void UpdateMap(int[][] map, Pane g){
		int cols = map.length; int rows = map[0].length;
		for ( int i = 0; i <cols; i ++ ){
			for ( int j = 0; j < rows; j ++){
				if (!controller.isValidCoord(i, j)) {
					continue;
				}
				if (mapview[i][j] == null) { continue; }
				//removes children if there are children in the hex
				if (map[i][j] == 0){
					mapview[i][j].getChildren().clear();
					hexes[i][j].setFill(Color.WHITE);
					//hexes[i][j].toFront();
				}
				if (map[i][j] > 0){
					Image image = new Image("critter.png");
					ImageView view = new ImageView(image);
					view.setFitWidth(this.size);
					view.setPreserveRatio(true);
					view.setCache(true);
					view.setSmooth(true);
					view.setRotate((map[i][j]%6) * 60);
					mapview[i][j].getChildren().clear();
					mapview[i][j].getChildren().add(view);
					hexes[i][j].setFill(Color.WHITE);
				}
				if (map[i][j] < -1){
					hexes[i][j].setFill(Color.rgb(150, 0, 0));
					Label l = new Label(Integer.toString(((Math.abs(map[i][j])-1) + 50)%255));
					l.setTextFill(Color.WHITE);
					mapview[i][j].getChildren().clear();
					mapview[i][j].getChildren().add(l);
				}
				//mapview[i][j].getChildren().add(new Label(Integer.toString(map[i][j])));
				//mapview[i][j].getChildren().add(new Label(i + ", " + j));
			}
		}
	}
	/**
	 * 
	 * @param x
	 * @param y
	 * @param size
	 */
	public Pane EmptyHex(double x, double y, double size, Paint fill, int col, int row){
		Pane p = new Pane();
		p.setLayoutX(x- size/2);
		p.setLayoutY(y - size/2);
		Polygon polygon = new Polygon();
		polygon.setStroke(Color.BLACK);
		polygon.setFill(fill);
		polygon.getPoints().addAll(new Double[]{x ,y - size,
				x - Math.sqrt(3) * size /2, y - size /2,
				x - Math.sqrt(3) * size /2, y + size /2,
				x, y + size,
				x + size * Math.sqrt(3) / 2,  y + size/2,
				x + size * Math.sqrt(3) / 2, y - size/2
				});
		polygon.setRotate(30);
		controller.hexmap().getChildren().add(polygon);
		hexes[col][row] = polygon;
		//p.autosize();
		p.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				controller.getCritterSpeciesProgram(col, row);
			}
		});
		//p.setBackground(new Background(new BackgroundFill(Color.AQUA, null, null)));
		return p;
	}
}
