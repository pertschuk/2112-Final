package demoServlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import javafx.application.Platform;
import javafx.fxml.FXML;
import json.*;
import simulator.*;

/**
 * Servlet implementation class
 */
@WebServlet("/") /* relative URL path to servlet. */
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Simulator sim = new Simulator();
	
	private int step;
	private int speed;
	private boolean running = false;
	
	private Random random = new Random();
	
	private String adminpass, writepass;
	
	private HashMap<Integer, String> keys = new HashMap<Integer, String>();
	
	/**
	 * Handle GET request
	 * 
	 * Request from the USER to load the model in
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//TODO: IMPLEMENT

		
		int steps = 0;
		//Retrieves the GET parameters from the URL, looks for step
		Map<String, String[]> parameterNames = request.getParameterMap();
		Gson gson = new Gson();
		response.addHeader("Content-Type", "application/json");
		String level = "read";
		int from_row = -1, to_row = -1, from_column = -1, to_column = -1;
		int update_since = 0;
	    for (Entry<String, String[]> entry : parameterNames.entrySet()) {
	    	int session_id;
	            switch (entry.getKey()) {
	            case "steps":
	                steps = Integer.parseInt(entry.getValue()[0]);
	                break;
	            case "session_id":
	            	session_id = Integer.parseInt(entry.getValue()[0]);
	            	if (this.keys.containsKey(session_id)){
	            		level = keys.get(session_id);
	            	}
	            	else {
	            		level = "read";
	            	}
	            	break;
	            case "update_since":
	            	update_since = Integer.parseInt(entry.getValue()[0]);
	            case "from_row":
	            	from_row = Integer.parseInt(entry.getValue()[0]);
	            case "to_row":
	            	to_row  = Integer.parseInt(entry.getValue()[0]);
	            case "from_column":
	            	from_column = Integer.parseInt(entry.getValue()[0]);
	            case "to_column":
	            	to_column = Integer.parseInt(entry.getValue()[0]);
	            }
	    }
		PrintWriter w = response.getWriter();

		switch(request.getRequestURI()){
		case "/world":
			StateBundle state;
			if (from_row != -1 && to_row != -1 && from_column != -1 && to_column != -1){
				//load a subsection of the world
				 state = this.sim.bundle(from_row, to_row, from_column, to_column, update_since);
			}
			else {
				state = this.sim.bundle(update_since);
			}
			String json = gson.toJson(state);
			w.println(json);
			w.flush();
			w.close();
			response.setStatus(200);
		default:
			// default stuff
		}
	}
	
	/**
	 * Handles http post requests to the server
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int session_id = 0;
		String level = "";
		response.addHeader("Content-Type", "text/plain");
		Gson gson = new Gson();
		PrintWriter writer = response.getWriter();
		BufferedReader r = request.getReader();
		Map<String, String[]> parameterNames = request.getParameterMap();
		for (Entry<String, String[]> entry : parameterNames.entrySet()) {
            switch (entry.getKey()) {
            case "session_id":
            	session_id = Integer.parseInt(entry.getValue()[0]);
            	if (this.keys.containsKey(session_id))
            		level = keys.get(session_id);
                break;
            }
		}
		InputStream i = request.getInputStream();
		String body = IOUtils.toString(i);
		switch(request.getRequestURI()){
		case "/login":
			LoginBundle login = gson.fromJson(body, LoginBundle.class);
			if ((login.password == this.adminpass && login.level == "admin")|| login.password == this.writepass && login.level == "write" ){
				session_id = random.nextInt(1000);
				while (keys.containsKey(session_id)){
					session_id = random.nextInt(1000);
				}
				this.keys.put(session_id, login.level);
				LoginResponseBundle res = new LoginResponseBundle(session_id);
				writer.println((gson.toJson(res)));
				writer.flush();
				writer.close();
				response.setStatus(200);
			}
		case "/critters":
			this.AddCritters(gson.fromJson(body, Bundles.CritterBundle.class));
		case "/step":
			if ((level == "admin" || level == "write") && running ==false){
				Bundles.StepBundle step = gson.fromJson(body, Bundles.StepBundle.class);
				this.step(step.count);
			}
		case "/run":
			if (running == false){
				try {
					Bundles.RateBundle rate = gson.fromJson(body, Bundles.RateBundle.class);
					this.speed = rate.rate;
					this.startWorld();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		case "/world":
			Bundles.WorldBundle world = gson.fromJson(body, Bundles.WorldBundle.class);
			String worldstring = world.getWorld();
			Simulator s = new Simulator(new BufferedReader( new InputStreamReader(new ByteArrayInputStream(worldstring.getBytes()))));
			this.sim = s;
			//add code to push to clients
		default:
			if (request.getPathInfo().matches("/critter/\\d+")){
				int id = Integer.parseInt(request.getPathInfo().split("/")[2]);
			}
			if (request.getPathInfo().matches("/world/create_entity\\d+")){
				
			}
		}
	}
	
	/**
	 * Delete critter
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		Map<String, String[]> parameterNames = request.getParameterMap();
		int session_id;
		String level;
		for (Entry<String, String[]> entry : parameterNames.entrySet()) {
            switch (entry.getKey()) {
            case "session_id":
            	session_id = Integer.parseInt(entry.getValue()[0]);
            	if (this.keys.containsKey(session_id))
            		level = keys.get(session_id);
                break;
            }
		}
		int id = Integer.parseInt(request.getPathInfo().split("/")[2]);
		sim.deleteCritter(id);
	}
	
	/**
	 * Adds critters to the world
	 * @param critters CritterBundle of critters
	 */
	private void AddCritters(Bundles.CritterBundle critters) {
		for (Critter c : critters.getCritters()){
			this.sim.placeCritter(c);
		}
	}
	
	/**
	 * Update World
	 * @param world
	 * @throws IOException 
	 */
	private void UpdateWorld(Bundles.WorldBundle world) throws IOException {
		InputStream input = new ByteArrayInputStream(world.getWorld().getBytes());
		BufferedReader br = new BufferedReader(new InputStreamReader(input));
		Simulator s = new Simulator(br);
	}
	
	synchronized int getStep(){
		return step;
	}
	
	synchronized void step(int n){
		for (int i = 0; i < n; i++){
			sim.step();
			step++;
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
		Simulator s = this.sim;
		new Thread() { // Create a new background process
		    public void run() {
		        	Long time = System.nanoTime();
		        	for (int i= 0; i < speed; i++){
		        		s.step();
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
		    	        if (running){
		    	        	runContinuously();
		    	        }
		            }
		        } );
		    }
		}.start(); // Starts the background thread!
	}
}
