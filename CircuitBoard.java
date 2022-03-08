import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Represents a 2D circuit board as read from an input file.
 *  
 * @author mvail
 */
public class CircuitBoard {
	/** current contents of the board */
	private char[][] board;
	/** location of row,col for '1' */
	private Point startingPoint;
	/** location of row,col for '2' */
	private Point endingPoint;
	//increments when 1 is found
	private int startnum =0;
	//increments when 2 is found
	private int endnum =0;
	//only one start and end
	private int onlyTwo =0;
	
	
	
	//constants you may find useful
	private  int ROWS; //initialized in constructor
	private  int COLS; //initialized in constructor
	private final char OPEN = 'O'; //capital 'o'
	private final char CLOSED = 'X';
	private final char TRACE = 'T';
	private final char START = '1';
	private final char END = '2';
	private final String ALLOWED_CHARS = "OXT12";

	/** Construct a CircuitBoard from a given board input file, where the first
	 * line contains the number of rows and columns as ints and each subsequent
	 * line is one row of characters representing the contents of that position.
	 * Valid characters are as follows:
	 *  'O' an open position
	 *  'X' an occupied, unavailable position
	 *  '1' first of two components needing to be connected
	 *  '2' second of two components needing to be connected
	 *  'T' is not expected in input files - represents part of the trace
	 *   connecting components 1 and 2 in the solution
	 * 
	 * @param filename
	 * 		file containing a grid of characters
	 * @throws FileNotFoundException if Scanner cannot read the file
	 * @throws InvalidFileFormatException for any other format or content issue that prevents reading a valid input file
	 */
	public CircuitBoard(String filename) throws FileNotFoundException {
		Scanner fileScan = new Scanner(new File(filename));
		// gets file from user
		boolean valid =true;

		ROWS =0;
		COLS =0;
			try {
			// gets file from user
		File file = new File(filename);
		Scanner input = new Scanner(file);
	
		// grabs first line
	
		String row1 = input.nextLine();
		// creates a scanner of the first row to get first and second number from line
		Scanner numScan = new Scanner(row1);
		if(!numScan.hasNextInt()) {
			throw new InvalidFileFormatException("Expecting integer CircuitTrace.");
			//r Valid Input File Tests
		}
		ROWS = numScan.nextInt();
		if(!numScan.hasNextInt()) {
			throw new InvalidFileFormatException("none left");
		}
		COLS = numScan.nextInt();
		
		//creates size of board using two int values given
		board = new char[ROWS][COLS];
		Scanner scanLine = null;
		// create grid
		int collomn = 0;
	//populate board
		for (int i = 0; i < ROWS; i++) {
			collomn = 0;
			String lines = input.nextLine();
			 scanLine = new Scanner(lines);
			while (scanLine.hasNextLine()) {
				
				String characters1 = scanLine.nextLine();
				
				String characters2 = characters1.replaceAll("\\s+", "");
				
			char validChar = 0;
			//checks if length matches given
				if(characters2.length() != COLS) {
					throw new InvalidFileFormatException("Length does not match");
				}
				
				for(int x =0; x<characters2.length(); x++) {
					 validChar = characters2.charAt(x);
				
					 
					 //checks if the character matches the given values
					if(ALLOWED_CHARS.indexOf(validChar) == -1) {
						throw new InvalidFileFormatException("Not Supported Character");
					}
					//checks if there is only one start and end
					if( validChar == START || validChar == END) {
						onlyTwo++;
					}
					
					
					//checks if the rows and col are the same as the given
				if(i >= ROWS || collomn >= COLS) {
					throw new InvalidFileFormatException("Row or Col not same");
				}
				
		
//				
				//enters value
				board[i][collomn] = validChar;
				
				//starting piont and should increment only once
				if( validChar == START ) { //1
						startingPoint = new Point(i, collomn);
						//or x instad of i
						startnum++;
					}
				//ending piont and should increment only once
					if( validChar == END) { //2
						endingPoint = new Point(i, collomn);
						endnum++;
				}
				
				
			collomn++;
			}
				
			}
			}
		
		
		//checks for only one start and end
		if(startnum != 1) {
			throw new InvalidFileFormatException("only one start");
		}
		if(endnum != 1) {
			throw new InvalidFileFormatException("only one end");
		}
		
		//checks for more after board has been populated
		if(input.hasNext()) {
			throw new InvalidFileFormatException("More in list");
		}
		
		}
			catch (FileNotFoundException e) {
			valid = false;
			
			System.out.println(e.getMessage());
			System.out.println(e.toString());
	
		}
			catch(NumberFormatException e) {
				System.out.println(e.getMessage());
				System.out.println(e.toString());
				valid = false;

			
			}
			
		catch(InputMismatchException e) {
			System.out.println(e.getMessage());
			System.out.println(e.toString());
			valid = false;
		
		}
		catch(ArrayIndexOutOfBoundsException e) {
			System.out.println(e.getMessage());
			System.out.println(e.toString());
			valid = false;
		
		}
		
		
		fileScan.close();
		}
		
		
			

	
		

	
	
	/** Copy constructor - duplicates original board
	 * 
	 * @param original board to copy
	 */
	public CircuitBoard(CircuitBoard original) {
		board = original.getBoard();
		startingPoint = new Point(original.startingPoint);
		endingPoint = new Point(original.endingPoint);
		ROWS = original.numRows();
		COLS = original.numCols();
	}

	/** utility method for copy constructor
	 * @return copy of board array */
	private char[][] getBoard() {
		char[][] copy = new char[board.length][board[0].length];
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				copy[row][col] = board[row][col];
			}
		}
		return copy;
	}
	
	/** Return the char at board position x,y
	 * @param row row coordinate
	 * @param col col coordinate
	 * @return char at row, col
	 */
	public char charAt(int row, int col) {
		return board[row][col];
	}
	
	/** Return whether given board position is open
	 * @param row
	 * @param col
	 * @return true if position at (row, col) is open 
	 */
	public boolean isOpen(int row, int col) {
		if (row < 0 || row >= board.length || col < 0 || col >= board[row].length) {
			return false;
		}
		return board[row][col] == OPEN;
	}
	
	/** Set given position to be a 'T'
	 * @param row
	 * @param col
	 * @throws OccupiedPositionException if given position is not open
	 */
	public void makeTrace(int row, int col) {
		if (isOpen(row, col)) {
			board[row][col] = TRACE;
		} else {
			throw new OccupiedPositionException("row " + row + ", col " + col + "contains '" + board[row][col] + "'");
		}
	}
	
	/** @return starting Point(row,col) */
	public Point getStartingPoint() {
		return new Point(startingPoint);
	}
	
	/** @return ending Point(row,col) */
	public Point getEndingPoint() {
		return new Point(endingPoint);
	}
	
	/** @return number of rows in this CircuitBoard */
	public int numRows() {
		return ROWS;
	}
	
	/** @return number of columns in this CircuitBoard */
	public int numCols() {
		return COLS;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				str.append(board[row][col] + " ");
			}
			str.append("\n");
		}
		return str.toString();
	}
	public int onlyOne() {
		return onlyTwo;
	}
}// class CircuitBoard
