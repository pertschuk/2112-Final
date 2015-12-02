package ast;

/** factor --> sensor */
public class SensorFactor extends Factor {
	private Sensor s;
	/**
	 * Constructor creates a factor with a single Sensor child
	 * 
	 * @param s Sensor that is a child of factor
	 */
	public SensorFactor(Sensor s) {
		this.s = s;
		this.children.add(s);
	}
	
	public void replaceChild(Node old, Node n){
		this.removeChild(old);
		this.addChild(n);
		this.s = (Sensor) n;
	}
	
	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		sb.append(s.prettyPrint(new StringBuilder()));
		return sb;
	}
}
