package parse;

import java.io.Reader;

import ast.*;
import exceptions.SyntaxError;

public class ParserImpl implements Parser {

    @Override
    public Program parse(Reader r) {
        Tokenizer t = new Tokenizer(r);
        Program p = null;
		try {
			p = parseProgram(t);
		} catch (SyntaxError e) {
			e.printStackTrace();
			System.out.println("File could not be read");
		}
        return p;
    }

    /** Parses a program from the stream of tokens provided by the Tokenizer,
     *  consuming tokens representing the program. All following methods with
     *  a name "parseX" have the same spec except that they parse syntactic form
     *  X.
     *  @return the created AST
     *  @throws SyntaxError if there the input tokens have invalid syntax
     */
    public static ProgramImpl parseProgram(Tokenizer t) throws SyntaxError {
        Rule r = parseRule(t);
        ProgramImpl p = new ProgramImpl();
        p.addNode(r);
        //add every rule to the ProgramImpl
        while (t.hasNext()) {
        	r = parseRule(t);
        	p.addNode(r);
        }
        return p;
    }

    public static Rule parseRule(Tokenizer t) throws SyntaxError {
        Condition c = parseCondition(t);
        consume(t, TokenType.ARR); // handles '-->'
        //t.next(); 
        Command cd = parseCommand(t);
        // ';' handled in parseCommand
        return new Rule(c,cd);
    }

    public static Condition parseCondition(Tokenizer t) throws SyntaxError {
        Condition c = new ConditionImpl(parseConjunction(t)); //change so Condition constructor takes a conjunction
        Token toke = t.peek();
        Condition bc = null; //to be set to a binary condition if enters while loop
        while (toke.getType().equals(TokenType.OR)) {
        	consume(t,TokenType.OR);
        	Condition nc = new ConditionImpl(parseConjunction(t));
        	bc = new BinaryCondition(c, BinaryCondition.Operator.OR , nc);//or == equivalent op
        	toke = t.peek();
        	c = bc;
        }
        if (bc == null) return c;
        return bc;
    }    

    public static Conjunction parseConjunction(Tokenizer t) throws SyntaxError {
    	 Conjunction c = new Conjunction(parseRelation(t));
         Token toke = t.peek();
         //Condition bc = c; //to be set to a binary condition if enters while loop
         while (toke.getType().equals(TokenType.AND)) {
        	 consume(t,TokenType.AND);
         	c = new BinaryConjunction(c,BinaryCondition.Operator.AND, new Conjunction(parseRelation(t))); //and == equivalent op
         	toke = t.peek();
         }
         return c;
    }
    
    public static Relation parseRelation(Tokenizer t) throws SyntaxError {
    	Relation r;
    	Token toke = t.peek();
    	//case relation --> { condition }
    	if (toke.getType().equals(TokenType.LBRACE)) {
    		consume(t,TokenType.LBRACE); //gets rid of '{'
    		r = new Relation(parseCondition(t));
    		consume(t,TokenType.RBRACE);//gets rid of '}'
    	} else {
    		//will this work if expressions aren't conditions?
    		Expr e = parseExpression(t);
    		RelOp ro = new RelOp(t.next().toString());
    		r = new Relation(e, ro, parseExpression(t));
    	}
    	return r;
    }

    public static Expr parseExpression(Tokenizer t) throws SyntaxError {
        /*Expr e = new ExprImpl(parseTerm(t));
        Token toke = t.peek();
        while (toke.isAddOp()) {
        	AddOp ao = new AddOp(t.next().toString());
        	e = new BinaryExpr(e, ao, new ExprImpl(parseTerm(t)));
        	toke = t.peek();
        }
        return e;*/
    	Expr expr = new ExprImpl(parseTerm(t));
        while (t.hasNext()) {
            Token toke = t.peek();
            if (toke.isAddOp()) {
                t.next();
                TokenType tokenType = toke.getType();
                expr =
                        new BinaryExpr(expr,
                                       new AddOp(tokenType.toString()),
                                       new ExprImpl(parseTerm(t)));
            }
            else break;
        }

        return expr;
    }

