// The King
import java.util.HashSet;

public class King extends ChessPiece {
    
    // Instance variables
    private boolean moved;
    private boolean inCheck;
    
    // Constructs a King object
    public King(String coord, char color, Tile[][] board) {
        // Calls the super constructor
        super(coord, color, board);
        moved = false;
        inCheck = false;
    }
    
    // Returns whether the piece has moved or not
    public boolean hasMoved() {
        return moved;
    }
    
    // Changes king to have moved
    public void changeMoved() {
        moved = true;
    }
    
    // Changes inCheck if the King is under attack
    public void check(boolean check) {
        inCheck = check;
    }
    
    // Returns whether the King is in check
    public boolean inCheck() {
        return inCheck;
    }
    
    /**
     * Find all of the moves this piece can possible make
     */
    public void findAllMoves() {
        // Checks for that the king is not being attacked when moving to a specific spot
        HashSet<String> allAttacks = checkForAttack();
        // Add all possible moves
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // Change the coordinates
                int tempRow = row + i;
                int tempCol = col + j;
                addMove(tempRow, tempCol, allAttacks);
            }
        }
        // Checks for possible castles
        // checkCastle();
    }
    
    // Checks to see if the move is legal given a coordinate and adds the legal ones
    private void addMove(int newRow, int newCol, HashSet<String> allAttacks) {
        // If the coordinate is in bound
        if (newRow >= 0 && newRow < SIZE && newCol >= 0 && newCol < SIZE) {
            // If the piece at the destination tile exists and is not one of our own
            ChessPiece temp = board[newRow][newCol - 'a'].getPiece();
            if (temp == null || temp.getColor() != color ) {
                String pos = (char) newCol + "" + newRow;
                // If the move is not in an attack position
                if (!allAttacks.contains(pos)) {
                    possibleMoves.add(pos);
                }
            }
        }
    }
    
    // Checks for the King is not being attacked
    private HashSet<String> checkForAttack() {
        // Loop through board
        HashSet<String> allAttacks = new HashSet<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                // If the tile is not empty and the piece is opposite color
                if (!board[i][j].isEmpty() && board[i][j].getPiece().getColor() != color) {
                    ChessPiece temp = board[i][j].getPiece();
                    // Adds all of the attacking positions of this piece to the set
                    for (String pos : temp.getAllMoves()) {
                        allAttacks.add(pos);
                    }
                }
            }
        }
        return allAttacks;
    }
    
    // Checks to see if the King is under attack
    public boolean isInCheck() {
        // Get all of the attacking positions
        HashSet<String> allAttacks = checkForAttack();
        inCheck =  allAttacks.contains(coord);
        return inCheck;
    }
    
//    // Checks to see if the king can castle
//    private void checkCastle() {
//        // There are four possible rook positions
//        if (color == 'w') {
//            checkCastle(7, 0);
//            checkCastle(7, 7);
//        } else {
//            checkCastle(0, 0);
//            checkCastle(0, 7);
//        }
//    }
//    
//    // Checks castle based on the position of the rook
//    private void checkCastle(int row, int col) {
//        // Check to see if the piece at the given pos is a rook
//        if (board[row][col].getPiece() != null && board[row][col].getPiece() instanceof Rook) {
//            // We know the piece must be a rook
//            Rook temp = (Rook) board[row][col].getPiece();
//            // If castle is true, add the possible castlePos
//            if (temp.castle()) {
//                
//            }
//        }
//    }
    
    // To String Method
    @Override
    public String toString() {
        return "(" + color + ")K";
    }
}