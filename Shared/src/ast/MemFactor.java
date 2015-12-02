package ast;

public class MemFactor extends Factor {
	private Expr expression;
	
	/**
	 * Creates a factor of mem[expr]
	 * 
	 * @param expr
	 * @param i
	 */
	public MemFactor (Expr expr){
		this.children.add(expr);
		this.expression = expr;
	}
	
	@Override 
	public void replaceChild(Node old, Node n){
		this.removeChild(old);
		this.addChild(n);
		this.expression = (Expr) n;
	}
	
	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		sb.append("mem[" + expression.prettyPrint(new StringBuilder()) + "]");
		return sb;
	}
}
