package ast;

public class ConditionImpl extends ASTNode implements Condition {
	
	private Conjunction conjunction;
	
	/**
	 * Constructor creates a new condition node with a conjunction as a child
	 * @param conj Conjunction that is its child
	 */
	public ConditionImpl(Conjunction conj){
		this.conjunction = conj;
		children.add(conj);
	}
	/**
	 * Default constructor called by subclasses
	 */
	public ConditionImpl(){
		
	}
	
	public void replaceChild(Node old, Node n){
		this.conjunction = (Conjunction) n;
		super.replaceChild(old, n);
	}
	
	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		sb.append(conjunction.prettyPrint(new StringBuilder()));
		return sb;
	}

	@Override
	public boolean Evaluate() {
		// TODO Auto-generated method stub
		return false;
	}

}
