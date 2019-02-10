import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

/**
 * @author 
 * 
 */
public class ChessMainGame extends Problem<HashSet<Piece>> {

	private int movesmade;

	private ArrayList<HashSet<Piece>> moves = new ArrayList<HashSet<Piece>>();

	private static int rows;

	private static int cols;

	private HashSet<Piece> currentconfig = new HashSet<Piece>();

	private ArrayList<Piece> currentMoveToMake = new ArrayList<Piece>();

	/*
	 * Creates a ChessMainGame game model
	 * 
	 * @param int r = number of rows on the board int c = number or columns on the
	 * board HashSet<Piece> config - set of all ChessMainGame pieces in the game.
	 */
	public ChessMainGame(int r, int c, HashSet<Piece> config) {

		this.movesmade = 0;
		super.start = config;
		super.goal = 1;
		this.rows = r;
		this.cols = c;
		this.currentconfig = config;
		this.moves.add(config);
	}

	/*
	 * Returns a deepcopy of the current configuration
	 * 
	 * @param none
	 * 
	 * @return HashSet<Piece> config - set of all ChessMainGame pieces in the game.
	 */
	public HashSet<Piece> getConfig() {

		return DeepCopy(this.currentconfig);
	}

	/*
	 * Returns the number of rows on the board
	 * 
	 * @param none
	 * 
	 * @rerun int rows number of rows on the board.
	 */
	public static int getRows() {

		return rows;
	}

	/*
	 * Called when a user selects a ChessMainGame piece button on the board. Then
	 * either tries to make the move if it is a valid more or adds it to a current
	 * move stack.
	 * 
	 * @param ChessMainGame Piece c - ChessMainGame Piece user pressed
	 * HashSet<Piece> config - set of all ChessMainGame pieces in the game.
	 * 
	 * @returns boolean true if successful or false if unsuccessful.
	 */
	public boolean makeMove(Piece c, HashSet<Piece> config) {

		switch (currentMoveToMake.size()) {
		case 2:
			this.currentMoveToMake.removeAll(currentMoveToMake);

			return false;
		case 0:
			currentMoveToMake.add(c);
			return true;
		case 1:
			currentMoveToMake.add(c);
			if (checkMove(currentMoveToMake.get(0), c, config)) {
				if (makeMove(c, config)) {
					this.currentMoveToMake.removeAll(currentMoveToMake);
					return true;
				}
				return false;
			} else {
				return false;
			}

		default:
			throw new RuntimeException("Internal Error: currentMoveToMake Stack too big.");
		}

	}

