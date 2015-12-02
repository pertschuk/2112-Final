package ast;

/**
 * A representation of a critter rule.
 */
public class Rule extends ASTNode implements Node {
	
	private Node condition;
	private Node command;
	
	/**
	 * Creates a rule with 2 children
	 * @param condition
	 * @param command
	 */
	public Rule(Node condition, Node command){
		this.condition = condition;
		this.command = command;
		//add the children to rules in the AST
		children.add(condition);
		children.add(command);
	}
	
	public void replaceChild(Node n, Node old){
		if (this.condition == old){
			this.condition =  n;
		}
		else if (this.command == old){
			this.command = n;
		}
		super.replaceChild(old, n);
	}
	
    @Override
    public StringBuilder prettyPrint(StringBuilder sb) {
        sb.append(condition.prettyPrint(new StringBuilder()));
        sb.append(" --> ");
        sb.append(command.prettyPrint(new StringBuilder()));
        sb.append(";\n");
        return sb;
    }
    
}
