package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

import org.junit.Test;

import parse.*;
import ast.*;


public class MutationTest {
	
	@Test
    public void DuplicateTests() throws FileNotFoundException {
    	File f = new File("remove_example.txt");
    	assertTrue("check if f can be read", f.canRead());
		BufferedReader br = new BufferedReader(new FileReader(f));
    	Parser pa = ParserFactory.getParser();
    	Program p = pa.parse(br);
    	System.out.println(p);
    	assertTrue("p is a program", p != null);
    	System.out.println("---------------STARTING DUPLICATE TESTS----------------");
    	//assertTrue("Check parser", p.size() == 12);
    	//Perform Mutations
    	Mutation duplicate = MutationFactory.getDuplicate();
    	
    	//Mutate the program
    	Node mutated = duplicate.mutate(p, p);
    	//System.out.print(mutated);
    	Node node = duplicate.getRandomValid(p);
    	System.out.println(node.getClass());
    	duplicate.mutate(node, p);
    	System.out.println(p);
    	
    	//prints out tree
    	
    	
    }
	@Test
	public void InsertTests() throws FileNotFoundException {
		System.out.println("---------------STARTING INSERT TESTS----------------");
		//Mutation insert = MutationFactory.getInsert();
		File f = new File("remove_example.txt");
    	assertTrue("check if f can be read", f.canRead());
		BufferedReader br = new BufferedReader(new FileReader(f));
    	Parser pa = ParserFactory.getParser();
    	Program p = pa.parse(br);
    	
    	for (int i = 0; i < p.size(); i++){
    		//System.out.println(p.nodeAt(i).getClass());
    	} 
    	
		assertTrue("GetParent broken", MutationShared.findParent(p.nodeAt(2), p).getClass().toString().equals("class ast.Rule")); //should be rule: WORKS
		
		Mutation insert = MutationFactory.getInsert();
		
		insert.mutate(insert.getRandomValid(p), p);
		System.out.println(p);
		
		//insert.mutate(p.nodeAt(0), p); // Should throw expression, it does
		//System.out.println(p);
		
		//System.out.println(p); 
	}
	@Test
	public void ReplaceTests() throws FileNotFoundException {
		System.out.println("---------------STARTING REPLACE TESTS----------------");
		
		Mutation replace = MutationFactory.getReplace();

		File f = new File("remove_example.txt");
    	assertTrue("check if f can be read", f.canRead());
		BufferedReader br = new BufferedReader(new FileReader(f));
    	Parser pa = ParserFactory.getParser();
    	Program p = pa.parse(br);
    	assertTrue("p is a program", p != null);
    	assertTrue("Check program isn't empty", p.size() > 1);
    	//Perform Mutations
    	replace.mutate(replace.getRandomValid(p), p); //replaces 4 with 3
    	System.out.print(p);
		
	}
	@Test
	public void RemoveTests() throws FileNotFoundException {
		//System.out.println("---------------STARTING REMOVE TESTS----------------");
		File f = new File("remove_example.txt");
    	assertTrue("check if f can be read", f.canRead());
		BufferedReader br = new BufferedReader(new FileReader(f));
    	Parser pa = ParserFactory.getParser();
    	Program p = pa.parse(br);
    	assertTrue("p is a program", p != null);
    	assertTrue("Check program isn't empty", p.size() > 1);
    	//Perform Mutations
    	//Mutation r = MutationFactory.getRemove();
    	//Node removed = r.mutate(r.getRandomValid(p), p);
    	//System.out.println(p);
    	//assertTrue("Check mutation remove", "{ mem[3] * 400 < 7 } --> grow;".equals(p.toString()));
    
    	
	}
	@Test
	public void SwapTests() throws FileNotFoundException {
		System.out.println("---------------STARTING SWAP TESTS----------------");
		File f = new File("remove_example.txt");
    	assertTrue("check if f can be read", f.canRead());
		BufferedReader br = new BufferedReader(new FileReader(f));
    	Parser pa = ParserFactory.getParser();
    	Program p = pa.parse(br);
    	assertTrue("p is a program", p != null);
    	assertTrue("Check program isn't empty", p.size() > 1);
    	//Perform Mutations
    	Mutation s = MutationFactory.getSwap();
    	Node n = s.getRandomValid(p);
    	Node swapped = s.mutate(n, p);
    	
    	System.out.print(p);
	}
	@Test
	public void TransformTests() throws FileNotFoundException {
		System.out.println("---------------STARTING TRANSFORM TESTS----------------");
		File f = new File("remove_example.txt");
    	assertTrue("check if f can be read", f.canRead());
		BufferedReader br = new BufferedReader(new FileReader(f));
    	Parser pa = ParserFactory.getParser();
    	Program p = pa.parse(br);
    	assertTrue("p is a program", p != null);
    	assertTrue("Check program isn't empty", p.size() > 1);
    	Mutation transform = MutationFactory.getTransform();
    	Node transformed = transform.mutate(transform.getRandomValid(p), p);
    	System.out.println(p);

	}
}
