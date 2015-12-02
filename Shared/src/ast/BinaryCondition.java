package ast;

/**
 * A representation of a binary Boolean condition: 'and' or 'or'
 *
 */
public class BinaryCondition extends ConditionImpl {
	
	private Operator operator;
	private Condition l,r;
    /**
     * Create an AST representation of l op r.
     * @param l
     * @param op takes an ENUM Operator type of AND or OR
     * @param r
     */
    public BinaryCondition(Condition l, Operator op, Condition r) {
    	this.l = l;
    	this.r = r;
        children.add(l);
        children.add(r);
        this.operator = op;
    }
    
    @Override 
	public void replaceChild(Node n, Node old){
		if (this.l == old){
			this.l = (Condition) n;
		}
		else if (this.r == old){
			this.r = (Condition) n;
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

    /**
     * An enumeration of all possible binary condition operators.
     */
    public enum Operator {
        OR, AND;
    }

	@Override
	public boolean Evaluate() {
		return false;
	}
}
