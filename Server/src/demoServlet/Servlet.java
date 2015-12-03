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

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

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
		
		switch(request.getRequestURI()){
		case "/world":
			//get world
		}
	    for (Entry<String, String[]> entry : parameterNames.entrySet()) {
	    	int session_id;
	    	String level;
	            switch (entry.getKey()) {
	            case "steps":
	                steps = Integer.parseInt(entry.getValue()[0]);
	                break;
	            case "session_id":
	            	session_id = Integer.parseInt(entry.getValue()[0]);
	            	if (this.keys.containsKey(session_id)){
	            		level = keys.get(session_id);
	            	}
	            }
	    }
  
		for (int i = 0; i < steps; i++){
			sim.step();
			step++;
		}
		//delta compression
		HexBundle[] diffs = sim.getDiffs(step, 0);
		
		StateBundle state = new StateBundle(this.sim, step, 0);

		PrintWriter w = response.getWriter();
		
		String json = gson.toJson(state);
		//write the JSON
		w.println(json);
		//flush the stream to make sure it actually gets written
		w.flush();
		//close the output stream
		w.close();
		//send a 200 status indicating success
		response.setStatus(200);
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
		switch(request.getRequestURI()){
		case "/login":
			//login
		case "/critters":
			//post critters
		case "/step":
			//step once if run is 0
		case "/run":
			//set the rate the server is running at
		default:
			if (request.getPathInfo().matches("/login/\\d+")){
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
	
	synchronized void stepOnce(){
		sim.step();
		step++;
	}
}
