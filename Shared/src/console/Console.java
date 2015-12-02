package console;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import simulator.*;

/** The console user interface for Assignment 5. */
public class Console {
    private Scanner scan;
    public boolean done;
    Random random = new Random();

    //TODO world representation...
    
    private Simulator s; //representation of the world

    public static void main(String[] args) throws IOException {
        Console console = new Console();

        while (!console.done) {
            System.out.print("Enter a command or \"help\" for a list of commands.\n> ");
            console.handleCommand();
        }
    }

    /**
     * Processes a single console command provided by the user.
     * @throws IOException 
     */
    void handleCommand() throws IOException {
        String command = scan.next();
        switch (command) {
        case "new": {
            newWorld();
            break;
        }
        case "load": {
            String filename = scan.next();
            loadWorld(filename);
            break;
        }
        case "critters": {
            String filename = scan.next();
            int n = scan.nextInt();
            try {
				loadCritters(filename, n);
			} catch (FileNotFoundException e) {
				System.out.println("Could not load critter file");
				//e.printStackTrace();
			}
            break;
        }
        case "step": {
            int n = scan.nextInt();
            advanceTime(n);
            break;
        }
        case "info": {
            worldInfo();
            break;
        }
        case "hex": {
            int c = scan.nextInt();
            int r = scan.nextInt();
            hexInfo(c, r);
            break;
        }
        case "help": {
            printHelp();
            break;
        }
        case "exit": {
            done = true;
            break;
        }
        default:
            System.out.println(command + " is not a valid command.");
        }
    }

    /**
     * Constructs a new Console capable of reading the standard input.
     */
    public Console() {
        scan = new Scanner(System.in);
        done = false;
    }

    /**
     * Starts new random world simulation.
     */
    private void newWorld() {
        s = new Simulator();
    }

    /**
     * Starts new simulation with world specified in filename.
     * @param filename
     */
    private void loadWorld(String filename) {
    	File world = new File(filename);
    	try {
			s = new Simulator(world);
		} catch (FileNotFoundException e) {
			System.out.println("Unable to load world");
		} catch (IOException e) {
			System.out.println("Unable to load world");
			e.printStackTrace();
		}
    }

    /**
     * Loads critter definition from filename and randomly places 
     * n critters with that definition into the world.
     * @param filename
     * @param n
     * @throws IOException 
     */
    private void loadCritters(String filename, int n) throws IOException {
    	
        Critter c;
        //modify to deal with cloning issue UPDATE FIXED(HOPEFULLY)
        for (int i = 0; i < n; i++){
        	int[] coords = s.emptyCoords();
        	c = Critter.loadCritter(filename, random.nextInt(6), s, coords[0], coords[1]);
        	s.addCritter(c); 
        }
    }

    /**
     * Advances the world by n time steps.
     * @param n
     */
    private void advanceTime(int n) {
        for (int i =0; i < n; i++){
        	//steps the simulator forward
        	s.step();
        }
    }

    /**
     * Prints current time step, number of critters, and world
     * map of the simulation.
     */
    private void worldInfo() {
        //System.out.println(s.getInfo());
    	System.out.println(s.getInfo());
    }

    /**
     * Prints description of the contents of hex (c,r).
     * @param c column of hex
     * @param r row of hex
     */
    private void hexInfo(int c, int r) {
        System.out.println(s.getHex(c, r));
    }

    /**
     * Prints a list of possible commands to the standard output.
     */
    private void printHelp() {
        System.out.println("new: start a new simulation with a random world");
        System.out.println("load <world_file>: start a new simulation with "
                + "the world loaded from world_file");
        System.out.println("critters <critter_file> <n>: add n critters "
                + "defined by critter_file randomly into the world");
        System.out.println("step <n>: advance the world by n timesteps");
        System.out.println("info: print current timestep, number of critters "
                + "living, and map of world");
        System.out.println("hex <c> <r>: print contents of hex "
                + "at column c, row r");
        System.out.println("exit: exit the program");
    }
}
