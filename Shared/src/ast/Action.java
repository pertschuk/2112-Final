package ast;

public class Action extends ASTNode {
	private String action;
	private Expr e;
	
	/**
	 * Case that action doesn't require an expression
	 * 
	 * @param action
	 */
	public Action(String action){
		this.action = action;
		e = null;
	}
	
	/**
	 * Case where action accepts an expression to evaluate
	 * 
	 * @param action
	 * @param expression
	 */
	public Action(String action, Expr expression){
		this.action = action;
		e = expression;
		this.children.add(expression);
	}
	
	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		sb.append(action);
		if (e != null)
			sb.append(" [" + e.prettyPrint(new StringBuilder()) + "]");
		return sb;
	}
	
	/**getters to be used in interpreter*/
	public String getAction() { return action; }
	public Expr getExpr() { return e; }
}
