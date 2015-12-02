package ast;

import java.util.Random;

/** Node is replaced with randomly chosen node of same kind, but children stay same */
public class MutationTransform implements Mutation {

	@Override
	public Node mutate(Node n, Program p) {
		
		Random r = new Random();
		
		Node parent = MutationShared.findParent(n, p);

		Node[] nodes = MutationShared.getAllChildren(p, n.getClass().toString());
		
		Node[] nodecopy = nodes.clone();
		if (nodes.length >0){
			Node newnode = nodecopy[r.nextInt(nodes.length)];
			//remove all children from the node
			int newchildren = n.children().length;
			int oldchildren = newnode.children().length;
			
			for (int i = 0; i < Math.min(newchildren, oldchildren); i++){
				
				newnode.replaceChild(newnode.children()[i], n.children()[i]);
			}
			//possible code to handle imbalance

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
		String[] valid = new String[]{"ast.Factor", "ast.Rule", "ast.Conjunction", "ast.Relation", "ast.Term", "ast.Factor", "ast.Update"};
		return MutationShared.getRandomValid(p, valid);
	}

}
