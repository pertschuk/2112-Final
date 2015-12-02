package ast;

import parse.*;

public class ExprImpl extends ASTNode implements Expr {
	
	protected Term term1;
	
	/**
	 * Default constructor creates a expression with one term 
	 * @param parseTerm
	 */
	public ExprImpl(Term parseTerm) {
		this.term1 = parseTerm;
		this.children.add(parseTerm);
	}
	
	@Override
	public void replaceChild(Node n, Node old){
		super.replaceChild(old, n);
		this.term1 = (Term) n;
	}
	
	//Default constructor needed for inheritance in BinaryExpr
	public ExprImpl(){
		
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		sb.append(term1.prettyPrint(new StringBuilder()));
		return sb;
	}

}
