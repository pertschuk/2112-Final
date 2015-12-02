package ast;

/** Update --> mem[ exp1 ] := exp2 */
public class Update extends ASTNode {

	private Expr exp1, exp2;
	
	/**
	 * Only constructor for an Update. Requires two expressions,
	 * one to assign and one to get assigned.
	 * 
	 * @param exp1
	 * @param exp2
	 */
	public Update(Expr exp1, Expr exp2){
		this.exp1 = exp1;
		this.exp2 = exp2;
		this.children.add(exp1);
		this.children.add(exp2);
	}
	
	public void replaceChild(Node n, Node old){
		if (this.exp1 == old){
			this.exp1 = (Expr) n;
		}
		else if (this.exp2 == old){
			this.exp2 = (Expr) n;
		}
		super.replaceChild(old, n);
	}
	
	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		sb.append("mem[ " + exp1.prettyPrint(new StringBuilder()) + " ] := " + exp2.prettyPrint(new StringBuilder()) + " ");
		return sb;
	}

}
