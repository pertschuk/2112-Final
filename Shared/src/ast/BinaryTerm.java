package ast;

/**
 * Class represent a term with two children
 * @author jack
 *
 */
public class BinaryTerm extends Term {
	
	private Term t1;
	private Term t2; 
	private MulOp op;
	
	/**
	 * Constructor creates a binary term with two children, one factor and
	 * another term. Thus is is recursively defined
	 * @param f
	 * @param t
	 */
	public BinaryTerm(Term t1, MulOp op, Term t2){
		this.children.add(t1);
		this.children.add(t2);
		this.children.add(op);
		this.t1 = t1;
		this.op = op;
		this.t2 = t2;
	}
	
	public void replaceChild(Node n, Node old){
		if (this.t1 == old){
			this.t1 = (Term) n;
		}
		else if (this.t2 == old){
			this.t2 = (Term) n;
		}
		this.removeChild(old);
		this.addChild(n);
	}
	
	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		sb.append(t1.prettyPrint(new StringBuilder()));
		sb.append(" " + op + " ");
		sb.append(t2.prettyPrint(new StringBuilder()));
		return sb;
	}
}
