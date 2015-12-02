package ast;

/**
 * Class represents an AST node that represent a integer number value
 * @author jack
 *
 */
public class Number extends ASTNode {
	
	private int number;
	
	/**
	 * Creates a new number Node
	 * @param i Value of new Node
	 */
	public Number(int i){
		this.number = i;
	}
	
	/**
	 * 
	 * @return the int value of this AST node
	 */
	public int getValue(){
		return number;
	}
	
	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		sb.append(number);
		return sb;
	}

}
