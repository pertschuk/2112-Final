package json;

import java.util.ArrayList;
import java.util.Collection;

import simulator.Critter;
import simulator.Hex;

public class Bundles {
	
	/**
	 * JSON bundle that represent an update of the server
	 * by the client
	 * @author jack
	 */
	public static class CritterBundle {
		private ArrayList<Critter> critters = new ArrayList<Critter>();
		
		public CritterBundle(ArrayList<Critter> newcritters){
			this.critters = newcritters;
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
	public static class WorldBundle {
		private String world;
		
		public WorldBundle(String world){
			this.world = world;
		}
		public String getWorld(){
			return world;
		}
	}
	
	public static class StepBundle {
		public int count;
		public StepBundle (int steps){
			count = steps;
		}
	}
	
	public static class RateBundle {
		public int rate;
		public RateBundle(int rate) {
			this.rate = rate;
		}
	}
}
