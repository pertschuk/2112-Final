package simulator;

public class Food extends Hex {
	private int food;
	
	/**
	 * Constructor for a food object
	 * @param r  row
	 * @param c  column
	 * @param food  amount of food
	 */
	public Food(int c, int r, int food){
		this.r = r;
		this.c = c;
		this.food = food;
	}
	/**
	 * 
	 * @return The amount of food on a hex;
	 */
	public int getFood(){
		return food;
	}
	
	public String getInfo(){
		return "I am " + food + " food.";
	}
	@Override
	public int sense(){
		return -food - 1;
	}
	public void setFood(int f) {
		this.food = f;	
	}
	public void addFood(int f){
		this.food += f;
	}
	public String toString(){
		return "F";
	}
}
