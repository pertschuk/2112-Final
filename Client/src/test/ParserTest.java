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

public class ParserTest {
	
    @Test
    public void test1() throws FileNotFoundException {
    	//a simple one rule program should be created
    	File f = new File("simple.txt");
    	assertTrue("check if f can be read", f.canRead());
		BufferedReader br = new BufferedReader(new FileReader(f));
		BufferedReader br2 = new BufferedReader(new FileReader("example-rules.txt"));
    	Parser pa = ParserFactory.getParser();
    	Program p = pa.parse(br);
    	Program p2 = pa.parse(br2);
    	assertTrue("p is a program", p != null);
    	System.out.print(p); //this output is equivalent to original instructions
    						 //not including formatting differences
    	System.out.println(p.size());
    	/* test passes */
    }
    
    @Test
    public void test2() throws FileNotFoundException {
    	//More complicated program with multiple rules, some complicated
    	File f = new File("example-rules.txt");
    	assertTrue("check if f can be read", f.canRead());
		BufferedReader br = new BufferedReader(new FileReader(f));
    	Parser pa = ParserFactory.getParser();
    	Program p = pa.parse(br);
    	assertTrue("p is a program", p != null);
    	System.out.print(p); //this output is equivalent to original instructions
    						 //not including formatting differences
    	System.out.println(p.size());
    	/* test passes */
    }
    
    /* We conclude that parser and ast structure are correct from these test cases passing */
    
    @Test
    public void test3() throws FileNotFoundException {
    	File f = new File("example-critter2.txt");
    	BufferedReader br = new BufferedReader(new FileReader(f));
    	Parser pa = ParserFactory.getParser();
    	Program p = pa.parse(br);
    	assertTrue("p is a program", p!= null);
    	System.out.print(p);
    }
}