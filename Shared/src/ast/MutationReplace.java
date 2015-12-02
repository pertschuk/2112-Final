package ast;

import java.util.Arrays;
import java.util.Random;

/** The node and its children are replaced with a copy of another randomly selected 
 * node of the right kind, found somewhere in the rule set. The entire AST subtree 
 * rooted at the selected node is copied.
 */
public class MutationReplace implements Mutation {

	
	Random r = new Random();
	
	@Override
	public Node mutate(Node n, Program p) {
		Node parent = MutationShared.findParent(n, p);
		
		Node[] nodes = MutationShared.getAllChildren(p, n.getClass().toString());
		if (nodes.length >0){
			Node newnode = nodes[r.nextInt(nodes.length)];

			parent.replaceChild(n, newnode);
			return newnode;
		}
		else {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public boolean equals(Mutation m) {
		if (m instanceof MutationRemove)
			return true;
		return false;
	}

	@Override
	public Node getRandomValid(Program p) {
		String[] valid = new String[]{"ast.ExprImpl"};
		return MutationShared.getRandomValid(p, valid);
	}

}
