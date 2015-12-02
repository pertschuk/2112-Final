package interpret;

/**
 * An example interface for representing an outcome of interpreting
 * a critter program.
 */
public interface Outcome {
	/**
	 * Method determines whether or not the outcome has a significant
	 * value
	 * @return  true  if this has a significant value
	 * 			false if this has no significant value
	 */
	public boolean hasValue();
	
	/**
	 * Getter for type of Outcome
	 * @return  String value of Outcome 
	 */
	public String getType();
	
	/**
	 * Getter for value of Outcome
	 * @return  int value of Outcome
	 * 		    will be -1 if hasValue() is false
	 */
	public int getValue();
}
