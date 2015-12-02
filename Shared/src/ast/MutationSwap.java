package ast;

import java.util.Random;

/** Mutation that switches the order of two children of a node if possible */
public class MutationSwap implements Mutation {
	private Random r = new Random();
	@Override
	public Node mutate(Node n, Program p) {
		Node[] nodes = n.children();
		Node placeholder = MutationShared.getPlaceholder(n.children()[0].getClass().toString());
		if (n.children().length > 1){
			n.replaceChild(n.children()[0], placeholder);
			n.replaceChild(n.children()[1], n.children()[0]);
			n.replaceChild(placeholder, n.children()[1]);
		}
		return p;
	}

	@Override
	public boolean equals(Mutation m) {
		if (m instanceof MutationSwap)
			return true;
		return false;
	}

	@Override
	public Node getRandomValid(Program p) {
		String[] valid = new String[]{"ast.BinaryCondition", "ast.BinaryConjunction", "ast.BinaryExpr"};
		return MutationShared.getRandomValid(p, valid);
	}

}
