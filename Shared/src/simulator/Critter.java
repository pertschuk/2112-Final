package simulator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Random;

import ast.*;
import interpret.Interpreter;
import interpret.Outcome;
import parse.Parser;
import parse.ParserImpl;

/**
 * Instances of this class represent critters in the world
 * @author jack pertschuk
 *
 */
public class Critter extends Hex{
	private Program p;
	private int[] mem;
	private String species;
	private int direction;
	private static Random random = new Random();
	public String lastrule;
	public int id;
	public int uid;
	
	private static int nextid = 0;
	
	/**
	 * Default critter 
	 * @param c
	 * @param r
	 * @param p
	 * @param mem
	 * @param species
	 * @param d
	 */
	public Critter(int c, int r, Program p, int[] mem, String species, int d){
		this.r = r;
		this.c = c;
		this.p = p;
		this.mem = mem;
		this.direction = d;
		this.species = species;
		this.id = nextid;
		nextid ++;
	}
	/**
	 * Loads critter from file
	 * @param filename
	 * @throws IOException 
	 */
	public static Critter loadCritter(String filename, int d, Simulator s, int col, int row) throws IOException{
		//possibly change to inputstream
    	Reader r = new FileReader(filename);
    	BufferedReader reader = new BufferedReader(r);
    	
    	//TODO: READ THE INITIAL CONDITIONS TO mem[]
       	//throws IOexception
    	String curline = reader.readLine();
    	//ignore comments
    	while ((curline.charAt(0) == '/' && curline.charAt(1) =='/' )|| curline.length() <1){
    		curline = reader.readLine();
    	}

    	String species = curline.split(": ")[1];
    	
    	curline = reader.readLine();
    	while ((curline.charAt(0) == '/' && curline.charAt(1) =='/' )|| curline.length() <1){
    		curline = reader.readLine();
    	}
    	int[] mem = new int[Integer.parseInt(curline.split(": ")[1])];
    	
    	//read initial mem conditions
    	for (int i =1; i < 5; i++){
    		//get the second half of each line split at ':'
    		curline = reader.readLine();
    		//ignore comments
    		while ((curline.charAt(0) == '/' && curline.charAt(1) =='/' )|| (curline.length() <1 && curline != null)){
        		curline = reader.readLine();
        	}
			mem[i] = Integer.parseInt(curline.split(": ")[1]);
    	}
    	curline = reader.readLine();
		//ignore comments
		while ((curline.charAt(0) == '/' && curline.charAt(1) =='/' )|| (curline.length() <1 && curline != null)){
    		curline = reader.readLine();
    	}
    	mem[7] = Integer.parseInt(curline.split(": ")[1]);
    	//reader.readLine(); //sets up
    	Parser parse = new ParserImpl();
    	Program p = parse.parse(reader);
    	mem[0] = 8;
    	return new Critter(col, row, p , mem, species, random.nextInt(6));
	}
	/**
	 * Method returns critical info about the critter
	 * 
	 * @return
	 */
	public String getInfo(){
		StringBuilder sb = new StringBuilder();
		sb.append("\nSpecies: " + this.species + "\n");
		for (int i = 0; i < mem.length; i++){
			sb.append("mem:" + i + " = "+ mem[i] + "\n");
		}
		sb.append("\n--------\nRule Set: \n--------\n");
		sb.append(this.program().prettyPrint(sb));
		sb.append("\n--------\nLast Rule Executed: \n--------\n");
		if (lastrule != null){
			sb.append(this.lastrule);
		}
		else
			sb.append("None");
		return sb.toString();
	}
	
	public String getSpecies() { return species; }
	
	/**
	 * Get the memory value for the critter at i
	 * @param i
	 * @return
	 */
	public int getMem(int i){
		return this.mem[i % mem.length];
	}
	public int[] getMemArray() { return mem; }
	/**
	 * Sets the memory value for this critter
	 * @param i
	 * @param value
	 */
	public void setMem(int i, int value){
		this.mem[i % mem.length] = value;
	}
	
	@Override
	public int sense(){
		return Math.abs(mem[3] * 100000 + mem[6] * 1000 + mem[7] * 10 + direction);
	}
	
	public int getDir(){
		return this.direction;
	}
	
	public Outcome eval(Interpreter i){
		return i.interpret(this.p, this);
	}
	
	public void turn(int dir){
		this.direction = Math.abs((direction + dir) % 6);
	}
	/**
	 * Returns the amount of energy actually consumed
	 * @param energy
	 * @return
	 */
	public int addEnergy(int energy){
		if (this.mem[4]  + energy > mem[3] * Simulator.ENERGY_PER_SIZE){
			int added = mem[3] * Simulator.ENERGY_PER_SIZE - mem[4];
			this.mem[4] = mem[3] * Simulator.ENERGY_PER_SIZE;
			return added;
		}
		else {
			this.mem[4] = mem[4] + energy;
			return energy;
		}
		
	}
	/**
	 * Returns true if critter has died
	 * @param energy
	 * @return
	 */
	public boolean decreaseEnergy(int energy){
		mem[4] -= energy;
		if (mem[4] < 0){
			return true; // dead
		}
		else {
			return false; // alive
		}
	}
	public int complexity(){
		return p.children().length * Simulator.RULE_COST + (mem[1]*mem[2]) * Simulator.ABILITY_COST;
	}
	
	public Critter bud(){
		int[] newmem = mem.clone();
		newmem[3] = 1;
		newmem[4] = Simulator.INITIAL_ENERGY;
		newmem[6] = 0;
		newmem[7] = 0;
		Program newp = random.nextInt(3) == 0 ? this.p.mutate() : this.p; // TODO: ADD MUTATION
		Critter c = new Critter(this.c, this.r - 1, newp, newmem, this.species, 0);
		return c;
	}
	
	public Critter mate(Critter mate){
		int r = random.nextInt(2);
		int memsize = r == 0 ?  mate.getMem(0) : this.mem[0];
		int[] newmem = new int[memsize];
		newmem[1] = random.nextInt(2) == 0 ? mate.getMem(1): this.mem[1];
		newmem[2] = random.nextInt(2) == 0 ? mate.getMem(2): this.mem[2];
		newmem[3] = 1;
		newmem[4] = Simulator.INITIAL_ENERGY;
		newmem[6] = 0;
		newmem[7] = 0;
		Rule[] matechildren = (Rule[]) mate.program().children();
		Rule[] children = (Rule[]) this.p.children();
		int numrules = random.nextInt(2) == 0 ? matechildren.length : children.length;
		Program newp = new ProgramImpl();
		Rule rule;
		//add rules to the new program
		for ( int i = 0; i < numrules; i++){
			if (i > matechildren.length -1){
				rule = children[i];
			}
			else if (i > children.length -1){
				rule = matechildren[i];
			}
			else {
				rule = random.nextInt(2) == 0 ? matechildren[i] : children[i];
			}
			newp.addNode(rule);
		}
		Critter c = new Critter(this.c, this.r - 1, newp, newmem, this.species, 0);
		return c;
	}
	
	public Program program(){
		return this.p;
	}
	
	public String toString(){
		return Integer.toString(direction);
	}
	
	public String[] getProperties() {
		String[] info = new String[3];
		info[0] = species;
		info[1] = p.prettyPrint(new StringBuilder()).toString();
		if (lastrule != null){
			info[2] = lastrule;
		} else { info[2] = "";}
		return info;
	}
	
}
