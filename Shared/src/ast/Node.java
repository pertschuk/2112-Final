package ast;

/**
 * A node in the abstract syntax tree of a program.
 */
public interface Node {
	
	/**
	 * Adds a child to the given node
	 * @param n Node to add
	 */
	public void addChild(Node n);
	
	/**
	 * Removes child from given node
	 * @param n Node to remove
	 */
	public void removeChild(Node n);
	
	/**
	 * Replaces an old with a new one
	 * @param old
	 * @param n
	 */
	public void replaceChild(Node old, Node n);
	/**
	 * @return An array of Nodes containing the {@code children} of the current node
	 */
	Node[] children();
    /**
     * The number of nodes in the AST rooted at this node, including this node
     * 
     * @return The size of the AST rooted at this node
     */
    int size();

    /**
     * Returns the node at {@code index} in the AST rooted at this node. Indices
     * are defined such that:<br>
     * 1. Indices are in the range {@code [0, size())}<br>
     * 2. {@code this.nodeAt(0) == this} for all nodes in the AST <br>
     * 3. All nodes in the AST rooted at {@code this} must be reachable by a
     *    call to {@code this.nodeAt(i)} with an appropriate index {@code i}
     * 
     * @param index The index of the node to retrieve
     * @return The node at {@code index}
     * @throws IndexOutOfBoundsException
     *             if {@code index} is not in the range of valid indices
     */
    Node nodeAt(int index);

    /**
     * Appends the program represented by this node prettily to the given
     * StringBuilder.
     * <p>
     * The output of this method must be consistent with both the critter 
     * grammar and itself; that is:<br>
     * 1. It must be possible to put the result of this method into a valid 
     *    critter program<br>
     * 2. Placing the result of this method into a valid critter program then 
     *    parsing the program must yield an AST which contains a subtree 
     *    identical to the one rooted at {@code this}
     * 
     * @param sb The {@code StringBuilder} to which the program will be appended
     * @return The {@code StringBuilder} to which this program was appended
     */
    StringBuilder prettyPrint(StringBuilder sb);
    
    /**
     * Returns the pretty-print of the abstract syntax subtree rooted at this
     * node.
     * <p>
     * This method returns the same result as {@code prettyPrint(...).toString()}
     * @return The pretty-print of the AST rooted at this node.
     */
    @Override
    String toString();
}
