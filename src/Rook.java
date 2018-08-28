// The Rook
import java.util.HashSet;

public class Rook extends ChessPiece {
    
    // Instance variables and class constants
    protected boolean moved;
    protected static final String WKINGPOS = "e1";
    protected static final String BKINGPOS = "e8";
    
    // Creates the constructor
    public Rook(String coord, char color, Tile[][] board) {
        // Calls the superclass's constructor
        super(coord, color, board);
        moved = false;
    }
    
    // Change the rook's movement status
    public void changeMoved() {
        moved = true;
    }
    
    /**
     * Find all of the moves this piece can possible make
     */
    public void findAllMoves() {
        // Calls another version with a direction of movement
        findAllMoves(-1, 0);
        findAllMoves(1, 0);
        findAllMoves(0, 1);
        findAllMoves(0, -1);
    }
    
    // Helper method to findAllMoves
    private void findAllMoves(int deltaRow, int deltaCol) {
        // Creates new row and column values
        int newRow = row + deltaRow;
        int newCol = col + deltaCol;
        // While both are in bound
        while (newRow >= 0 && newRow < SIZE && newCol >= 0 && newCol < SIZE) {
            ChessPiece temp = board[newRow][newCol].getPiece();
            if (temp == null) { // If the tile is empty 
                possibleMoves.add((char)newCol + "" + newRow);
            } else if (temp.getColor() != color) { // If the piece is not ours
                possibleMoves.add((char)newCol + "" + newRow);
                break;
            } else { // The piece is ours, stop
                break;
            }
            newRow += deltaRow;
            newCol += deltaCol;
        }
    }
    
    // Checks for Attack when castling
    protected boolean underAttack(String pos) {
        // Loop through the board
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                ChessPiece temp = board[i][j].getPiece();
                // If the tile is not empty
                if (!board[i][j].isEmpty() && temp.getColor() != color) {
                    // Check to see if the tile is under attack
                    if (checkForAttack(pos, temp)) {
                        return true;
                    }
                } else if (!board[i][j].isEmpty() && temp.getColor() == color) {
                    // If the tile is occupied by our own piece
                    return true;
                }
            }
        }
        return false;
    }
    
    // Checks for attack, pawn is a special case
    private boolean checkForAttack(String pos, ChessPiece temp) {
        HashSet<String> moves = temp.getAllMoves();
        // If we are dealing with a pawn
        if (temp instanceof Pawn) {
            moves = ((Pawn) temp).getAllAttacks();
        } 
        for (String move : moves) {
            if(move.equals(pos)) { // Under attack
                return true;
            }
        }
        return false;
    }
    
    // To String method
    @Override
    public String toString() {
        return "(" + color + ")R";
    }
    
}