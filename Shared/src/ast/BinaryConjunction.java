package ast;

/**
 * Class represents a Binary Conjunction (with 2 relations as children)
 * @author jack
 *
 */
public class BinaryConjunction extends Conjunction {
	
	private BinaryCondition.Operator operator;
	Conjunction l, r; 
	
	/**
	 * Creates a new BinaryConjunction with 2 conjunctions as children
	 * @param l
	 * @param op
	 * @param r
	 */
	public BinaryConjunction(Conjunction l, BinaryCondition.Operator op, Conjunction r) {
		this.l = l;
		this.r = r;
        children.add(l);
        children.add(r);
        this.operator = op;
    }
	
	@Override 
	public void replaceChild(Node n, Node old){
		if (this.l == old){
			this.l = (Conjunction) n;
		}
		else if (this.r == old){
			this.r = (Conjunction) n;
		}
		this.removeChild(old);
		this.addChild(n);
	}
	
	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		sb.append(l.prettyPrint(new StringBuilder()));
		sb.append(" " + operator + " ");
		sb.append(r.prettyPrint(new StringBuilder()));
		return sb;
	}
}
