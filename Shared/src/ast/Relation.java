package ast;

public class Relation extends ASTNode {
	
	private Expr expr1, expr2;
	private RelOp op;
	private Condition c;
	
	/**
	 * Default constructor make a new relation with expr, rel, expr
	 * 
	 * @param expr1
	 * @param op
	 * @param expr2
	 */
	public Relation(Expr expr1, RelOp op, Expr expr2){
		this.expr1 = expr1;
		this.expr2 = expr2;
		this.op = op;
		this.children.add(expr1);
		this.children.add(expr2);
		this.children.add(op);
	}
	
	/**
	 * In the case that Relation resolves to {condition}
	 * @param c
	 */
	public Relation(Condition c){
		this.c = c;
		this.children.add(c);
	}
	
	@Override
	public void replaceChild(Node old, Node n){
		super.replaceChild(old, n);
		if (n instanceof Expr){
			if (this.expr1 == old){
				this.expr1 = (Expr) n;
			}
			else {
				this.expr2 = (Expr) n;
			}
		}
		else  {
			this.op = (RelOp) n;
		}
	}
	
	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		if (c == null) {
			sb.append(expr1.prettyPrint(new StringBuilder()));
			sb.append(" " + op + " ");
			sb.append(expr2.prettyPrint(new StringBuilder()));
		} else {
			sb.append("{ " + c.prettyPrint(new StringBuilder()) + " }");
		}
		return sb;
	}

}
