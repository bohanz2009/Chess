// Pawn
import java.util.HashSet;

public class Pawn extends ChessPiece {
    
    // Instance variables
    private boolean moved;
    private HashSet<String> possibleAttacks;
    
    // Creates the constructor
    public Pawn(String coord, char color, Tile[][] board) {
        // Calls the superclass's constructor
        super(coord, color, board);
        moved = false;
    }
    
    // Returns if the pawn has moved or not
    public boolean getMoved() {
        return moved;
    }
    
    // Change the pawn's movement status
    public void changeMoved() {
        moved = true;
    }
    
    /**
     * Find all of the moves this piece can possible make
     */
    public void findAllMoves() {
        // Calls another version with a direction of movement
        possibleMoves = new HashSet<String>();
        int newRow = changePawnRow(row);
        boolean oneStepIsLegal = checkMoves(newRow);
        // If the piece has not moved, the pawn's first move can be two places
        checkTwoSpot(changePawnRow(newRow), oneStepIsLegal);
        checkAttackPos();
    }
    
    // Changes the row position based on the color
    private int changePawnRow(int temp) {
        if (color == 'w') { // If we are white
            return temp - 1;
        } 
        return temp + 1;
    }
    
    // Checks the movement position without attacking
    private boolean checkMoves(int newRow) {
        // If move is in bound
        if (newRow >= 0 && newRow < SIZE) {
            // If the tile is empty or if the piece there is not one of our own
            if (board[newRow][col - 'a'].isEmpty() || board[newRow][col - 'a'].getPiece().getColor() != color) {
                possibleMoves.add(col + "" + Math.abs(SIZE - newRow));
                return true;
            }
        }
        return false;
    }
    
    // Checks to see if the pawn can move two spaces
    private void checkTwoSpot(int temp, boolean prevIsLegal) {
        // If the pawn has not moved and the one step move is legal
        if (!moved && prevIsLegal) {
            checkMoves(temp);
        }
    }
    
    // Checks to see the pawn's attack positions
    private void checkAttackPos() {
        // Check attack positions based on direction
        if (color == 'b') { // If we are black
            checkAttackPos(1, 1);
            checkAttackPos(-1, 1);
        } else {
            checkAttackPos(1, -1);
            checkAttackPos(-1, -1);
        }
    }
    
    // Checks attack position based on column and row values
    private void checkAttackPos(int deltaCol, int deltaRow) {
        possibleAttacks = new HashSet<>();
        // Loop through possible moves
        int indexRow = row;
        int indexCol = col - 'a';
        // Creates new row and column values
        indexRow += deltaRow;
        indexCol += deltaCol;
        // If both are in bound
        if (indexRow >= 0 && indexRow < SIZE && indexCol >= 0 && indexCol < SIZE) {
            // If the piece at the destination tile exists and is not one of our own;
            if (!board[indexRow][indexCol].isEmpty() && board[indexRow][indexCol].getPiece().getColor() != color) {
                indexCol = (indexCol + 'a');
                possibleAttacks.add((char)indexCol + "" + Math.abs(SIZE - indexRow));
            }
        }
        
        // Add the attacking positions into possible moves
        addAttack(possibleAttacks);
    }
    
    // Adds attacking positions into possible moves
    private void addAttack(HashSet<String> possibleAttacks) {
        // Loop through attacks and add it to moves
        for (String attack: possibleAttacks) {
            possibleMoves.add(attack);
        }
    }
    
    // Returns the list of possible attack positions
    public HashSet<String> getAllAttacks() {
        return possibleAttacks;
    }
    
    // To String method
    @Override
    public String toString() {
        return "(" + color + ")P";
    }
}