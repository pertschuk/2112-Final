package interpret;

/**
 * A representation of the action to be performed by the critter
 * after a call to interpreter
 * @author Jack Solon
 */
public class OutcomeImpl implements Outcome {
	private String type;
	private int exprVal; //will be set to -1 if type of action
					     //has no value
	
	/**
	 * Constructor to use if action has no expression
	 * @param type  the type of action
	 */
	public OutcomeImpl(String type) {
		this.type = type;
		exprVal = -1;
	}
	
	/**
	 * Constructor to use if action has a significan expression
	 * as in tag [ expr ] and serve [ expr ]
	 * @param type  the type of action
	 * @param value the value of expr
	 */
	public OutcomeImpl(String type, int exprVal) {
		this.type = type;
		this.exprVal = exprVal;
	}
	
	@Override
	public boolean hasValue() {
		return exprVal != -1;
	}
	
	@Override
	public String getType() { return type; }
	
	@Override
	public int getValue() { return exprVal; }
}
