package ast;

public class BinaryExpr extends ExprImpl {
	
	private Expr e1;
	private Expr e2;
	private AddOp op;
	
	/**
	 * Constuctor in the case that both children are expressions
	 * @param term1
	 * @param term2
	 */
	public BinaryExpr(Expr e1, AddOp op, Expr e2){
		this.e1 = e1;
		this.e2 = e2;
		this.op = op;
		this.children.add(e1);
		this.children.add(e2);	
		this.children.add(op);
	}
	
	public void replaceChild(Node n, Node old){
		if (this.e1 == old){
			this.e1 = (Expr) n;
		}
		else if (this.e2 == old){
			this.e2 = (Expr) n;
		}
		super.replaceChild(old, n);
	}
	
	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		sb.append(e1.prettyPrint(new StringBuilder()));
		sb.append(" " + op + " ");
		sb.append(e2.prettyPrint(new StringBuilder()));
		return sb;
	}
}
