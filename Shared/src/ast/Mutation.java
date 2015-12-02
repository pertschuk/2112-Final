package ast;

/**
 * A mutation to the AST
 */
public interface Mutation {
	/**
	 * Performs a mutation on a node returning the mutated node
	 * @param n  the node to be mutated
	 * @param p the program which the node is rooted at
	 * @return  the mutated node
	 */
	Node mutate(Node n, Program p);
	
    /**
     * Compares the type of this mutation to {@code m}
     * 
     * @param m
     *            The mutation to compare with
     * @return Whether this mutation is the same type as {@code m}
     */
    boolean equals(Mutation m);
    
    /**
     * Returns a a valid child of program p that this mutation
     * can be performed on.
     * @return
     */
	public Node getRandomValid(Program p);
}
