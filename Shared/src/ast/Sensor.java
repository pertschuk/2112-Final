package ast;

public class Sensor extends ASTNode {
	private String type;
	private Expr expression;
	
	/**
	 * 
	 * @param type
	 * @param expression
	 */
	public Sensor(String type, Expr expression ){
		this.type = type;
		this.expression = expression;
	}
	
	/**
	 * Case that there is no expression, e.g. {@code smell}
	 * @param type
	 */
	public Sensor(String type){
		this.type = type;
		expression = null;
	}
	
	public void replaceChild(Node n, Node old){
		this.expression = (Expr) n;
		super.replaceChild(old, n);
	}
	
	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		sb.append(type);
		if (expression != null)
			sb.append(" [" + expression.prettyPrint(new StringBuilder()) + "]");
		return sb;
	}
	
	/**Getter methods for use in interpreter */
	public String getType() { return type; }
	public Expr getExpr() { return expression; }
}
