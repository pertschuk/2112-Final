package controller;

public class LoadMapEvent implements Event {
	private int[][] map;
	public LoadMapEvent(int[][] map) {
		this.map = map;
	}
	public int[][] getMap(){
		return map;
	}
}
