package ast;

/** Mutation that removes a node and replaces it with a random child if necessary */
public class MutationRemove implements Mutation {

	@Override
	public Node mutate(Node n, Program p) {
		Node parent = MutationShared.findParent(n, p);
		//if n is a program do nothing
		if (n instanceof Program) return n;
		else if (n instanceof Rule) {
			//remove the node
			parent.replaceChild(n,null);
			n = null;
			return n;
		}

		else if (n instanceof Conjunction) {
			if (n instanceof BinaryConjunction) {
				if (Math.random() < 0.5)
					parent.replaceChild(n, n.children()[0]);
				else
					parent.replaceChild(n, n.children()[1]);
				return n;
			} else {
				Node[] nConjChildren = MutationShared.getAllChildren(n,"Conjunction");
				if (nConjChildren.length > 0) {
					parent.replaceChild(n, nConjChildren[(int)(Math.random()*nConjChildren.length)]);
				}
				//otherwise do nothing
				return n;
			}
		} else if (n instanceof Relation) {
			Node[] nRelChildren = MutationShared.getAllChildren(n,"Relation");
			if (nRelChildren.length > 0) {
				parent.replaceChild(n, nRelChildren[(int)(Math.random()*nRelChildren.length)]);
			}
			parent = MutationShared.findParent(parent, p);
			Node binaryparent = MutationShared.findParent(parent, p);	
			binaryparent.replaceChild(parent, parent.children()[1]);
			//otherwise do nothing
			return n;
		} else if (n instanceof Expr) {
			if (n instanceof BinaryExpr) {
				if (Math.random() < 0.5)
					parent.replaceChild(n, n.children()[0]);
				else
					parent.replaceChild(n, n.children()[1]);
				return n;
			} else {
				Node[] nExprChildren = MutationShared.getAllChildren(n,"Expr");		
				if (nExprChildren.length > 0) {
					parent.replaceChild(n, nExprChildren[(int)(Math.random()*nExprChildren.length)]);
				}
				//otherwise do nothing
				return n;
			}
		} else if (n instanceof Term) {
			if (n instanceof BinaryTerm) {
				if (Math.random() < 0.5)
					parent.replaceChild(n, n.children()[0]);
				else
					parent.replaceChild(n, n.children()[1]);
				return n;
			} else {
				Node[] nTermChildren = MutationShared.getAllChildren(n,"Term");
				if (nTermChildren.length > 0) {
					parent.replaceChild(n, nTermChildren[(int)(Math.random()*nTermChildren.length)]);
				}
				//otherwise do nothing
				return n;
			}
		} else if (n instanceof Factor) {
			Node[] nFactChildren = MutationShared.getAllChildren(n,"Factor");
			if (nFactChildren.length > 0) {
				parent.replaceChild(n, nFactChildren[(int)(Math.random()*nFactChildren.length)]);
			}
			//otherwise do nothing
			return n;
		} else if (n instanceof Command) {
			//do nothing
			return n;
		} else if (n instanceof Update) {
			if (parent.children().length > 1) {
				parent.replaceChild(n, null);
				n = null;
			}
			return n;
		} else if (n instanceof Action) {
			//do nothing
			return n;
		} else {
			//do nothing
			return n;
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
		//not done
		String[] valid = new String[]{"ast.Factor", "ast.Rule", "ast.Conjunction", "ast.Relation", "ast.Term", "ast.Factor", "ast.Update" };
		return MutationShared.getRandomValid(p, valid);
	}
}
