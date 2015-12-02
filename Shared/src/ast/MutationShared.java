package ast;

import java.util.*;

/**
 * Class that contains some shared functionality between Mutation Classes
 * @author jack
 *
 */
public class MutationShared {
	private static Random r = new Random();
	public static Factor randomFactor(Expr e){
		
		int i = r.nextInt(6);
		if (i == 0) {
			return new MemFactor(e);
		}
		if (i == 1) {
			return new NegFactor(new SingleFactor(e));
		}
		if (i == 2){
			return new SingleFactor(e);
		}
		if (i == 3){
			return new SensorFactor(new Sensor("nearby", e));
		}
		if (i == 4){
			return new SensorFactor(new Sensor("ahead", e));
		}
		if (i == 5){
			return new SensorFactor(new Sensor("random", e));
		}
		return null;
	}
	
	/**
	 * 
	 * @param p
	 * @param type
	 * @return
	 */
	public static Node[] getAll(Program p, String type){
		LinkedList<Node> nodes = new LinkedList<Node>();
		for (int i = 0; i < p.size(); i++){
			if (p.nodeAt(i).getClass().toString().equals(type)){
				nodes.add(p);
			}
		}
		Node[] nodearray = new Node[nodes.size()];
		return nodes.toArray(nodearray);
	}
	
	/**
	 * 
	 * @param n
	 * @param type
	 * @return
	 */
	public static Node[] getAllChildren(Node n, String type) {
		LinkedList<Node> nodes = new LinkedList<Node>();
		Node current = n;
		LinkedList<Node> queue = new LinkedList<Node>();
		queue.add(current);
		//loop through all children of n
		while (queue.size() !=0 ){
			current = queue.removeFirst();
			for(Node child : current.children()){
				//if this node is of type type, add to nodes
				if (child != null) { queue.add(child);}
				if (child != null && child.getClass().toString().equals(type))
					nodes.add(child);
			}
		}
		Node[] nodearray = new Node[nodes.size()];
		return nodes.toArray(nodearray);
	}
	/**Function that finds the parent of a Node
	 * 
	 * @param n
	 * @param p
	 * @return
	 */
	public static Node findParent(Node n, Program p) {
		int i = 0;
		NodeWithParent current = new NodeWithParent(p, null);
		LinkedList<NodeWithParent> queue = new LinkedList<NodeWithParent>();
		
		while (i < p.size()){
			for(Node child: current.n.children()){
				queue.add(new NodeWithParent(child, current.n));
			}
			if(current.n == n){
				return current.parent;
			}
			current = queue.removeFirst();
			i++;
		}
		
		return null;
	}
	/**
	 * Simple class that links nodes with parents
	 * @author jack
	 *
	 */
	private static class NodeWithParent {
		public Node n;
		public Node parent;
		public NodeWithParent(Node n, Node parent){
			this.n = n;
			this.parent = parent;
		}
	}
	
	/**
	 * Returns a random valid node
	 * @param p
	 * @param valid
	 * @return
	 */
	public static Node getRandomValid(Program p, String[] valid){
		LinkedList<Node> nodelist = new LinkedList<Node>();
		Node[] nodes;
		for (String s : valid){
			nodes =  MutationShared.getAllChildren(p, "class " + s);
			nodelist.addAll(Arrays.asList(nodes));
		}
		if (nodelist.size() > 0) {
			return nodelist.get(r.nextInt(nodelist.size()));
		}
		else{
			throw new UnsupportedOperationException();
		}
	}
	public static Node getPlaceholder(String type){
		switch(type){
		case "class ast.BinaryCondition":
			return new BinaryCondition(new ConditionImpl(), BinaryCondition.Operator.OR, new ConditionImpl());
		case "class ast.BinaryExpr":
			return new BinaryExpr(null, null, null);
		case "class ast.BinaryConjunction":
			return new BinaryConjunction(null, null, null);
		case "class ast.Relation":
			return new Relation(null);
		}
		return null;
	}
}
