// The Knight

public class Knight extends ChessPiece {
    
 // Creates the constructor
    public Knight(String coord, char color, Tile[][] board) {
        // Calls the superclass's constructor
        super(coord, color, board);
    }
    
    /**
     * Find all of the moves this piece can possible make
     */
    public void findAllMoves() {
        // Calls another version with a direction of movement
        // Knight can move eight places
        findAllMoves(-1, 2);
        findAllMoves(1, 2);
        findAllMoves(-1, -2);
        findAllMoves(1, -2);
        findAllMoves(2, -1);
        findAllMoves(2, 1);
        findAllMoves(-2, -1);
        findAllMoves(-2, 1);
    }
    
    // Helper method to findAllMoves
    private void findAllMoves(int deltaRow, int deltaCol) {
        // Creates new row and column values
        int newRow = row + deltaRow;
        int newCol = col + deltaCol;
        // If both are in bound
        if (newRow >= 0 && newRow < SIZE && newCol >= 0 && newCol < SIZE) {
            // If the dest tile is empty or if the piece there is not our own
            if (board[newRow][newCol - 'a'].isEmpty() || 
                    board[newRow][newCol - 'a'].getPiece().getColor() != color) {
                possibleMoves.add((char)newCol + "" + newRow);
            }
        }
    }
    
    // To String method
    @Override
    public String toString() {
        return "(" + color + ")N";
    }
}