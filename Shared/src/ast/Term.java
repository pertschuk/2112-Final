package ast;

public class Term extends ASTNode {
	
	private Factor f;
	
	/**
	 * Default constructor for a Term with one factor
	 * @param f
	 */
	public Term(Factor f){
		this.f = f;
		this.children.add(f);
	}
	
	//Default constructor needed for inheritance in BinaryTerm
	public Term() {
		
	}
	
	public void replaceChild(Node n, Node old){
		this.f = (Factor) n;
		super.replaceChild(old, n);
	}
	
	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		sb.append(f.prettyPrint(new StringBuilder()));
		return sb;
	}

}
