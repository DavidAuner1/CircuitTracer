import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Search for shortest paths between start and end points on a circuit board
 * as read from an input file using either a stack or queue as the underlying
 * search state storage structure and displaying output to the console or to
 * a GUI according to options specified via command-line arguments.
 * 
 * @author DAuner
 */
public class CircuitTracer {
	
	private Storage<TraceState> stateStore = null; 
	private ArrayList<TraceState> bestPaths;
	
	
	
	//Arr
	/** launch the program
	 * 
	 * @param args three required arguments:
	 *  first arg: -s for stack or -q for queue
	 *  second arg: -c for console output or -g for GUI output
	 *  third arg: input file name 
	 */
	public static void main(String[] args) {
		
	
		new CircuitTracer(args); //create this with args
		
	}

	/** Print instructions for running CircuitTracer from the command line. */
	private static void printUsage() {
		//TODO: print out clear usage instructions when there are problems with
		// any command line args
		// See https://en.wikipedia.org/wiki/Usage_message for format and content guidance
	System.out.println("Usage: java CircuitTracer [-s stack || -q queue] [-c console || -g GUI] [filename]");
	}
	
	/** 
	 * Set up the CircuitBoard and all other components based on command
	 * line arguments.
	 * 
	 * @param args command line arguments passed through from main()
	 */
	public CircuitTracer(String[] args) {
		//TODO: parse and validate command line args - first validation provided
		//TODO: initialize the Storage to use either a stack or queue
				//TODO: read in the CircuitBoard from the given file
				//TODO: run the search for best paths
				//TODO: output results to console or GUI, according to specified choice
//				
		
		//checks for 3 arguments only
		if (args.length != 3) {
		printUsage();
	
		return;
	}
	
	//cehcks whether stack or que 
	//stores objects of type TraceState
	if (args[0].equals("-s") || args[0].equals("-q")) {
		if (args[0].equals("-s")) {
			stateStore = new Storage<TraceState>(Storage.DataStructure.stack);
		}
		if (args[0].equals("-q")) {
			
			stateStore = new Storage<TraceState>(Storage.DataStructure.queue);

		}

	}
	else {
		printUsage();
	
		return;
	}

	String inputFileName = args[2];

	try {
		
		
		CircuitBoard board;
		
		board = new CircuitBoard(inputFileName);
		
//stores objects of type TraceState
		bestPaths = new ArrayList<TraceState>();
		
		
	
		TraceState path;
		int startingPointX = board.getStartingPoint().x;
		int startingPointY = board.getStartingPoint().y;
		//add a new initial TraceState object (a path with one trace) to 
		//stateStore for each open position adjacent to the starting component
		//down
		if (board.isOpen(startingPointX+1, startingPointY)) {
			path = new TraceState(board, startingPointX+1, startingPointY);
			//System.out.print(board.charAt(startingPointX, startingPointY));
			stateStore.store(path);
			//right
		}if (board.isOpen(startingPointX, startingPointY+1)) {
			path = new TraceState(board, startingPointX, startingPointY+1);
			
	//	System.out.print(board.charAt(startingPointX, startingPointY));
			stateStore.store(path);
		}
		
		
		//left
		if (board.isOpen(startingPointX, startingPointY-1)) {
			path = new TraceState(board, startingPointX, startingPointY-1);
			//System.out.print(board.charAt(startingPointX, startingPointY));
			stateStore.store(path);
			//up
		}if (board.isOpen(startingPointX-1, startingPointY)) {
			path = new TraceState(board, startingPointX-1, startingPointY);
			//System.out.print(board.charAt(startingPointX, startingPointY));
			stateStore.store(path);
		}
		
		while (!stateStore.isEmpty()) {
			//retrieve the next TraceState object from stateStore
			TraceState nextTrace = stateStore.retrieve();
			if (nextTrace.isComplete()) {

				if (bestPaths.isEmpty() || nextTrace.pathLength() == bestPaths.get(0).pathLength() ) {//bestPaths should all have the same length, thus just use first one
					bestPaths.add(nextTrace);
				}
				else if (nextTrace.pathLength() < bestPaths.get(0).pathLength()){
					bestPaths.clear();
					bestPaths.add(nextTrace);
				}
			}
			else { 
				//generate all valid next TraceState objects from the current TraceState and add them to stateStore
				//down
				if (board.isOpen(nextTrace.getRow()+1, nextTrace.getCol()) ) {
					if (nextTrace.isOpen(nextTrace.getRow()+1, nextTrace.getCol())) {
						TraceState newTrace = new TraceState(nextTrace, nextTrace.getRow()+1, nextTrace.getCol());
						stateStore.store(newTrace);
					}
				}
				//up
				if (board.isOpen(nextTrace.getRow()-1, nextTrace.getCol()) ) {
					if (nextTrace.isOpen(nextTrace.getRow()-1, nextTrace.getCol())) {
						TraceState newTrace  = new TraceState(nextTrace, nextTrace.getRow()-1, nextTrace.getCol());
						stateStore.store(newTrace);
					}
				}
				
				
				//right
				if (board.isOpen(nextTrace.getRow(), nextTrace.getCol()+1) ) {
					if (nextTrace.isOpen(nextTrace.getRow(), nextTrace.getCol()+1)) {
						TraceState newTrace  = new TraceState(nextTrace, nextTrace.getRow(), nextTrace.getCol()+1);
						stateStore.store(newTrace);
					}
				}
				//left
				if (board.isOpen(nextTrace.getRow(), nextTrace.getCol()-1) ) {
					if (nextTrace.isOpen(nextTrace.getRow(), nextTrace.getCol()-1)) {
						TraceState newTrace  = new TraceState(nextTrace, nextTrace.getRow(), nextTrace.getCol()-1);
						stateStore.store(newTrace);
					}
				}

			}

		}
			
		
	} catch (FileNotFoundException e) {
		System.out.println(e.toString());
	//System.exit(0);
	} catch (InvalidFileFormatException e) {
		//System.out.println("Hello");
		System.out.println(e.toString());
		return;
		//System.exit(0);
		
	} 
	//end searchPaths
	

	//output of board whether console or gui
	if (args[1].equals("-c") || args[1].equals("-g")) {
		if (args[1].equals("-c")) { 
			
			for(TraceState b : bestPaths) {
				System.out.println(b.getBoard().toString());
			}
			
		}
		else if (args[1].equals("-g")) { 
			System.out.println("Not Implemented!!");
			
		}
	
		
	}
	else { // not c or g
		printUsage();
		return;	
	}

	}
		
	
		
	
}	// class CircuitTracer

 