	/*
	 * Checks the move if it is a valid move for the ChessMainGame piece to make
	 * 
	 * @param ChessMainGame Piece c1 - ChessMainGame piece to be moved ChessMainGame
	 * Piece c2 - ChessMainGame piece to be removed HashSet<Piece> config - set of
	 * all ChessMainGame pieces in the game.
	 * 
	 * @return boolean true if valid false if not.
	 */
	private boolean checkMove(Piece c1, Piece c2, HashSet<Piece> config) {

		ArrayList<Tuple> moves = c1.getMoves(config);
		if (moves.contains(c2.getLocation())) {
			for (Piece c : config) {
				if (c.compareTo(c1) == 0) {
					HashSet<Piece> newConfig = c.movePiece(c2, config);
					update(newConfig);

					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}

	/*
	 * Returns the number of columns on the board.
	 * 
	 * @param none.
	 * 
	 * @return int cols number of columns on the board.
	 */
	public static int getCols() {

		return cols;
	}

	/*
	 * Returns the number of moves the user has made in the game.
	 * 
	 * @param none.
	 * 
	 * @return int movesMade number of moves made by user.
	 */
	public int getMoveCount() {

		return movesmade;
	}

	/*
	 * Updates the model to reflect a change made to the board.
	 * 
	 * @param HashSet<Piece> config - set of all ChessMainGame pieces in the game.
	 */
	public void update(HashSet<Piece> config) {

		HashSet<Piece> copy = DeepCopy(config);
		this.moves.add(copy);
		this.currentconfig = config;
		start = config;
		this.movesmade++;
	}

	/*
	 * Calls reset on the board until all moves had been undone. Then resets
	 * movesMade.
	 * 
	 * @param none.
	 */
	public void reset() {

		while (undo()) {

		}
		this.movesmade = 0;
	}

	/*
	 * Undos the user's last move.
	 * 
	 * @param none.
	 * 
	 * @return boolean true if successful false if not.
	 */
	public boolean undo() {

		if (this.moves.size() == 1) {
			return false;
		} else {
			if (this.moves.remove(this.moves.size() - 1) != null) {
				this.currentconfig = this.moves.get(moves.size() - 1);
				start = this.moves.get(moves.size() - 1);
				return true;
			} else {
				return false;
			}
		}

	}

	/*
	 * Returns the size of the currentMoveToMake stack to determine user move
	 * status.
	 * 
	 * @param none.
	 * 
	 * @return int currentMoveToMake stack size.
	 */
	public int moveStatus() {

		return currentMoveToMake.size();
	}

	/*
	 * Prints the current board.
	 * 
	 * @param HashSet<Piece> config - set of all ChessMainGame pieces in the game.
	 */
	public static void printBoard(HashSet<Piece> config) {

		String board[][] = new String[getRows()][getCols()];
		for (int i = 0; i < getRows(); i++) {
			for (int j = 0; j < getCols(); j++) {
				board[i][j] = "*";
			}
		}
		for (Piece c : config) {
			board[c.getLocation().x][c.getLocation().y] = c.printBoardName();
		}
		for (String[] row : board) {
			for (String piece : row) {
				System.out.print(piece);
			}
			System.out.println();
		}
	}

	/*
	 * Determines the moves needed to win the game and if solution exists makes the
	 * next move.
	 * 
	 * @param none.
	 * 
	 * @return true if there is a solution false if no solution.
	 */
	public boolean cheat() {

		Solver<HashSet<Piece>> chessSolver = new Solver<HashSet<Piece>>(this);
		ArrayList<HashSet<Piece>> steps;
		steps = chessSolver.BFS();
		if (steps != null) {
			update(steps.get(1));
			return true;
		} else {
			return false;
		}

	}

	/*
	 * Creates the board as a 2D array of Strings.
	 * 
	 * @param HashSet<Piece> config - set of all ChessMainGame pieces in the game.
	 * 
	 * @returns String[][] of the current board.
	 */
	public static String[][] getBoard(HashSet<Piece> config) {

		String board[][] = new String[getRows()][getCols()];
		for (int i = 0; i < getRows(); i++) {
			for (int j = 0; j < getCols(); j++) {
				board[i][j] = "*";
			}
		}
		for (Piece c : config) {
			board[c.getLocation().x][c.getLocation().y] = c.printBoardName();
		}
		return board;
	}

	/*
	 * Main Program Run method for the model. Takes a file in and then finds the
	 * shortest path solution for it.
	 * 
	 * @param args - intial board configuration.
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("usage: java ChessMainGame input-file");
		} else {
			String gameFile = args[0];
			BufferedReader reader;
			// redactionsFile wordFile
			try {
				reader = new BufferedReader(new FileReader(new File(gameFile)));

			} catch (FileNotFoundException e) {
				System.out.println(gameFile + " not found.");
				return;
			}

			HashSet<Piece> pieces = new HashSet<Piece>();
			try {
				String line = reader.readLine();
				String[] split = line.split("\\s+");
				line = reader.readLine();
				int rows = Integer.parseInt(split[0]);
				int cols = Integer.parseInt(split[1]);

				int row = 0;
				int id = 0;
				while (line != null) {

					split = line.split("\\s+");
					for (int col = 0; col < split.length; col++) {
						if (split[col].compareTo("B") == 0) {
							Bishop bishop = new Bishop(row, col, id);
							pieces.add(bishop);
							id++;
						}
						if (split[col].compareTo("Q") == 0) {
							Queen queen = new Queen(row, col, id);
							pieces.add(queen);
							id++;
						}
						if (split[col].compareTo("K") == 0) {
							King king = new King(row, col, id);
							pieces.add(king);
							id++;
						}
						if (split[col].compareTo("N") == 0) {
							Knight knight = new Knight(row, col, id);
							pieces.add(knight);
							id++;
						}
						if (split[col].compareTo("P") == 0) {
							Pawn pawn = new Pawn(row, col, id);
							pieces.add(pawn);
							id++;
						}
						if (split[col].compareTo("R") == 0) {
							Rook rook = new Rook(row, col, id);
							pieces.add(rook);
							id++;
						}
					}
					line = reader.readLine();
					row++;
				}

				reader.close();

				ChessMainGame game = new ChessMainGame(rows, cols, pieces);

				printBoard(pieces);
				ArrayList<HashSet<Piece>> steps;
				Solver<HashSet<Piece>> chessSolver = new Solver<HashSet<Piece>>(game);

				steps = chessSolver.BFS();
				if (steps != null) {
					for (int i = 0; i < steps.size(); i++) {
						System.out.println("Step " + i + ": ");
						printBoard(steps.get(i));
					}
				} else {
					System.out.println("No solution.");

				}
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}

		}

	}

	/*
	 * Deep copies the configuration to be used again. Creates all new Piece
	 * objects.
	 * 
	 * @param HashSet<Piece> config - set of all ChessMainGame pieces in the game.
	 * 
	 * @return HashSet<Piece> copy - set of all ChessMainGame pieces in the game.
	 */
	public static HashSet<Piece> DeepCopy(HashSet<Piece> pieces) {

		HashSet<Piece> copy = new HashSet<Piece>();
		for (Piece c : pieces) {
			if (c instanceof Bishop) {
				Bishop piece = new Bishop(c.getLocation().x, c.getLocation().y, c.getID());
				copy.add(piece);
			}
			if (c instanceof Queen) {
				Queen piece = new Queen(c.getLocation().x, c.getLocation().y, c.getID());
				copy.add(piece);
			}
			if (c instanceof King) {
				King piece = new King(c.getLocation().x, c.getLocation().y, c.getID());
				copy.add(piece);
			}
			if (c instanceof Pawn) {
				Pawn piece = new Pawn(c.getLocation().x, c.getLocation().y, c.getID());
				copy.add(piece);
			}
			if (c instanceof Knight) {
				Knight piece = new Knight(c.getLocation().x, c.getLocation().y, c.getID());
				copy.add(piece);
			}
			if (c instanceof Rook) {
				Rook piece = new Rook(c.getLocation().x, c.getLocation().y, c.getID());
				copy.add(piece);
			}
		}

		return copy;

	}

	/*
	 * Gets all possible neighbors and moves for the ChessMainGame pieces on the
	 * game board.
	 * 
	 * @param HashSet<Piece> config - set of all ChessMainGame pieces in the game.
	 * 
	 * @return ArrayList<HashSet<Piece>> of all possible board configurations.
	 */
	@Override
	public ArrayList<HashSet<Piece>> getNeighbors(HashSet<Piece> config) {

		ArrayList<HashSet<Piece>> neighbors = new ArrayList<HashSet<Piece>>();

		for (Piece c : config) {

			HashSet<Piece> clone = DeepCopy(config);

			ArrayList<HashSet<Piece>> moves = c.getNeighbors(c.getMoves(clone), clone);
			for (HashSet<Piece> adjecent : moves) {
				neighbors.add(adjecent);
			}
		}

		return neighbors;
	}

	/*
	 * Determines if the given config is the goal to the Problem.
	 * 
	 * @param HashSet<Piece> config - set of all ChessMainGame pieces in the game.
	 * 
	 * @return true or false if goal configuration.
	 */
	@Override
	public boolean isGoalReached(HashSet<Piece> config) {

		if (config != null) {
			if (config.size() == 1) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}

class Bishop extends Piece {

	/*
	 * Creates a bishop object.
	 * 
	 * @param int x - x row location int y - y col location int id - unique id for
	 * piece.
	 */
	public Bishop(int x, int y, int id) {

		super.locale = new Tuple(x, y);
		super.piecename = "Bishop";
		super.id = id;
	}

	/*
	 * Compares current ChessMainGame piece to given ChessMainGame piece object and
	 * compares all variables.
	 *
	 * @param Piece o - ChessMainGame piece to compare to0
	 * 
	 * @return int -1 if not equal 0 if equal.
	 *
	 */
	public int compareTo(Piece o) {

		if (this.id != o.id) {
			return -1;
		} else if (locale.x != o.getLocation().x || locale.y != o.getLocation().y) {
			return -1;
		} else if (piecename != o.piecename) {
			return -1;
		} else {
			return 0;
		}
	}

	/*
	 * Gets all possible move locations based off current board configuration.
	 * 
	 * @param HashSet<Piece> config - set of all ChessMainGame pieces in the game.
	 * 
	 * @return ArrayList<Tuple> moves list of all possible move locations.
	 */
	@Override
	public ArrayList<Tuple> getMoves(HashSet<Piece> config) {

		ArrayList<Tuple> moves = new ArrayList<Tuple>();
		HashSet<Piece> moveablese = new HashSet<Piece>();
		HashSet<Piece> moveablene = new HashSet<Piece>();
		HashSet<Piece> moveablenw = new HashSet<Piece>();
		HashSet<Piece> moveablesw = new HashSet<Piece>();

		for (Piece c : config) {
			int tempx = this.locale.x + 1;
			int tempy = this.locale.y + 1;
			while (tempx < ChessMainGame.getRows() && tempy < ChessMainGame.getCols()) {
				if (c.getLocation().x == tempx && c.getLocation().y == tempy) {
					moveablese.add(c);
				}
				tempx++;
				tempy++;
			}

			tempx = this.locale.x - 1;
			tempy = this.locale.y + 1;
			while (tempx > -1 && tempy < ChessMainGame.getCols()) {
				if (c.getLocation().x == tempx && c.getLocation().y == tempy) {
					moveablene.add(c);
				}
				tempx--;
				tempy++;
			}

			tempx = this.locale.x - 1;
			tempy = this.locale.y - 1;
			while (tempx > -1 && tempy > -1) {
				if (c.getLocation().x == tempx && c.getLocation().y == tempy) {
					moveablenw.add(c);
				}
				tempx--;
				tempy--;
			}

			tempx = this.locale.x + 1;
			tempy = this.locale.y - 1;
			while (tempx < ChessMainGame.getRows() && tempy > -1) {
				if (c.getLocation().x == tempx && c.getLocation().y == tempy) {
					moveablesw.add(c);
				}
				tempx++;
				tempy--;
			}

		}

		Piece northeast = null;
		Piece southeast = null;
		Piece northwest = null;
		Piece southwest = null;

		if (moveablene.size() > 1) {
			int distance = 1000;
			for (Piece c : moveablene) {
				int distanceform = (int) distance(this.locale.x, c.getLocation().x, this.locale.y, c.getLocation().y);
				if (distanceform < distance || northeast == null) {
					northeast = c;
					distance = distanceform;
				}
			}
			moves.add(northeast.getLocation());
		} else if (moveablene.size() == 1) {
			northeast = moveablene.iterator().next();
			moves.add(northeast.getLocation());
		}

		if (moveablese.size() > 1) {
			int distance = 1000;
			for (Piece c : moveablese) {
				int distanceform = (int) distance(this.locale.x, c.getLocation().x, this.locale.y, c.getLocation().y);
				if (distanceform < distance || southeast == null) {
					southeast = c;
					distance = distanceform;
				}
			}
			moves.add(southeast.getLocation());
		} else if (moveablese.size() == 1) {
			southeast = moveablese.iterator().next();
			moves.add(southeast.getLocation());
		}

		if (moveablenw.size() > 1) {
			int distance = 1000;
			for (Piece c : moveablenw) {

				int distanceform = this.locale.x - c.getLocation().x;

				if (distanceform < distance || northwest == null) {
					northwest = c;

					distance = distanceform;
				}
			}
			moves.add(northwest.getLocation());
		} else if (moveablenw.size() == 1) {
			northwest = moveablenw.iterator().next();
			moves.add(northwest.getLocation());
		}

		if (moveablesw.size() > 1) {
			int distance = 1000;
			for (Piece c : moveablesw) {
				int distanceform = c.getLocation().x - this.locale.x;
				if (distanceform < distance || southwest == null) {
					southwest = c;
					distance = distanceform;
				}
			}
			moves.add(southwest.getLocation());
		} else if (moveablesw.size() == 1) {
			southwest = moveablesw.iterator().next();
			moves.add(southwest.getLocation());
		}

		return moves;
	}

	/*
	 * Implementation of the distance formula to calculate closet pieces.
	 * 
	 * @param double x1 first x location double x2 second x location double y1 first
	 * y location double y2 second y location
	 * 
	 * @return double representation of the distance between both pairs.
	 */
	private double distance(double x1, double y1, double x2, double y2) {

		return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
	}
}

abstract class Piece implements Comparable<Piece> {

	protected Tuple locale;

	protected String piecename;

	protected int id;

	/*
	 * Prints the ChessMainGame Piece's board name;
	 * 
	 * @return String of the ChessMainGame piece board name.
	 */
	public String printBoardName() {

		return piecename.substring(0, 1);
	}

	/*
	 * Returns the ChessMainGame Piece's id
	 * 
	 * @return int ID of ChessMainGame Piece
	 */
	public int getID() {

		return id;
	}

	/*
	 * Creates the toString representation of the ChessMainGame Piece.
	 * 
	 * @return String String representation of the ChessMainGame piece.
	 */
	/*
	 * Move the piece to the location of another ChessMainGame piece.
	 * 
	 * @param Piece c - ChessMainGame piece to move to and remove HashSet<Piece>
	 * config - set of all ChessMainGame pieces in the game.
	 */
	public HashSet<Piece> movePiece(Piece c, HashSet<Piece> config) {

		this.locale = c.getLocation();
		config.remove(c);
		return config;
	}

	/*
	 * Generates all possible neighbors for a Piece and returns an array of the
	 * pieces.
	 * 
	 * @param ArrayList<Tuple> moves - list of possible move locations
	 * HashSet<Piece> config - set of all ChessMainGame pieces in the game.
	 * 
	 * @return ArrayList<HashSet<Piece>> with all possible adjecent configs.
	 */
	public ArrayList<HashSet<Piece>> getNeighbors(ArrayList<Tuple> moves, HashSet<Piece> config) {

		ArrayList<HashSet<Piece>> configs = new ArrayList<HashSet<Piece>>();

		for (Tuple move : moves) {

			Piece taken = null;
			Piece taker = null;
			for (Piece c : config) {
				if (c.getLocation().compareTo(move) == 0) {
					taken = c;
				}
				if (c.getLocation().compareTo(this.locale) == 0) {
					taker = c;
				}
			}
			if (taker != null && taken != null) {
				config = taker.movePiece(taken, config);
				configs.add(config);
			}

		}

		return configs;

	}

	/*
	 * Generates all possible move locations for the ChessMainGame piece on the
	 * given board.
	 * 
	 * @param HashSet<Piece> config - set of all ChessMainGame pieces in the game.
	 * 
	 * @return ArrayList<Tuple> list of all move locations.
	 */
	public abstract ArrayList<Tuple> getMoves(HashSet<Piece> config);

	/*
	 * Updates the Piece's location to given location
	 * 
	 * @param Tuple location new location to update ChessMainGame piece to.
	 */
	public void setLocation(Tuple location) {

		this.locale = location;
	}

	/*
	 * Returns the locatin of the ChessMainGame piece
	 * 
	 * @param - none.
	 * 
	 * @return Tuple location of ChessMainGame piece.
	 */
	public Tuple getLocation() {

		return locale;
	}

	/*
	 * Creates a unique hashcode for the object based off the variables.
	 * 
	 * @return int generated hashcode value.
	 */
	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((locale == null) ? 0 : locale.hashCode());
		result = prime * result + ((piecename == null) ? 0 : piecename.hashCode());
		return result;
	}

	/*
	 * Determines if a ChessMainGame piece and a given object are equal or not.
	 * 
	 * @param Object obj - any object to compare to.
	 * 
	 * @return boolean if they are equal or not.
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Piece other = (Piece) obj;
		if (id != other.id)
			return false;
		if (locale == null) {
			if (other.locale != null)
				return false;
		} else if (!locale.equals(other.locale))
			return false;
		if (piecename == null) {
			if (other.piecename != null)
				return false;
		} else if (!piecename.equals(other.piecename))
			return false;
		return true;
	}

}

class Knight extends Piece {

	/*
	 * Creates a knight object.
	 * 
	 * @param int x - x row location int y - y col location int id - unique id for
	 * piece.
	 */
	public Knight(int x, int y, int id) {

		super.locale = new Tuple(x, y);
		super.piecename = "Knight";
		super.id = id;
	}

	/*
	 * Overrides absctract class name because King and Knight share "K" as the first
	 * letter.
	 * 
	 * @param none.
	 * 
	 * @return String N
	 */
	@Override
	public String printBoardName() {

		return "N";
	}

	/*
	 * Compares current ChessMainGame piece to given ChessMainGame piece object and
	 * compares all variables.
	 * 
	 * @param Piece o - ChessMainGame piece to compare to0
	 * 
	 * @return int -1 if not equal 0 if equal.
	 */
	public int compareTo(Piece o) {

		if (this.id != o.id) {
			return -1;
		} else if (locale.x != o.getLocation().x || locale.y != o.getLocation().y) {
			return -1;
		} else if (piecename != o.piecename) {
			return -1;
		} else {
			return 0;
		}
	}

	/*
	 * Gets all possible move locations based off current board configuration.
	 * 
	 * @param HashSet<Piece> config - set of all ChessMainGame pieces in the game.
	 * 
	 * @return ArrayList<Tuple> moves list of all possible move locations.
	 */
	@Override
	public ArrayList<Tuple> getMoves(HashSet<Piece> config) {

		ArrayList<Tuple> moves = new ArrayList<Tuple>();
		for (Piece c : config) {
			int abs1 = Math.abs(c.getLocation().x - this.locale.x);
			int abs2 = Math.abs(c.getLocation().y - this.locale.y);

			if ((abs1 == 1 && abs2 == 2) || (abs1 == 2 && abs2 == 1)) {
				moves.add(new Tuple(c.getLocation().x, c.getLocation().y));
			}

		}

		return moves;
	}

}

class Pawn extends Piece {

	/*
	 * Creates a pawn object.
	 * 
	 * @param int x - x row location int y - y col location int id - unique id for
	 * piece.
	 */
	public Pawn(int x, int y, int id) {

		super.locale = new Tuple(x, y);
		super.piecename = "Pawn";
		super.id = id;
	}

	/*
	 * Compares current ChessMainGame piece to given ChessMainGame piece object and
	 * compares all variables.
	 *
	 * @param Piece o - ChessMainGame piece to compare to0
	 * 
	 * @return int -1 if not equal 0 if equal.
	 *
	 */
	public int compareTo(Piece o) {

		if (this.id != o.id) {
			return -1;
		} else if (locale.x != o.getLocation().x || locale.y != o.getLocation().y) {
			return -1;
		} else if (piecename != o.piecename) {
			return -1;
		} else {
			return 0;
		}
	}

	/*
	 * Gets all possible move locations based off current board configuration.
	 * 
	 * @param HashSet<Piece> config - set of all ChessMainGame pieces in the game.
	 * 
	 * @return ArrayList<Tuple> moves list of all possible move locations.
	 */
	@Override
	public ArrayList<Tuple> getMoves(HashSet<Piece> config) {

		ArrayList<Tuple> moves = new ArrayList<Tuple>();
		for (Piece c : config) {

			// move northeast
			if (c.getLocation().x == this.locale.x - 1 && c.getLocation().y == this.locale.y + 1) {

				moves.add(new Tuple(c.getLocation().x, c.getLocation().y));
			}
			// move northwest
			if (c.getLocation().x == this.locale.x - 1 && c.getLocation().y == this.locale.y - 1) {
				moves.add(new Tuple(c.getLocation().x, c.getLocation().y));
			}

		}

		return moves;
	}

}

class King extends Piece {

	/*
	 * Creates a bishop object.
	 * 
	 * @param int x - x row location int y - y col location int id - unique id for
	 * piece.
	 */
	public King(int x, int y, int id) {

		super.locale = new Tuple(x, y);
		super.piecename = "King";
		super.id = id;
	}

	/*
	 * Compares current ChessMainGame piece to given ChessMainGame piece object and
	 * compares all variables.
	 * 
	 * @param Piece o - ChessMainGame piece to compare to0
	 * 
	 * @return int -1 if not equal 0 if equal.
	 */
	@Override
	public int compareTo(Piece o) {

		if (this.id != o.id) {
			return -1;
		} else if (locale.x != o.getLocation().x || locale.y != o.getLocation().y) {
			return -1;
		} else if (piecename != o.piecename) {
			return -1;
		} else {
			return 0;
		}
	}

	/*
	 * Gets all possible move locations based off current board configuration.
	 * 
	 * @param HashSet<Piece> config - set of all ChessMainGame pieces in the game.
	 * 
	 * @return ArrayList<Tuple> moves list of all possible move locations.
	 */
	@Override
	public ArrayList<Tuple> getMoves(HashSet<Piece> config) {

		HashSet<Piece> clone = (HashSet<Piece>) config.clone();
		ArrayList<Tuple> moves = new ArrayList<Tuple>();
		for (Piece c : clone) {
			// south
			if (c.getLocation().x == this.locale.x + 1 && c.getLocation().y == this.locale.y) {
				moves.add(new Tuple(c.getLocation().x, c.getLocation().y));
			}
			// north
			if (c.getLocation().x == this.locale.x - 1 && c.getLocation().y == this.locale.y) {
				moves.add(new Tuple(c.getLocation().x, c.getLocation().y));
			}

			// east
			if (c.getLocation().x == this.locale.x && c.getLocation().y == this.locale.y + 1) {
				moves.add(new Tuple(c.getLocation().x, c.getLocation().y));
			}
			// west
			if (c.getLocation().x == this.locale.x && c.getLocation().y == this.locale.y - 1) {
				moves.add(new Tuple(c.getLocation().x, c.getLocation().y));
			}

			// southeast
			if (c.getLocation().x == this.locale.x + 1 && c.getLocation().y == this.locale.y + 1) {
				moves.add(new Tuple(c.getLocation().x, c.getLocation().y));
			}

			// northeast
			if (c.getLocation().x == this.locale.x - 1 && c.getLocation().y == this.locale.y + 1) {
				moves.add(new Tuple(c.getLocation().x, c.getLocation().y));
			}

			// northwest
			if (c.getLocation().x == this.locale.x - 1 && c.getLocation().y == this.locale.y - 1) {
				moves.add(new Tuple(c.getLocation().x, c.getLocation().y));
			}
			// southwest
			if (c.getLocation().x == this.locale.x + 1 && c.getLocation().y == this.locale.y - 1) {
				moves.add(new Tuple(c.getLocation().x, c.getLocation().y));
			}

		}
		// TODO Auto-generated method stub
		return moves;
	}

}

class Queen extends Piece {

	/*
	 * Creates a queen object.
	 * 
	 * @param int x - x row location int y - y col location int id - unique id for
	 * piece.
	 */
	public Queen(int x, int y, int id) {

		super.locale = new Tuple(x, y);
		super.piecename = "Queen";
		super.id = id;
	}

	/*
	 * Compares current ChessMainGame piece to given ChessMainGame piece object and
	 * compares all variables.
	 * 
	 * @param Piece o - ChessMainGame piece to compare to0
	 * 
	 * @return int -1 if not equal 0 if equal.
	 */
	public int compareTo(Piece o) {

		if (this.id != o.id) {
			return -1;
		} else if (locale.x != o.getLocation().x || locale.y != o.getLocation().y) {
			return -1;
		} else if (piecename != o.piecename) {
			return -1;
		} else {
			return 0;
		}
	}

	/*
	 * Gets all possible move locations based off current board configuration.
	 * 
	 * @param HashSet<Piece> config - set of all ChessMainGame pieces in the game.
	 * 
	 * @return ArrayList<Tuple> moves list of all possible move locations.
	 */
	@Override
	public ArrayList<Tuple> getMoves(HashSet<Piece> config) {

		ArrayList<Tuple> moves = new ArrayList<Tuple>();
		HashSet<Piece> moveablese = new HashSet<Piece>();
		HashSet<Piece> moveablene = new HashSet<Piece>();
		HashSet<Piece> moveablenw = new HashSet<Piece>();
		HashSet<Piece> moveablesw = new HashSet<Piece>();

		for (Piece c : config) {
			int tempx = this.locale.x + 1;
			int tempy = this.locale.y + 1;
			while (tempx < ChessMainGame.getRows() && tempy < ChessMainGame.getCols()) {
				if (c.getLocation().x == tempx && c.getLocation().y == tempy) {
					moveablese.add(c);
				}
				tempx++;
				tempy++;
			}

			tempx = this.locale.x - 1;
			tempy = this.locale.y + 1;
			while (tempx > -1 && tempy < ChessMainGame.getCols()) {
				if (c.getLocation().x == tempx && c.getLocation().y == tempy) {
					moveablene.add(c);
				}
				tempx--;
				tempy++;
			}

			tempx = this.locale.x - 1;
			tempy = this.locale.y - 1;
			while (tempx > -1 && tempy > -1) {
				if (c.getLocation().x == tempx && c.getLocation().y == tempy) {
					moveablenw.add(c);
				}
				tempx--;
				tempy--;
			}

			tempx = this.locale.x + 1;
			tempy = this.locale.y - 1;
			while (tempx < ChessMainGame.getRows() && tempy > -1) {
				if (c.getLocation().x == tempx && c.getLocation().y == tempy) {
					moveablesw.add(c);
				}
				tempx++;
				tempy--;
			}

		}

		Piece northeast = null;
		Piece southeast = null;
		Piece northwest = null;
		Piece southwest = null;

		if (moveablene.size() > 1) {
			int distance = 1000;
			for (Piece c : moveablene) {
				int distanceform = (int) distance(this.locale.x, c.getLocation().x, this.locale.y, c.getLocation().y);
				if (distanceform < distance || northeast == null) {
					northeast = c;
					distance = distanceform;
				}
			}
			moves.add(northeast.getLocation());
		} else if (moveablene.size() == 1) {
			northeast = moveablene.iterator().next();
			moves.add(northeast.getLocation());
		}

		if (moveablese.size() > 1) {
			int distance = 1000;
			for (Piece c : moveablese) {
				int distanceform = (int) distance(this.locale.x, c.getLocation().x, this.locale.y, c.getLocation().y);
				if (distanceform < distance || southeast == null) {
					southeast = c;
					distance = distanceform;
				}
			}
			moves.add(southeast.getLocation());
		} else if (moveablese.size() == 1) {
			southeast = moveablese.iterator().next();
			moves.add(southeast.getLocation());
		}

		if (moveablenw.size() > 1) {
			int distance = 1000;
			for (Piece c : moveablenw) {

				int distanceform = this.locale.x - c.getLocation().x;

				if (distanceform < distance || northwest == null) {
					northwest = c;

					distance = distanceform;
				}
			}
			moves.add(northwest.getLocation());
		} else if (moveablenw.size() == 1) {
			northwest = moveablenw.iterator().next();
			moves.add(northwest.getLocation());
		}

		if (moveablesw.size() > 1) {
			int distance = 1000;
			for (Piece c : moveablesw) {
				int distanceform = c.getLocation().x - this.locale.x;
				if (distanceform < distance || southwest == null) {
					southwest = c;
					distance = distanceform;
				}
			}
			moves.add(southwest.getLocation());
		} else if (moveablesw.size() == 1) {
			southwest = moveablesw.iterator().next();
			moves.add(southwest.getLocation());
		}

		HashSet<Piece> moveable = new HashSet<Piece>();

		for (Piece c : config) {
			if ((c.getLocation().x == this.locale.x || c.getLocation().y == this.locale.y) && c.compareTo(this) != 0) {
				moveable.add(c);
			}

		}

		Piece north = null;
		Piece south = null;
		Piece east = null;
		Piece west = null;

		for (Piece c : moveable) {
			// horizontal movement
			if (c.getLocation().x == this.locale.x) {
				int relative = c.getLocation().y - this.locale.y;
				if (relative > 0) {
					// east
					if (east == null || east.getLocation().y > c.getLocation().y) {
						east = c;
					}

				} else {
					// west
					if (west == null || west.getLocation().y < c.getLocation().y) {
						west = c;
					}
				}
			}
			// vertical movement
			if (c.getLocation().y == this.locale.y) {
				int relative = c.getLocation().x - this.locale.x;
				if (relative < 0) {
					// north
					if (north == null || north.getLocation().x < c.getLocation().x) {
						north = c;
					}

				} else {
					// west
					if (south == null || south.getLocation().x > c.getLocation().x) {
						south = c;
					}
				}
			}
		}

		if (north != null) {
			moves.add(north.getLocation());
		}
		if (south != null) {
			moves.add(south.getLocation());
		}
		if (east != null) {
			moves.add(east.getLocation());
		}
		if (west != null) {
			moves.add(west.getLocation());
		}

		return moves;

	}

	/*
	 * Implementation of the distance formula to calculate closet pieces.
	 * 
	 * @param double x1 first x location double x2 second x location double y1 first
	 * y location double y2 second y location
	 * 
	 * @return double representation of the distance between both pairs.
	 */
	private double distance(double x1, double y1, double x2, double y2) {

		return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
	}
}

class Rook extends Piece {

	/*
	 * Creates a rook object.
	 * 
	 * @param int x - x row location int y - y col location int id - unique id for
	 * piece.
	 */
	public Rook(int x, int y, int id) {

		super.locale = new Tuple(x, y);
		super.piecename = "Rook";
		super.id = id;
	}

	/*
	 * Compares current ChessMainGame piece to given ChessMainGame piece object and
	 * compares all variables.
	 * 
	 * @param Piece o - ChessMainGame piece to compare to0
	 * 
	 * @return int -1 if not equal 0 if equal.
	 */
	public int compareTo(Piece o) {

		if (this.id != o.id) {
			return -1;
		} else if (locale.x != o.getLocation().x || locale.y != o.getLocation().y) {
			return -1;
		} else if (piecename != o.piecename) {
			return -1;
		} else {
			return 0;
		}
	}

	/*
	 * Gets all possible move locations based off current board configuration.
	 * 
	 * @param HashSet<Piece> config - set of all ChessMainGame pieces in the game.
	 * 
	 * @return ArrayList<Tuple> moves list of all possible move locations.
	 */
	@Override
	public ArrayList<Tuple> getMoves(HashSet<Piece> config) {

		ArrayList<Tuple> moves = new ArrayList<Tuple>();
		HashSet<Piece> moveable = new HashSet<Piece>();

		for (Piece c : config) {
			if ((c.getLocation().x == this.locale.x || c.getLocation().y == this.locale.y) && c.compareTo(this) != 0) {
				moveable.add(c);
			}

		}

		Piece north = null;
		Piece south = null;
		Piece east = null;
		Piece west = null;

		for (Piece c : moveable) {
			// horizontal movement
			if (c.getLocation().x == this.locale.x) {
				int relative = c.getLocation().y - this.locale.y;
				if (relative > 0) {
					// east
					if (east == null || east.getLocation().y > c.getLocation().y) {
						east = c;
					}

				} else {
					// west
					if (west == null || west.getLocation().y < c.getLocation().y) {
						west = c;
					}
				}
			}
			// vertical movement
			if (c.getLocation().y == this.locale.y) {
				int relative = c.getLocation().x - this.locale.x;
				if (relative < 0) {
					// north
					if (north == null || north.getLocation().x < c.getLocation().x) {
						north = c;
					}

				} else {
					// west
					if (south == null || south.getLocation().x > c.getLocation().x) {
						south = c;
					}
				}
			}
		}

		if (north != null) {
			moves.add(north.getLocation());
		}
		if (south != null) {
			moves.add(south.getLocation());
		}
		if (east != null) {
			moves.add(east.getLocation());
		}
		if (west != null) {
			moves.add(west.getLocation());
		}

		return moves;
	}

}

class Tuple implements Comparable<Tuple> {
	public final int x;
	public final int y;

	public Tuple(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Tuple t) {

		if (this.x == t.x && this.y == t.y) {

			return 0;

		} else {
			return -1;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tuple other = (Tuple) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */

}

abstract class Problem<E> {

	protected Integer goal;

	protected E start;

	/**
	 * getGoal gets the end config for the Problem
	 * 
	 * @return an int which represents the goal config
	 */
	public Integer getGoal() {

		return goal;
	}

	/**
	 * Each kind of Problem will know a separate way to return its neighbors
	 */
	public abstract ArrayList<E> getNeighbors(E config);

	/**
	 * getStart gets the starting configuration for the Problem
	 * 
	 * @return an int which represents the start config
	 */
	public E getStart() {

		return start;
	}

	/**
	 * Compares a given config with the Problem's personal goal config
	 * 
	 * @param config
	 *            - Type E, a Problem's style of config
	 * @return boolean - true if the config is the same as the goal, or within.
	 */
	public abstract boolean isGoalReached(E config);
}

class Solver<E> {
	private Problem<E> myPuzzle;

	int cutOff = 0;
	int result = 1;

	/**
	 * Constructs a solver object
	 * 
	 * @param thePuzzle
	 *            any kind of valid Problem
	 */
	public Solver(Problem<E> thePuzzle) {
		myPuzzle = thePuzzle;
	}

	/**
	 * @param args
	 *            command line args, unused.
	 */
	public static void main(String[] args) {

		// TODO Auto-generated method stub

	}

	/**
	 * solve() utilizes a BFS along with an ArrayList to return the steps needed to
	 * solve a Problem The ArrayList current lists the current steps taken, kind of
	 * like the dictionary in cs242
	 * 
	 * @param curConf
	 *            is a general Problem with it's starting config
	 * @return an ArrayList with the steps needed to solve the Problem
	 */
	public ArrayList<E> BFS() {
		/*
		 * 1) Get starting step 2) Enqueue starting step 3) Place starting step in
		 * visited list -Loop- while queue not empty 4) Dequeue as current step 5) Check
		 * if goal step 6) Add all neighbors to queue -Loop back- 7) If found solution,
		 * return solution
		 */
		HashMap<E, E> visited = new HashMap<E, E>();
		ArrayList<E> queue = new ArrayList<E>();
		boolean found = false;
		E currentStep = myPuzzle.getStart();
		queue.add(currentStep);
		visited.put(currentStep, null);
		while (!queue.isEmpty()) {
			currentStep = queue.get(0);
			queue.remove(0);
			if (myPuzzle.isGoalReached(currentStep)) {
				found = true;
				break;
			}
			for (E adjecent : myPuzzle.getNeighbors(currentStep)) {
				if (!visited.containsKey(adjecent)) {
					visited.put(adjecent, currentStep);
					queue.add(adjecent);
				}
			}

		}
		if (found) {
			ArrayList<E> backStep = new ArrayList<E>();
			ArrayList<E> steps = new ArrayList<E>();
			backStep.add(currentStep);
			while (visited.get(currentStep) != null) {
				backStep.add(visited.get(currentStep));
				currentStep = visited.get(currentStep);
			}
			for (int i = backStep.size() - 1; i >= 0; i--) {
				steps.add(backStep.get(i));
			}
			return steps;
		}
		return null; // No solution found
	}

	public ArrayList<E> DFS() {
		/*
		 * 1) Get starting step 2) Enqueue starting step 3) Place starting step in
		 * visited list -Loop- while queue not empty 4) Dequeue as current step 5) Check
		 * if goal step 6) Add all neighbors to queue -Loop back- 7) If found solution,
		 * return solution
		 */
		HashMap<E, E> visited = new HashMap<E, E>();
		Stack<E> stack = new Stack<>();
		boolean found = false;
		E currentStep = myPuzzle.getStart();
		stack.push(currentStep);
		visited.put(currentStep, null);
		while (!stack.isEmpty()) {
			currentStep = stack.get(0);
			stack.pop();
			if (myPuzzle.isGoalReached(currentStep)) {
				found = true;
				break;
			}
			for (E adjecent : myPuzzle.getNeighbors(currentStep)) {
				if (!visited.containsKey(adjecent)) {
					visited.put(adjecent, currentStep);
					stack.push(adjecent);
				}
			}

		}
		if (found) {
			ArrayList<E> backStep = new ArrayList<E>();
			ArrayList<E> steps = new ArrayList<E>();
			backStep.add(currentStep);
			while (visited.get(currentStep) != null) {
				backStep.add(visited.get(currentStep));
				currentStep = visited.get(currentStep);
			}
			for (int i = backStep.size() - 1; i >= 0; i--) {
				steps.add(backStep.get(i));
			}
			return steps;
		}
		return null; // No solution found
	}

	public ArrayList<E> Iterative_Deepening_Search() {
		/*
		 * 1) Get starting step 2) Enqueue starting step 3) Place starting step in
		 * visited list -Loop- while queue not empty 4) Dequeue as current step 5) Check
		 * if goal step 6) Add all neighbors to queue -Loop back- 7) If found solution,
		 * return solution
		 */
		HashMap<E, E> visited = new HashMap<E, E>();
		Stack<E> stack = new Stack<>();
		boolean found = false;
		E currentStep = myPuzzle.getStart();
		stack.push(currentStep);
		visited.put(currentStep, null);
		while (!stack.isEmpty()) {
			currentStep = stack.get(0);
			stack.pop();
			if (myPuzzle.isGoalReached(currentStep)) {
				found = true;
				break;
			}
			if (result != cutOff) { // Depth With limit
				for (E adjecent : myPuzzle.getNeighbors(currentStep)) {
					if (!visited.containsKey(adjecent)) {
						visited.put(adjecent, currentStep);
						stack.push(adjecent);
					}
				}
			}

		}
		if (found) {
			ArrayList<E> backStep = new ArrayList<E>();
			ArrayList<E> steps = new ArrayList<E>();
			backStep.add(currentStep);
			while (visited.get(currentStep) != null) {
				backStep.add(visited.get(currentStep));
				currentStep = visited.get(currentStep);
			}
			for (int i = backStep.size() - 1; i >= 0; i--) {
				steps.add(backStep.get(i));
			}
			return steps;
		}
		return null; // No solution found
	}
}
