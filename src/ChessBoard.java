// Creates the chess board

public class ChessBoard {
    
    // Instance variables
    private Tile[][] board;
    private Tile[][] prevBoard;
    private char curr;
    private int moves;
    private boolean checkMate;
    private boolean promotion;
    private static final int SIZE = 8;
    private static final char WHITE = 'w';
    private static final char BLACK = 'b';
    
    // Constructor for the ChessBoard class
    public ChessBoard() {
        // Chess board is always 8 x 8, and always starts with white
        board = new Tile[SIZE][SIZE];
        prevBoard = new Tile[SIZE][SIZE];
        createTiles(board);
        createTiles(prevBoard);
        addPieces();
        curr = WHITE;
        moves = 0;
        checkMate = false;
        promotion = false;
    }
    
    // Constructor that takes in a given board
    public ChessBoard(Tile[][] board) {
        this.board = makeCopy(board);
        // Same as the other constructor
        prevBoard = new Tile[SIZE][SIZE];
        createTiles(prevBoard);
        addPieces();
        curr = WHITE;
        moves = 0;
    }
    
    // Fills in the board with empty tiles
    private void createTiles(Tile[][] board) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                String col = ((char) (j + 'a')) + "";
                board[i][j] = new Tile(col + j, null);
            }
        }
    }
    
    // Adds the pieces in the board
    // Hard coded because chess board is always 8 x 8
    private void addPieces() {
        // Adds the pawns
        for (int i = 0; i < SIZE; i++) {
            String col = ((char) (i + 'a')) + "";
            board[6][i].placePiece(new Pawn(col + (2), WHITE, board));
            board[1][i].placePiece(new Pawn(col + (7), BLACK, board));
        }
        // Add Kings
        board[7][4].placePiece(new King("e" + 1, WHITE, board));
        board[0][4].placePiece(new King("e" + 8, BLACK, board));
        // Add Queens
        board[7][3].placePiece(new Queen("d" + 1, WHITE, board));
        board[0][3].placePiece(new Queen("d" + 8, BLACK, board));
        // Add Knights
        addKnights();
        // Add Bishops
        addBishops();
        // Add Rooks
        addRooks();
    }
    
    // Adds the knights
    private void addKnights() {
        board[7][1].placePiece(new Knight("b" + 1, WHITE, board));
        board[0][1].placePiece(new Knight("b" + 8, BLACK, board));
        board[7][6].placePiece(new Knight("g" + 1, WHITE, board));
        board[0][6].placePiece(new Knight("g" + 8, BLACK, board));
    }
    
    // Adds the bishops
    private void addBishops() {
        board[7][2].placePiece(new Bishop("c" + 1, WHITE, board));
        board[0][2].placePiece(new Bishop("c" + 8, BLACK, board));
        board[7][5].placePiece(new Bishop("f" + 1, WHITE, board));
        board[0][5].placePiece(new Bishop("f" + 8, BLACK, board));
    }
    
    // Adds the rooks
    private void addRooks() {
        board[7][0].placePiece(new LeftRook("a" + 1, WHITE, board));
        board[0][0].placePiece(new LeftRook("a" + 8, BLACK, board));
        board[7][7].placePiece(new RightRook("h" + 1, WHITE, board));
        board[0][7].placePiece(new RightRook("h" + 8, BLACK, board));
    }
    
    // Returns the current player
    public String getPlayer() {
        // If we are white
        if (curr == WHITE) {
            return "White";
        }
        return "Black";
    }
    
    // Returns the opposite player
    public String getOtherPlayer() {
        // If we are white
        if (curr == WHITE) {
            return "Black";
        }
        return "White";
    }
    
    // Returns the current color
    public char getColor() {
        return curr;
    }
    
    // Returns the number of moves that have been played
    // One move consists of one turn from black and white
    public int getMoves() {
        return moves;
    }
    
    // Returns the piece given a position
    // Pre: position is valid
    // If empty tile return null
    public ChessPiece getPiece(String pos) {
        int row = SIZE - (pos.charAt(1) - '0');
        int col = pos.charAt(0) - 'a';
        return board[row][col].getPiece();
    }
    
    /**
     * 
     * Pre: piece must exist and the destination coordinate must not contain a
     * piece of our own color
     * @return true if the move has been made, false otherwise
     */
    public boolean makeMove(int row, int col, int destRow, int destCol) {
        // The piece exists and is the player's color
        if (!board[row][col].isEmpty() && board[row][col].getPiece().getColor() == curr) {
            // Changes player and increments move
            changeCurr();
            moves++;
            ChessPiece pieceToMove = board[row][col].getPiece();
            // Checks to see if the move is legal
            String move = (char)(destCol + 'a') + "" + Math.abs(destRow - SIZE);
            boolean legal = pieceToMove.move(move);
            // If the move is not legal, check to see if it is a special case,
            // and if it is, then add move. Or the move is legal
            if (legal) { 
                proceedGame(row, col, destRow, destCol, pieceToMove);
                return true;
            } else if (isSpecialCase()) {
                
            }
        }
        return false;
    }
    
    // Modifies the board based on a legal move
    private void proceedGame(int row, int col, int destRow, int destCol, ChessPiece pieceToMove) {
        changeBoard(row, col, destRow, destCol, pieceToMove);
        promotion = checkPromotion(pieceToMove, destRow);
        // Recalculate the legal move for all pieces based on the new board
        recalculateMove();
    }
    
    // Returns if a pawn can be promoted
    public boolean promotionLegal() {
        return promotion;
    }
    
    // Makes a promotion and change the board accordingly
    public void makePromotion(int choice, String pos) {
        int indexRow = SIZE - (pos.charAt(1) - '0');
        int indexCol = pos.charAt(0) - 'a';
        if (choice == 1) { // Promote to queen
            board[indexRow][indexCol].placePiece(new Queen(pos, curr, board));
        } else if (choice == 2) { // Promote to knight
            board[indexRow][indexCol].placePiece(new Knight(pos, curr, board));
        } else if (choice == 3) { // Promote to bishop
            board[indexRow][indexCol].placePiece(new Bishop(pos, curr, board));
        } else { // Promote to rook
            Rook temp = new LeftRook(pos, curr, board);
            temp.changeMoved();
            board[indexRow][indexCol].placePiece(temp);
        }
    }
    
    // Recalculate the possible moves
    private void recalculateMove() {
        // Loop through board
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                // If the tile is not empty
                if (!board[i][j].isEmpty()) { 
                    // Find all moves again
                    board[i][j].getPiece().findAllMoves();
                }
            }
        }
    }
    
    // Checks to see if the move is a special case
    // The special cases are castle or en passant
    private boolean isSpecialCase() {
        return false;
    }
    
    // Changes the board based on the move
    private void changeBoard(int row, int col, int destRow, int destCol, ChessPiece pieceToMove) {
        // Make a copy for the undo option
        makeCopy(board);
        // Move the piece to the destination tile
        board[destRow][destCol].removePiece();
        board[destRow][destCol].placePiece(pieceToMove);
        // Changes the row and column positions of the current piece
        pieceToMove.changeRow(destRow);
        pieceToMove.changeColumn((char)(destCol + 'a'));
        // If the current piece is pawn, king, or rook 
        changeMoved(destRow, destCol);
        // Remove the piece from the old tile
        board[row][col].removePiece();
    }
    
    // Changes the movement status
    private void changeMoved(int row, int col) {
        // If pawn
        if (board[row][col].getPiece() instanceof Pawn) {
            ((Pawn) board[row][col].getPiece()).changeMoved();
        }
        // If Rook
        if (board[row][col].getPiece() instanceof Rook) {
            ((Rook) board[row][col].getPiece()).changeMoved();
        }
        // If King
        if (board[row][col].getPiece() instanceof King) {
            ((King) board[row][col].getPiece()).changeMoved();
        }
    }
    
    // Make a copy of the previous board state
    private Tile[][] makeCopy(Tile[][] board) {
        Tile[][] prevBoard = new Tile[SIZE][SIZE];
        createTiles(prevBoard);
        // Loop through board
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                // If the tile is not empty
                if (!board[i][j].isEmpty()) {
                    // Place the piece in
                    prevBoard[i][j].placePiece(board[i][j].getPiece());
                }
            }
        }
        return prevBoard;
    }
    
    // Get the previous board
    public ChessBoard getPrevBoard() {
        return new ChessBoard(prevBoard);
    }
    
    // Changes the current player
    private void changeCurr() {
        // If we are currently white
        if (curr == WHITE) {
            curr = BLACK;
        } else {
            curr = WHITE;
        }
    }
    
    // Checks to see if the pawn can be promoted
    public boolean checkPromotion(ChessPiece p, int row) {
        // Given a row and a piece
        if (p instanceof Pawn) { // If the piece is a pawn
            return (p.getColor() == WHITE && row == 0) || (p.getColor() == BLACK && row == 7);
        }
        return false;
    }
    
    // Checks to see if we are checkMate
    public boolean checkMate() {
        // We know this piece is a King
        King temp = findKing();
        System.out.println(temp.possibleMoves.size());
        checkMate = temp.isInCheck() && temp.possibleMoves.size() == 0;
        return checkMate;
    }
    
    // Gets checkMate without a parameter
    public boolean isCheckMate() {
        return checkMate;
    }
    
    // Checks to see if we are in stale mate
    public boolean staleMate() {
        // When the current player only has the King and it cannot move
        return getNumberOfPieces() == 1 && findKing().getAllMoves().size() == 0;
    }
    
    // Get the number of pieces the current player has
    private int getNumberOfPieces() {
        // Loop through the board and find all pieces matching the current player
        int count = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                // If the tile is not empty and the piece at the tile is the player's color
                if (!board[i][j].isEmpty() && board[i][j].getPiece().getColor() == curr) {
                    count++;
                }
            }
        }
        return count;
    }
    
    // Find the King of the current player
    private King findKing() {
        // Loop through the board and find the King of the player
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                // If the tile is not empty and the piece at the tile is the player's color
                if (!board[i][j].isEmpty() && board[i][j].getPiece().getColor() == curr) {
                    // if the piece is an instance of king, return it
                    if (board[i][j].getPiece() instanceof King) {
                        return (King) board[i][j].getPiece();
                    }
                }
            }
        }
        return null; // We will never get to this part
    }
    
    // Prints out the board
    public void printBoard() {
        // Prints out different board perspectives depending on the current player
        if (curr == 'w') { // White's turn
            printWhiteSide();
        } else {
            printBlackSide();
        }
    }
    
    // Prints out the game board from white's point of view
    private void printWhiteSide() {
        // Prints out the column numbers
        char fir = 'a';
        System.out.printf("%9s", fir);
        for (int i = 1; i < SIZE; i++) {
            fir++; // Increments letter by 1
            System.out.printf("%10s", fir);
        }
        System.out.println();
        int tempIndex = SIZE;
        // Loop through the board
        for (int row = 0; row < SIZE; row++) {
            System.out.print(tempIndex + "  ");
            tempIndex--;
            for (int col = 0; col < SIZE; col++) {
                // Prints out the tile and the piece inside it
                Tile temp = board[row][col];
                temp.printTile();
            }
            System.out.println("\n");
        }
    }
    
    // Prints out the game board from the black's point of view
    private void printBlackSide() {
        // Prints out the column numbers
        char fir = 'a';
        System.out.printf("%8s", fir);
        for (int i = 1; i < SIZE; i++) {
            fir++; // Increments letter by 1
            System.out.printf("%10s", fir);
        }
        System.out.println();
        int tempIndex = 0;
        // Loop through the board
        for (int row = SIZE - 1; row >= 0; row--) {
            System.out.print(tempIndex + 1 + " ");
            tempIndex++;
            for (int col = 0; col < SIZE; col++) {
                // Prints out the tile and the piece inside it
                Tile temp = board[row][col];
                System.out.print(temp.toString());
            }
            System.out.println("\n");
        }
    }
}