// Superclass for all the pieces in the game
import java.util.HashSet;

public abstract class ChessPiece {
    
    // Instance variables
    protected int row;
    protected char col;
    protected String coord;
    protected HashSet<String> possibleMoves;
    protected char color;
    protected Tile[][] board;
    protected static final int COORDSIZE = 2;
    protected static final int SIZE = 8;
    
    // Private constructor for the subclasses
    public ChessPiece(String coord, char color, Tile[][] board) {
        // Declares the row, column, color, and all moves
        row = coord.charAt(1) - '0';
        row = SIZE - row; // To avoid index out of bound errors
        col = coord.charAt(0);
        this.color = color;
        this.coord = coord;
        possibleMoves = new HashSet<String>();
        this.board = board;
        findAllMoves();
    }
    
    /**
     * Checks to see if the move is legal
     * Pre: dest must be in bound of the board
     * @param dest
     * @return Whether the move is legal
     */
    public boolean move(String dest) {
        // Checks to see if the move is in the list of all possible moves
        for (String move : possibleMoves) {
            if (move.equals(dest)) { // Move is legal
                return true;
            }
        }
        return false;
    }
    
    /**
     * Find all of the moves this piece can possible make
     */
    public abstract void findAllMoves();
    
    // Returns all moves
    public HashSet<String> getAllMoves() {
        return possibleMoves;
    }
    
    // Returns the row
    public int getRow() {
        return Math.abs(row - SIZE);
    }
    
    // Returns the column
    public char getCol() {
        return col;
    }
    
    // Change the row of the piece
    public void changeRow(int row) {
        this.row = row;
    }
    
    // Change the column of the piece
    public void changeColumn(char col) {
        this.col = col;
    }
    
    // Returns the color of the piece
    public char getColor() {
        return color;
    }
    
    // Prints out the piece
    public void printPiece() {
        System.out.printf("%-6s", this.toString());
    }
}