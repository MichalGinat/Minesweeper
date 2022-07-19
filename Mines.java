package mines;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//This class describes the game minesweeper without a graphical interface
public class Mines {
	private int height, width;
	private Place[][] board;
	private boolean showAll = false;

	// A constructor that initializes the board with data given
	// puts mines on the board in randomly
	public Mines(int height, int width, int numMines) {
		this.height = height;
		this.width = width;
		board = new Place[height][width];
		// Initializes placements on the board
		for (int i = 0; i < height; i++) { // passes on the rows
			for (int j = 0; j < width; j++) // passes on the columns
				board[i][j] = new Place();
		}
		Random rand = new Random();
		for (int i = 0; i < numMines; i++) { // puts the mines randomly
			if (!addMine(rand.nextInt(height), rand.nextInt(width)))
				i--; // if addMine return false, there is already mine, therfore try to put again.
		}
	}

	// inner class that describes specific place
	private class Place {
		private boolean isMine, isOpen, isFlag;
		private int minesNearNum = 0;
	}

	// Adds a mine in place of the given place.
	public boolean addMine(int i, int j) {
		if (board[i][j].isMine)
			return false; // if there is already mine
		board[i][j].isMine = true; // adds mine
		List<Point> listOfNeighbors = neighbors(i, j);
		for (Point neighbor : listOfNeighbors)
			board[neighbor.x][neighbor.y].minesNearNum++; // Adds to the number of neighbors' mines
		return true;
	}

	// Indicates that the user is opening this place.
	public boolean open(int i, int j) {
		if (board[i][j].isOpen)
			return true; // this place is already open
		if (board[i][j].isMine)
			return false; // this place is mine
		board[i][j].isOpen = true;
		if (board[i][j].minesNearNum != 0)
			return true; // There are mines around it so it will only return true
		List<Point> listOfNeighbors = neighbors(i, j);
		// There are no mines around it so it opens up all the neighbors recursively
		for (Point neighbor : listOfNeighbors)
			open(neighbor.x, neighbor.y);
		return true;
	}

	// puts a flag in the given location, or remove it if there is already a flag.
	public void toggleFlag(int x, int y) {
		board[x][y].isFlag = !board[x][y].isFlag;
	}

	// Returns true if all non-mine locations are open.
	public boolean isDone() {
		for (int i = 0; i < height; i++) { // passes on board's row
			for (int j = 0; j < width; j++) { // passes on board's column
				if ((!board[i][j].isMine && !board[i][j].isOpen))
					return false;
			}
		}
		return true;
	}

	// Returns representation as a string of the place
	public String get(int i, int j) {
		if (!board[i][j].isOpen && !showAll && board[i][j].isFlag)
			return "F"; // if closed and showAll is false and the flag is marked
		else if (!board[i][j].isOpen && !showAll)
			return "."; // if closed and showAll is false
		else if (board[i][j].isMine)
			return "X"; // if there is mine
		else if (board[i][j].minesNearNum != 0)
			return "" + board[i][j].minesNearNum; // There are mines around
		return " "; // There are not mines around
	}

	// Sets the value of the showAll field.
	public void setShowAll(boolean showAll) {
		this.showAll = showAll;
	}

	@Override
	public String toString() {
		// Returns a description of the board as a string by get for each place
		StringBuilder stringBoard = new StringBuilder();
		for (int i = 0; i < height; i++) { // passes on board's row
			for (int j = 0; j < width; j++) // passes on board's column
				stringBoard.append(get(i, j)); // adds specific place to stringBuilder
			stringBoard.append("\n");
		}
		return stringBoard.toString();
	}

	// inner class that describes specific location by x,y
	private class Point {
		private int x, y;
	}

	// Returns a list of points that are the neighbors of the same place
	private List<Point> neighbors(int row, int col) {
		List<Point> listOfNeighbors = new ArrayList<>();
		for (int i = row - 1; i < row + 2; i++) { // passes on potential rows
			for (int j = col - 1; j < col + 2; j++) { // passes on potential columns
				if (isInside(i, j) && (i != row || j != col)) {
					Point point = new Point();
					point.x = i;
					point.y = j;
					listOfNeighbors.add(point);
				}
			}
		}
		return listOfNeighbors;
	}

	// A method that returns whether the point is inside or outside the board
	private boolean isInside(int row, int col) {
		return !(row < 0 || row >= height || col < 0 || col >= width);
	}

}
