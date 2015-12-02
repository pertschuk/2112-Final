package ast;

public class NegFactor extends Factor {
	private Factor factor;
	/**
	 * Case that factor = -factor
	 * 
	 * @param factor The factor to negate
	 */
	public NegFactor (Factor factor){
		this.children.add(factor);
		this.factor = factor;
	}
	
	@Override 
	public void replaceChild(Node old, Node n){
		super.replaceChild(old, n);
		this.factor = (Factor) n;
	}
	
	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		sb.append("-" + factor.prettyPrint(new StringBuilder()));
		return sb;
	}
}
