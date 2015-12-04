package json;

import java.io.StringReader;
import java.util.Scanner;

import parse.ParserImpl;
import simulator.*;

public class HexBundle {
	public int row, col;
	public String type;
	public int value;
	public int id;
	public String species_id;
	public String program;
	public int[] mem;
	public String recently_executed_rule;
	public int direction;
	public HexBundle(Critter c){
		row = c.getRow();
		col = c.getCol();
		species_id = c.getSpecies();
		program = c.getProperties()[1]; // the program
		mem = c.getMemArray();
		direction = c.getDir();
		/*
		Scanner s = new Scanner(program);
		int i = 0;
		while (s.hasNext()){
			if (s.nextLine() == c.lastrule.toString()){
				break;
			}
			i++;
		}*/
		type = "critter";
		recently_executed_rule = c.lastrule.toString();
	}
	public HexBundle(Rock r){
		row = r.getRow();
		col = r.getCol();
		type = "rock";
	}
	public HexBundle(Food f){
		row = f.getRow();
		col = f.getCol();
		type = "food";
		value = f.getFood();
	}
	public HexBundle(int col, int row){
		this.row = row;
		this.col = col;
		this.type = "none";
	}
	public HexBundle(Hex h) {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Bundle with limited info
	 * @param c
	 * @param b
	 */
	public HexBundle(Critter c, boolean b) {
		row = c.getRow();
		col = c.getCol();
		species_id = c.getSpecies();
		mem = c.getMemArray();
		direction = c.getDir();
		type = "critter";
	}
	public Hex toHex() {
		if (type.equals("rock")){
			return new Rock(col, row);
		}
		if (type.equals("food")){
			return new Food(col, row, value);
		}
		if (type.equals("none")){
			return new Empty(col, row);
		}
		if (type.equals("critter")){
			ParserImpl parser = new ParserImpl();
			Critter c =  new Critter(col, row, parser.parse(new StringReader(this.program)), mem, species_id, direction);
			c.id = this.id;
			c.lastrule = this.recently_executed_rule;
			return c;
		}
		return null;
	}
}
