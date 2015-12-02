package ast;

import java.util.LinkedList;

public class Command extends ASTNode {
	//will hold references to all updates or actions in the command
	private LinkedList<Node> ua;
	
	public Command() {
		ua = new LinkedList<Node>();
	}
	
	public void addUpdate(Update u) {
		ua.add(u);
		this.children.add(u);
	}
	
	public void addAction(Action a) {
		ua.add(a);
		this.children.add(a);
	}
	
	public void replaceChild(Node n, Node old){
		this.ua.remove(old);
		this.ua.add(n);
		super.replaceChild(old, n);
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		for (Node n : ua)
			sb.append(n.prettyPrint(new StringBuilder()));
		return sb;
	}
	
	/**
	 * Returns updates and actions in command in array
	 * @return  array of command's updates and action
	 */
	public Node[] toArray() { 
		Node[] node = new Node[10];
		return ua.toArray(node); }
}
