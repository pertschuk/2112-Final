package ast;


public class Conjunction extends ASTNode {
	
	private Relation relation;
	
	/**
	 * Default super constructor for subclasses
	 */
	public Conjunction(){
		
	}
	
	/**
	 * Constructor for a conjunction with one relation as its child
	 * 
	 * @param r
	 */
	public Conjunction(Relation r){
		relation = r;
		this.children.add(r);
	}
	
	public void replaceChild(Node n, Node old){
		this.relation = (Relation) n;
		super.replaceChild(old, n);
	}
	
	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		sb.append(relation.prettyPrint(new StringBuilder()));
		return sb;
	}

}