    public static Term parseTerm(Tokenizer t) throws SyntaxError {
    	/*Term term = new Term(parseFactor(t));
        Token toke = t.peek();
        if (toke.isMulOp()) {
        	MulOp mo = new MulOp(t.next().toString());
        	//term = new BinaryTerm(term, mo, new Term(parseFactor(t)));
        	term = new BinaryTerm(term, mo, parseTerm(t));
        	//toke = t.peek();
        }
        return term;*/
    	Term term = new Term(parseFactor(t));
        while (t.hasNext()) {
            Token token = t.peek();
            if (token.isMulOp()) {
                t.next();
                TokenType tokenType = token.getType();
                term =
                        new BinaryTerm(term,
                                       new MulOp(tokenType.toString()),
                                       new Term(parseFactor(t)));
            }
            else break;
        }
        return term;
    }

    public static Factor parseFactor(Tokenizer t) throws SyntaxError {
    	Token toke = t.peek();
    	Factor f;
    	//case Factor --> <number>
    	if (toke.isNum()) {
    		//extract value of next NumToken to make factor
    		t.next();
    		int num = toke.toNumToken().getValue();
    		f = new NumFactor(num);
    	}
    	//case Factor --> mem[ expr ]
    	else if (toke.getType().equals(TokenType.MEM)) {
    		consume(t,TokenType.MEM);
    		consume(t,TokenType.LBRACKET);
    		Expr e = parseExpression(t);
    		consume(t,TokenType.RBRACKET);
    		f = new MemFactor(e);
    	} else if (toke.isMemSugar()) { //should this use consume?
    		int num = t.next().intMemSugarEquivalent();
    		//create expression from number
    		Expr e = new ExprImpl(new Term(new NumFactor(num)));
    		f = new MemFactor(e);
    	}
    	//case Factor --> ( expr )
    	else if (toke.getType().equals(TokenType.LPAREN)) {
    		consume(t,TokenType.LPAREN);
    		Expr e = parseExpression(t);
    		consume(t,TokenType.RPAREN);
    		f = new SingleFactor(e);
    	}
    	// case Factor --> - factor
    	else if (toke.getType().equals(TokenType.MINUS)) {
    		consume(t,TokenType.MINUS);
    		f = new NegFactor(parseFactor(t));
    	}
    	// case Factor --> Sensor
    	else {
    		f = new SensorFactor(parseSensor(t));
    	}
    	return f;
    }

    public static Sensor parseSensor(Tokenizer t) throws SyntaxError{
    	//if (t.peek().isSensor()) {
    		String type = t.next().toString();
    		if (t.peek().getType().equals(TokenType.LBRACKET)) {
    			consume(t,TokenType.LBRACKET);
    			Expr e = parseExpression(t);
    			consume(t,TokenType.RBRACKET);
    			return new Sensor(type,e);
    		} else {
    			return new Sensor(type);
    		}
    	//}
    	//throw new SyntaxError();
    }
    
    public static Command parseCommand(Tokenizer t) throws SyntaxError {
    	Token toke = t.peek();
    	Command c = new Command();
    	while (!toke.getType().equals(TokenType.SEMICOLON)) {
    		if(toke.getType().equals(TokenType.MEM)) {
    			consume(t,TokenType.MEM);
    			consume(t,TokenType.LBRACKET);
    			Expr e1 = parseExpression(t);
    			consume(t,TokenType.RBRACKET);
    			consume(t,TokenType.ASSIGN);
    			Expr e2 = parseExpression(t);
    			Update u = new Update(e1, e2);
    			c.addUpdate(u);
    		} else if (toke.isMemSugar()) {
    			int num = t.next().intMemSugarEquivalent();
        		//create expression from number
        		Expr e1 = new ExprImpl(new Term(new NumFactor(num)));
        		consume(t,TokenType.ASSIGN);
        		//t.next();
        		Expr e2 = parseExpression(t);
        		Update u = new Update(e1,e2);
        		c.addUpdate(u);
    		} 
    		//else is an action
    		else {
    			String action = t.next().toString();
    			Action a;
    			if (t.peek().toString().equals("[")) {
    				consume(t,TokenType.LBRACKET);
    				Expr e = parseExpression(t);
    				consume(t,TokenType.RBRACKET);
    				a = new Action(action, e);
    			} else {
    				a = new Action(action);
    			}
    			c.addAction(a);
    		}
    		toke = t.peek();
    	}
    	consume(t,TokenType.SEMICOLON);
    	return c;
    }
    
    /**
     * Consumes a token of the expected type.
     * @throws SyntaxError if the wrong kind of token is encountered.
     */
    public static void consume(Tokenizer t, TokenType tt) throws SyntaxError {
        Token toke = t.next();
    	if (toke.getType() == tt) { }
        else {
        	throw new SyntaxError();
        }
    }
}
