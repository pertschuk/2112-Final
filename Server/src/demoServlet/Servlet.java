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

import simulator.*;

/**
 * Servlet implementation class
 */
@WebServlet("/") /* relative URL path to servlet (under package name 'demoServlet'). */
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Simulator sim = new Simulator();
	
	private int step;
	
	private ArrayList<String> adminkeys = new ArrayList<String>();

	/**
	 * JSON bundle that represents the state of the world.
	 * Serialized and sent to the client and then deserialized
	 * @author jack
	 *
	 */
	class StateBundle {
		 private ArrayList<Hex> updates = new ArrayList<Hex>();
		 private int step;
		 
		 public StateBundle(int step, Collection<Hex> updates){
			 this.step = step;
			 updates.addAll(updates);
		 }
	}
	
	/**
	 * JSON bundle that represent an update of the server
	 * by the client
	 * @author jack
	 */
	private class CritterBundle {
		private ArrayList<Critter> critters = new ArrayList<Critter>();
		private int auth;
		
		public CritterBundle(ArrayList<Critter> newcritters, int auth){
			this.critters = newcritters;
			this.auth = auth;
		}
		public ArrayList<Critter> getCritters(){
			return critters;
		}
	}
	
	/**
	 * Represents an entire new world state.
	 * Can be sent to server.
	 * @author jack
	 */
	private class WorldBundle {
		private String world;
		private int auth;
		
		public WorldBundle(String world){
			this.world = world;
		}
		public String getWorld(){
			return world;
		}
	}
	
	/**
	 * Handle GET request
	 * 
	 * Request from the USER to load the model in
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//TODO: IMPLEMENT
		int curstep = 0;
		
		Gson gson = new Gson();
		response.addHeader("Content-Type", "application/json");
		PrintWriter w = response.getWriter();
		
		int steps = 0;
		//retreives the GET parameters from the URL, looks for step
		Map<String, String[]> parameterNames = request.getParameterMap();
	        for (Entry<String, String[]> entry : parameterNames.entrySet()) {
	            switch (entry.getKey()) {
	            case "steps":
	                steps = Integer.parseInt(entry.getValue()[0]);
	                break;
	         }
		}
		for (int i = 0; i < steps; i++){
			sim.step();
			step++;
		}
		//delta compression
		Collection<Hex> diffs = sim.getDiffs(step, curstep);
		
		StateBundle state = new StateBundle(getStep(), diffs );
		
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
		response.addHeader("Content-Type", "text/plain");
		Gson gson = new Gson();
		BufferedReader r = request.getReader();
		Map<String, String[]> parameterNames = request.getParameterMap();
        for (Entry<String, String[]> entry : parameterNames.entrySet()) {
            switch (entry.getKey()) {
            case "world":
            	WorldBundle world = gson.fromJson(r, WorldBundle.class);
            	UpdateWorld(world);
                break;
            case "critters":
            	CritterBundle critters = gson.fromJson(r, CritterBundle.class);
            	AddCritters(critters);
            	break;
            }
		}
	}
	
	/**
	 * Adds critters to the world
	 * @param critters CritterBundle of critters
	 */
	private void AddCritters(CritterBundle critters) {
		for (Critter c : critters.getCritters()){
			this.sim.placeCritter(c);
		}
	}
	
	/**
	 * Update World
	 * @param world
	 * @throws IOException 
	 */
	private void UpdateWorld(WorldBundle world) throws IOException {
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
