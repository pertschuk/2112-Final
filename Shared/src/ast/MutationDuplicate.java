package ast;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

/**
 * Class that handles mutations in which a rule or update is duplicated
 * @author Jack Pertschuk
 *
 */
public class MutationDuplicate implements Mutation {
	
	Random r = new Random();
		
	/**
	 * Performs mutation outlined by class spec. 
	 * 
	 * @return the mutated AST
	 * @throws UnsupportedOperationException if the given mutation is not applicable to the node
	 */
	public Node mutate(Node n, Program p) throws UnsupportedOperationException {
		
		if (!(n instanceof ProgramImpl | n instanceof Command)){
			throw new UnsupportedOperationException();
		}
		int a = n.children().length;
		int b = r.nextInt(a);
		
		
		//duplicates a random child if its a program
		if (n instanceof ProgramImpl){
			((ProgramImpl) n).addNode((Rule) n.children()[b]);
		}
		//case that it is a command
		else {
			if (n.children().length >1){
				((Command) n).addUpdate((Update) n.children()[r.nextInt(n.children().length - 1)]);
			}
			else if (n.children()[0] instanceof Update){
				((Command) n).addUpdate((Update) n.children()[0]);
			}
		}
		return n;
	}

	@Override
	public boolean equals(Mutation m) {
		return this.getClass().equals(m.getClass());
	}

	@Override
	public Node getRandomValid(Program p) {	
		String[] valid = new String[]{"ast.ProgramImpl", "ast.Command"};
		return MutationShared.getRandomValid(p, valid);
		
	}
}
