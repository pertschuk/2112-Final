package simulator;

public class Rock extends Hex {
	public Rock(int c, int r) {
		this.r = r;
		this.c = c;
	}

	public String getInfo(){
		return "I am a rock";
	}
	
	@Override
	public int sense(){
		return -1;
	}
	public String toString(){
		return "#";
	}
}
