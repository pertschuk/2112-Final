package json;

import simulator.Hex;
import simulator.Simulator;

public class StateBundle {
	public int current_timestep;
	public int current_version_number;
	public int update_since;
	public int rate;
	public String name;
	public int population;
	public int rows, columns;
	public int[] dead_critters;
	public HexBundle[] state;
	public StateBundle(Simulator s, int since, int now){
		state = s.getDiffs(since, now);
	}
}
