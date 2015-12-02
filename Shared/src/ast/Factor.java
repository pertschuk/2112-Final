package ast;

public abstract class Factor extends ASTNode {
	private Expr expression;
	private Factor f;
	private Sensor s;
	private boolean mem, neg = false;
	
	//Define implicit superconstructor DO NOT DELETE
	public Factor(){
	}
	
	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

}
