package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import parse.Parser;
import parse.ParserFactory;
import ast.Program;
import simulator.*;
import interpret.*;

public class A5Test {
	@Test
	public void testSimulator() throws IOException {
		/** test with default constructor */
		Simulator s = new Simulator();
		assertTrue("Simulator s was initialized", s != null);
		String info1 = s.getInfo();
		Critter c = Critter.loadCritter("example-critter.txt", 0, s, 2, 2);
		s.addCritter(c);
		String info2 = s.getInfo();
		assertTrue("Call to addCritter changed simulator state", !info1.equals(info2));
		s.step();
		String info3 = s.getInfo();
		assertTrue("Call to step changed simulator state", !info2.equals(info3));
		int[] coords = s.emptyCoords();
		//test empty coords and get hex multiple times
		for (int i = 0; i < 5; i++) {
			assertTrue("Coords returned by empty coords represent empty value", s.getHex(coords[0],coords[1]) == null);
			coords = s.emptyCoords();
		}
		/** test with parametrized constructor */
		Simulator s2 = new Simulator(new File("world.txt"));
		assertTrue("Simulator s2 was initialized", s2 != null);
		String s2info1 = s2.getInfo();
		System.out.println(s2info1);
		for (int i = 0; i < 40; i ++){
			s2.step(); 
			}
		String s2info2 = s2.getInfo();
		System.out.println(s2info2);
		
		//System.out.println(s2.getHex(4, 3));

		assertTrue("Call to step changed simulator state", !s2info2.equals(s2info1));
		//possibly test getHex with known value at certain hex value
	}
	
	@Test
	public void testCritter() throws IOException {
		File f = new File("example-rules.txt");
		BufferedReader br = new BufferedReader(new FileReader(f));
    	Parser pa = ParserFactory.getParser();
    	Program p = pa.parse(br);
		int[] mem1 = {8,1,1,1,100,1,1,1};
		int[] mem2 = {8,50,50,1,100,1,1,1};
		Critter c1 = new Critter(0, 0, p, mem1,"test1",0);
		Critter c2 = new Critter(1, 1, p, mem2, "test2",1);
		Critter c3 = new Critter(0, 0, p, mem1, "test1",0);//this critter should be same as c1
		assertTrue("Critter was initialized", c1 != null);
		assertTrue("Critter was initialized", c2 != null);
		assertTrue("Critters aren't equal", !c1.equals(c2));
		assertTrue("Critter c2.getMem works", c2.getMem(2) == 50);
		c2.setMem(2, 60);
		assertTrue("Critter c2.setMem works", c2.getMem(2) != 50);
		Simulator s = new Simulator();
		s.addCritter(c1);
		s.addCritter(c2);
		s.addCritter(c3);
		s.step();//method that executes rules and updates critter
					//this might require putting all critters into a simulation depending on impl
		assertTrue("c1 was updated", !c1.equals(c3));//c1 originally = c3, so if c1 updated then c1 != c3
		Critter c4 = Critter.loadCritter("example-critter.txt", 0, s, 2, 2);
		assertTrue("Critter was initialized", c4 != null);
		c4.getInfo();
		s.step();
		c4.getInfo();
		
	}

	@Test
	public void testFoodAndRock() {
		Rock r = new Rock(2,3);
		assertTrue("Test rock getRow", r.getRow() == 2);
		assertTrue("Test rock getCol", r.getCol() == 3);
		
		Food f = new Food(1,4,50);
		assertTrue("Test food getRow", f.getRow()==4);
		assertTrue("Test food getCol", f.getCol()==1);
		assertTrue("Test food getFood", f.getFood()==50);
	}
	
	@Test
	public void testInterpreter() throws IOException {
		/** first test interpreter on own, then with critter */
		File f = new File("example-rules.txt");
		BufferedReader br = new BufferedReader(new FileReader(f));
    	Parser pa = ParserFactory.getParser();
    	Program p = pa.parse(br);
		int[] mem1 = {8,1,1,1,100,1,1,1};
		Critter c = new Critter(0, 0, p, mem1,"test1",0);
		Simulator s = new Simulator(new File("world.txt"));
		Critter c2 = Critter.loadCritter("example-critter.txt", 0, s, 2, 2);
		s.addCritter(c);
		s.addCritter(c2);
		Interpreter i = new InterpreterImpl(s);
		//run tests on i
		Outcome o = i.interpret(p,c);
		assertTrue("Outcome isn't null", o != null);
		System.out.println("Type: " + o.getType() + " Value: " + o.getValue());
	}
	
	@Test
	public void testInterpreter2() throws FileNotFoundException {
		File f = new File("simplerules.txt"); //always gives outcome wait
		BufferedReader br = new BufferedReader(new FileReader(f));
		Parser pa = ParserFactory.getParser();
    	Program p = pa.parse(br);
		int[] mem1 = {8,1,1,1,100,1,1,1};
		Critter c = new Critter(3,4,p,mem1,"test2",0);
		Simulator s = new Simulator();
		s.addCritter(c);
		Interpreter i = new InterpreterImpl(s);
		Outcome o2 = i.interpret(p, c);
		//assertTrue("Outcome o2 is wait", o2.getType().equals("wait"));
		//o2 = i.interpret(p, c);
		//assertTrue("Outcome o2 is wait after interpret 2", o2.getType().equals("wait"));
	}
	
	@Test
	public void testOutcome() {
		/** 
		 * first directly create outcome objects to test, then use with interpreter
		 * possibly using critter object as we did in testInterpreter
		 */
		Outcome o1 = new OutcomeImpl("wait");
		Outcome o2 = new OutcomeImpl("tag", 0);
		assertTrue("o1 was created and != null", o1 != null);
		assertTrue("o2 was created and != null", o2 != null);
		assertFalse("o1 has no significant value", o1.hasValue());
		assertTrue("o2 has a significant value", o2.hasValue());
		assertTrue("o1 type is wait", o1.getType().equals("wait"));
		assertTrue("o1 value is -1", o1.getValue() == -1);
		System.out.println(o2.getType());
		assertTrue("o2 type is tag", o2.getType().equals("tag"));
		assertTrue("o2 value is 0", o2.getValue() == 0);
	}
}
