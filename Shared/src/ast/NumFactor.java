package ast;

public class NumFactor extends Factor {
	private int value;
	/**
	 * Creates a factor that contains a primitive integer node.
	 * @param number
	 */
	public NumFactor (int number){
		this.value = number;
		this.children.add(new Number(number));
	}
	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		sb.append(value);
		return sb;
	}
}
