/**
 * 
 */
package ast;

import java.util.LinkedList;

/**
 * Abstract class to represent the shared functionality of all nodes of an Abstract Syntax tree.
 * 
 * @author Jack Pertschuk
 *
 */
public abstract class ASTNode implements Node {
	
	LinkedList<Node> children;
	
	int size;
	
	public ASTNode(){
		children = new LinkedList<Node>();
	}
	
	public Node[] children(){
		return children.toArray(new Node[0]);
	}
	
	/**
	 * Adds given child
	 * @param n Node child to add
	 */
	public void addChild(Node n){
		children.add(n);
	}
	
	/**
	 * Removes given child
	 * @param n Node child to remove
	 */
	public void removeChild(Node n){
		children.remove(n);
	}
	
	public void replaceChild(Node old, Node n) {
		children.remove(old);
		children.add(n);
	}
	@Override
	// Precondition: size = 0; REQUIRES: each node has 2 children; Invariant: this is always a child of what used to be this
	public int size() {
		size = 1;
		for (Node child: children){
			size += child.size();
		}
		return size;
	}

	@Override
	public Node nodeAt(int index) throws IndexOutOfBoundsException {
		if (index > size() || index < 0) throw new IndexOutOfBoundsException();
		int i = 0;
		Node current = this;
		LinkedList<Node> queue = new LinkedList<Node>();
		//Precondition; i is less than index; Node is the head of the AST; Postcondition: current is the node at index
		while (i < index){
			for(Node child: current.children()){
				queue.add(child);
			}
			current = queue.removeFirst();
			if (current == null){
				throw new IndexOutOfBoundsException();
			}
			i++;
		}
		return current;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		return this.prettyPrint(sb).toString();
	}
}
