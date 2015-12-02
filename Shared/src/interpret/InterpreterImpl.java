package interpret;

import java.util.Arrays;
import java.util.Random;

//import parse.TokenType;
import ast.*;
import ast.Number;
import simulator.*;

public class InterpreterImpl implements Interpreter {
	private final int MAX_PASSES = 999;
	private Simulator sim;
	private Random r = new Random();
	
	public InterpreterImpl(Simulator sim) {
		this.sim = sim;
	}
	
	@Override
	public Outcome interpret(Program p, Critter c) {
		int pass = 1;
		setMem(c, 5, pass); //set mem[5] = 1
		Node[] rules = p.children();
		while (pass < MAX_PASSES) {
			for (Node r : rules) {
				c.lastrule = (Rule) r;
				boolean temp = eval((Condition)r.children()[0],c);
				
				//System.out.println(temp);
				
				if (temp) {
					//array of updates and actions in command
					Command cmd = (Command) r.children()[1];
					Node[] updateAction = cmd.toArray();
					//System.out.println(Arrays.toString(updateAction));
					for (int i = 0; i < updateAction.length; i++) {
						//if last element in array is action, return
						if (updateAction[i] instanceof Action) {
							//System.out.println("entered action");
							Action a = (Action) updateAction[i];
							if (a.getExpr() != null) 
								return new OutcomeImpl(a.getAction(), eval(a.getExpr(),c));
							return new OutcomeImpl(a.getAction());
						}
						else if (updateAction[i] instanceof Update)
							update((Update)updateAction[i],c);							
					}
				}
			}
			pass++;
			setMem(c, 5, pass);
		}
		return null;
	}

	/** all eval methods share a specification. Only differences between eval methods
	 *  are the type of node they are evaluating. The return type depends on the
	 *  type of node the method evaluates
	 */
	@Override
	public boolean eval(Condition cd, Critter c) {
		Node[] conjs = cd.children();
		for (Node n : conjs) {
			if (n instanceof Condition) {
				boolean res = eval((Condition) n, c);
				if (res) return true;
			} else {
				if (eval((Conjunction) n, c)) return true;
			}
		}
		return false;
	}
	
	public boolean eval(Conjunction cj, Critter c) {
		if (cj == null) { return false; }		
		Node[] rels = cj.children();
		for (Node rel : rels) {
			if (rel instanceof Conjunction) {
				boolean res = eval((Conjunction) rel, c);
				if (!res) return false;
			} else {
				if (!eval((Relation) rel, c)) return false;
			}
		}
		return true;
	}
	
	public boolean eval(Relation r, Critter c) {
		if (r == null) { return false; }
		Node[] children = r.children();
		//if child is a condition
		if (children.length == 1)
			return eval((Condition) children[0], c);
		//else children of form left expr, right expr, rel op
		int lExpr = eval((Expr)children[0],c);
		int rExpr = eval((Expr)children[1],c);
		String operator = children[2].toString();
		//System.out.println(lExpr + " " + operator+ " " + rExpr);
		switch(operator) {
		case "<": 
			return lExpr < rExpr;
		case "<=":
			return lExpr <= rExpr;
		case "=": 
			return lExpr == rExpr;
		case "!=":
			return lExpr != rExpr;
		case ">=":
			return lExpr >= rExpr;
		default:
			return lExpr > rExpr;
		}
	}
	
	@Override
	public int eval(Expr e, Critter c) {
		Node[] children = e.children();
		//case where expr is one term
		if (children.length == 1) {
			return eval((Term) children[0], c);
		}
		//otherwise the expression is binary
		int left = eval((Expr) children[0], c);
		int right = eval((Expr) children[1], c);
		String addOp = children[2].toString();
		if (addOp.equals("PLUS"))
			return left + right;
		return left - right;
	}
	
	public int eval(Term t, Critter c) {
		Node[] children = t.children();
		//case where term is one factor
		if (children.length == 1) {
			return eval((Factor) children[0], c);
		}
		//otherwise the expression is binary
		int left = eval((Term) children[0], c);
		int right = eval((Term) children[1], c);
		String mulOp = children[2].toString();
		//System.out.println(mulOp);
		if (mulOp.equals("MUL")){
			return left * right;
			}
		else if (mulOp.equals("DIV"))
			return left/right;
		return left % right;
	}
	
	public int eval(Factor f, Critter c) {
		Node[] children = f.children();
		//case Factor --> <number>
    	if (f instanceof NumFactor) {
    		Number n = (Number) children[0];
    		return n.getValue();
    	}
    	//case Factor --> mem[ expr ]
    	else if (f instanceof MemFactor) {
    		Expr e = (Expr) children[0];
    		return getMem(c,eval(e,c));
    	}
    	//case Factor --> ( expr )
    	else if (f instanceof SingleFactor) {
    		Expr e = (Expr) children[0];
    		return eval(e,c);
    	}
    	// case Factor --> - factor
    	else if (f instanceof NegFactor) {
    		Factor fac = (Factor) children[0];
    		return -1*eval(fac,c);
    	}
    	// case Factor --> Sensor
    	else {
    		Sensor s = (Sensor) children[0];
    		int res = eval(s,c);
    		System.out.println(res);
    		return res;
    	}
	}
	
	public int eval(Sensor s, Critter c) {
		String type = s.getType();
		Expr e = s.getExpr(); // might be null
		if (type.equals("nearby")) {
			int dir = eval(e,c);
			//System.out.println(sim.senseNearby(dir, c));
			return sim.senseNearby(dir, c);
		} else if (type.equals("ahead")) {
			int dist = eval(e,c);
			//System.out.println(sim.senseAhead(dist, c));
			return sim.senseAhead(dist, c);
		} else if (type.equals("random")) {
			int range = eval(e,c);
			if (range < 2) return 0;
			else {
				return r.nextInt(range);
			}
		} else { //case smell
			return sim.smell(c);
		}
	}
	
	/**
	 * Updates the critter according to parameter u
	 * @param u  update to be performed
	 * @param c  critter to be updated. will have side effect
	 */
	public void update(Update u, Critter c) {
		Node[] children = u.children();
		int e1 = eval((Expr)children[0],c);
		int e2 = eval((Expr)children[1],c);
		//mem[e1] := e2
		setMem(c, e1,e2);
	}
	@Override
	public int getMem(Critter c, int memIndex) {
		return c.getMem(memIndex);
	}

	@Override
	public void setMem(Critter c, int memIndex, int val) {
		c.setMem(memIndex, val);		
	}
}