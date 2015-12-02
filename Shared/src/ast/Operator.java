package ast;

public class Operator extends ASTNode {
	private String op;
	
	public Operator(String op){
		this.op = op;
	}
	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		if (op.equals("MUL")){
			sb.append("*");
			return sb;
		}
		else if (op.equals("ADD")) {
			sb.append("+");
			return sb;
		}
		else if (op.equals("SUB")) {
			sb.append("-");
			return sb;
		}
		else if (op.equals("DIV")) {
			sb.append("/");
			return sb;
		}
		else{
			sb.append(op);
			return sb;
		}
	}

}
