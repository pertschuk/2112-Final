package json;

import java.util.Scanner;

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
	public HexBundle(Critter c){
		row = c.getRow();
		col = c.getCol();
		species_id = c.getSpecies();
		program = c.getProperties()[1]; // the program
		mem = c.getMemArray();/*
		Scanner s = new Scanner(program);
		int i = 0;
		while (s.hasNext()){
			if (s.nextLine() == c.lastrule.toString()){
				break;
			}
			i++;
		}*/
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
	public Hex toHex() {
		// TODO Auto-generated method stub
		return null;
	}
}
