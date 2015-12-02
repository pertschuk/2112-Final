package ast;

import java.util.LinkedList;
import java.util.Random;
/**
 * A data structure representing a critter program.
 *
 */
public class ProgramImpl extends ASTNode implements Program {
	
	private LinkedList<Rule> rules;
	Random r = new Random();
	
	public ProgramImpl(){
		rules = new LinkedList<Rule>();
	}
	
	/**
	 * Adds a Rule to the program
	 * @param r
	 */
	public void addNode(Rule r){
		children.add(r);
		rules.add(r);
	}
	
	public void replaceChild(Node n, Node old){
		this.rules.remove(old);
		this.rules.add((Rule) n);
		super.replaceChild(old, n);
	}
	
    @Override
    public Program mutate() {
        int i = r.nextInt(5);
        Mutation m = null;
        Node n;
        switch(i){
        case 0:
        	m = MutationFactory.getDuplicate();
        case 1:
        	m = MutationFactory.getInsert();
        case 2:
        	m = MutationFactory.getTransform();
        case 3:
        	m = MutationFactory.getReplace();
        case 4:
        	m = MutationFactory.getSwap();
        }
        n = m.getRandomValid(this);
        m.mutate(n, this);
        return this;
    }

    @Override
    public Program mutate(int index, Mutation m) {
    	Node n = this.nodeAt(index);
        Node done = m.mutate(n, this);
        if (done ==null){
        	return null;
        }
        else {
        	return this;
        }
    }

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		for (Rule r : rules)
			sb.append(r.prettyPrint(new StringBuilder())); 
		return sb;
	}


}
