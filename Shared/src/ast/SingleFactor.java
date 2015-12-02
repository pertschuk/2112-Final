package ast;

public class SingleFactor extends Factor {
	private Expr expression;
	/**
	 * Case that the factor can be resolved to a single expression in ()
	 * 
	 * @param expression
	 */
	public SingleFactor(Expr expression){
		this.children.add(expression);
		this.expression = expression;
	}
	
	public void replaceChild(Node old, Node n){
		this.removeChild(old);
		this.addChild(n);
		this.expression = (Expr) n;
	}
	
	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		sb.append("( " + expression.prettyPrint(new StringBuilder()) + " )");
		return sb;
	}
}
