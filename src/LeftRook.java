// Left rook

public class LeftRook extends Rook {
    
    // Class constants
    private static final String WCASTLEPOS = "d1";
    private static final String BCASTLEPOS = "f8";
    
    // Creates the constructor
    public LeftRook(String coord, char color, Tile[][] board) {
        // Calls the superclass's constructor
        super(coord, color, board);
    }
    
    @Override
    public void findAllMoves() {
        // Calls superclass's findAllMoves() method
        super.findAllMoves();
        // Add castle as a possible move
        if (!moved && castle()) {
            if (color == 'w') { // If we are white
                possibleMoves.add(WCASTLEPOS);
            } else { // We are black
                possibleMoves.add(BCASTLEPOS);
            }
        }
    }
    
    // Checks to see if castle is possible
    // The positions after castle must not be under attack
    public boolean castle() {
        if (color == 'w') { // We are white
            return !underAttack(WCASTLEPOS) && !underAttack("c1") && castle(WKINGPOS);
        } else { // We are black
            return !underAttack(BCASTLEPOS) && !underAttack("g8") && castle(BKINGPOS);
        }
    }
    
    // Castle based on the king's position
    public boolean castle(String pos) {
        // Used for compare to see if the king is in the right place
        King temp = new King(pos, col, board);
        // If the tile at position is not empty and the piece there is a king
        if (!board[pos.charAt(1)][pos.charAt(0) - 'a'].isEmpty() && 
                board[pos.charAt(1)][pos.charAt(0) - 'a'].getPiece().getClass() == temp.getClass()) {
            King king = (King) board[pos.charAt(1)][pos.charAt(0) - 'a'].getPiece();
            // If the king has not moved, adds the possible move depending on the color
            if (!king.hasMoved() && king.color == color) {
                return true;
            }
        }
        return false;
    }
}