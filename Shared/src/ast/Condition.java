package ast;

/**
 * An interface representing a Boolean condition in a critter program.
 *
 */
public interface Condition extends Node {
	/**
	 * Evaluated whether this condition is true or not
	 * 
	 * @return True is the condition is true, false if it is not
	 */
	public boolean Evaluate();
}
