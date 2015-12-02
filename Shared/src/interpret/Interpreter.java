package interpret;

import ast.*;
import simulator.*;

/**
 * An example interface for interpreting a critter program. This is just a starting
 * point and may be changed as much as you like.
 */
public interface Interpreter {
    /**
     * Execute program {@code p} until either the maximum number of rules per
     * turn is reached or some rule whose command contains an action is
     * executed.
     * @param p
     * @param c
     * @return a result containing the action to be performed;
     * the action may be null if the maximum number of rules
     * per turn was exceeded.
     */
    Outcome interpret(Program p, Critter c);

    /**
     * Evaluate the given condition.
     * @param cd
     * @param c
     * @return a boolean that results from evaluating c.
     */
    boolean eval(Condition cd, Critter c);

    /**
     * Evaluate the given expression.
     * @param e
     * @param c
     * @return an integer that results from evaluating e.
     */
    int eval(Expr e, Critter c);
    
    /**
     * Get value of mem[memIndex] in given critter.
     * @param c
     * @param memIndex  the index of mem want to sense
     * @return  the integer result of that call to mem
     */
    int getMem(Critter c, int memIndex);
    
    /**
     * Set mem[memIndex] of critter to val
     * @param c
     * @param memIndex
     * @param val
     */
    void setMem(Critter c, int memIndex, int val);
}
