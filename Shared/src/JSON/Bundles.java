package JSON;

import java.util.ArrayList;
import java.util.Collection;

import simulator.Critter;
import simulator.Hex;

public class Bundles {
	/**
	 * JSON bundle that represents the state of the world.
	 * Serialized and sent to the client and then deserialized
	 * @author jack
	 *
	 */
	public static class StateBundle {
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
	public static class CritterBundle {
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
	public static class WorldBundle {
		private String world;
		private int auth;
		
		public WorldBundle(String world, int auth){
			this.world = world;
		}
		public String getWorld(){
			return world;
		}
	}
}
