package ast;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

import javax.naming.OperationNotSupportedException;

/** Newly created node is inserted at the parent of the mutated node */
public class MutationInsert implements Mutation {
	Random r = new Random();
	String[] valid;
	@Override
	public Node mutate(Node n, Program p) throws UnsupportedOperationException {
		//gets parent node
		Node parent = MutationShared.findParent(n, p);
		
		Random r = new Random();
		BinaryCondition.Operator op;
		//decides what operator to use
		
		if( r.nextInt(2) == 0) {
			op = BinaryCondition.Operator.AND;
		}
		else {
			op = BinaryCondition.Operator.OR;
		}
		
		//Get all nodes of that subtype in the rule set
		Node[] nodes = MutationShared.getAll(p, n.getClass().toString());
		
		if (n instanceof Expr){
			parent.replaceChild(n, new ExprImpl(new Term(MutationShared.randomFactor((Expr) n))));
		}
		else {
			throw new UnsupportedOperationException();
		}
		return n;
	}

	@Override
	public boolean equals(Mutation m) {
		//returns true if they are instances of the same class
		return this.getClass().equals(m.getClass());
	}

	@Override
	public Node getRandomValid(Program p) {
		//valid types
		String[] valid = new String[]{"ast.ExprImpl" };
		return MutationShared.getRandomValid(p, valid);
	}

}
