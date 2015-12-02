package controller;

public class UpdateMapEvent implements Event {
	private int[][] map;
	public UpdateMapEvent(int[][] map) {
		this.map = map;
	}
	public int[][] getMap(){
		return map;
	}
	
}
