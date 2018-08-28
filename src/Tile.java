// Represents each individual tile inside the chess board 
// there will be a total of 64 tiles in the entire board

public class Tile {
    
    // Instance variables
    private boolean isEmpty;
    private String coordinate;
    private ChessPiece currentPiece;
    
    // Constructs a tile object, if piece is null the tile is empty
    public Tile(String coordinate, ChessPiece piece) {
        this.coordinate = coordinate;
        currentPiece = piece;
        // If tile is empty
        if (piece == null) {
            isEmpty = true;
        } else {
            isEmpty = false;
        }
    }
    
    // Returns whether the tile is occupied
    public boolean isEmpty() {
        return isEmpty;
    }
    
    // Place a piece onto this tile
    public void placePiece(ChessPiece piece) {
        currentPiece = piece;
        isEmpty = false;
    }
    
    // Remove a piece from this tile
    public void removePiece() {
        currentPiece = null;
        isEmpty = true;
    }
    
    // Returns the currentPiece on this tile, if empty, returns null
    public ChessPiece getPiece() {
        return currentPiece;
    }
    
    // Returns the coordinate of the current tile
    public String getCoord() {
        return coordinate;
    }
    
    // To String method
    @Override
    public String toString() {
        // If piece is null
        if (currentPiece == null) {
            return "[    ] ";
        }
        return "[" + currentPiece.toString() + "] ";
    }
    
    // Print Tile method
    public void printTile() {
        // If piece is null
        if (currentPiece == null) {
            System.out.printf("%-14s%-4s", "[", "]");
        } else {
            System.out.printf("%-6s%s%-4s", "[", currentPiece.toString(), "]");
        }
    }
}