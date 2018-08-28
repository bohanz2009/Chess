// The Bishop

public class Bishop extends ChessPiece {
    
    // Creates the constructor
    public Bishop(String coord, char color, Tile[][] board) {
        // Calls the superclass's constructor
        super(coord, color, board);
    }
    
    /**
     * Find all of the moves this piece can possible make
     */
    public void findAllMoves() {
        // Calls another version with a direction of movement
        findAllMoves(-1, -1);
        findAllMoves(1, -1);
        findAllMoves(-1, 1);
        findAllMoves(1, 1);
    }
    
    // Helper method to findAllMoves
    private void findAllMoves(int deltaRow, int deltaCol) {
        // Creates new row and column values
        int newRow = row + deltaRow;
        int newCol = col + deltaCol;
        // While both are in bound
        while (newRow >= 0 && newRow < SIZE && newCol >= 0 && newCol < SIZE) {
            ChessPiece temp = board[newRow][newCol - 'a'].getPiece();
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
    
    // To String method
    @Override
    public String toString() {
        return "(" + color + ")B";
    }
}