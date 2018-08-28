// The main driver of the chess game
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import javax.swing.*;

@SuppressWarnings("serial")
public class ChessMain extends JFrame{
    
    // Class constants
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int SIZE = 8;
    
    public static void main(String[] args) {
        // Establish the JFrame
        setFrame();  
        intro();
        // Create an instance of the board and the scanner
        ChessBoard board = new ChessBoard();
        Scanner keyboard = new Scanner(System.in);
        displayBoard(board);
        int choice = displayPanel(board, keyboard);
        implementChoice(choice, board, keyboard);
        playAgain(keyboard);
    }
    
    // Sets up the Board frame
    public static void setFrame() {
        // Create the Frame
        JFrame frame = new JFrame("Chess");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = new Dimension((int)(screenSize.width * 2.0 / 3),(int)(screenSize.height * 2.0 / 3));
        int x=(int)(frameSize.width / 3);
        int y=(int)(frameSize.height / 3);
        frame.setBounds(x,y,800,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        // Establish the text area
        setTextArea(frame);
    }
    
    // Sets up the Text area
    public static void setTextArea(JFrame frame) {
        JTextArea textArea = new JTextArea(60,60);
        // Redirect Sysout to Frame
        PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
        System.setOut(printStream);
        System.setErr(printStream);
        // Set up layout
        textArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(new JScrollPane(textArea),BorderLayout.CENTER);
        // Establish the JPanel
        JPanel panel = new JPanel();
        panel.add(textArea);
        panel.add(scroll);
        frame.getContentPane().add(panel);
    }
    
    // Asks the user if he or she wishes to play again
    public static void playAgain(Scanner keyboard) {
        // Obtain the user's choice
        System.out.println("\nWould you like to play again? ");
        System.out.print("Enter \'Y\' or \'y\' to play again, anything else if you wish to quit: ");
        String again = keyboard.nextLine();
        // if the user wants to play again
        while (again.equalsIgnoreCase("y")) {
            // Plays the game again
            ChessBoard board = new ChessBoard();
            displayBoard(board);
            int choice = displayPanel(board, keyboard);
            implementChoice(choice, board, keyboard);
            // Repeat
            System.out.println("Would you like to play again? ");
            System.out.print("Enter \'Y\' or \'y\' to play again, anything else if you wish to quit: ");
            again = keyboard.nextLine();
        }
    }
    
    // Undo the last move
    public static ChessBoard displayPrevBoard(ChessBoard board, Scanner keyboard) {
        // Make sure that the number of moves is greater than 0
        if (board.getMoves() > 0) {
            board = board.getPrevBoard();
            System.out.println("\nUndo successful\n");
        } else {
            System.out.println("\nUndo is NOT successful, there is nothing to undo.\n");
        }
        displayBoard(board);
        return board;
    }
    
    // Display all the possible moves of a given piece
    public static void displayMoves(ChessBoard board, Scanner keyboard) {
        // Get the position
        System.out.print("Please enter the position of the piece you wish to display: ");
        String pos = getPrevPos(board, keyboard, true);
        ChessPiece curr = board.getPiece(pos);
        System.out.println("The possible moves for this piece are: ");
        // Print out all of the possible moves this piece has
        System.out.println(curr.getAllMoves().toString());
        System.out.println();
        displayBoard(board);
    }
    
    // Plays the rounds of chess
    public static ChessBoard playGame(ChessBoard board, Scanner keyboard) {
        // Get the movement position and the destination
        System.out.print("Please enter the position of the piece you wish to move: ");
        String prevPos = getPrevPos(board, keyboard, true);
        System.out.print("\nPlease enter the position you wish your piece to move to: ");
        String dest = getPrevPos(board, keyboard, false);
        // Checks to see that the move is legal
        checkMove(prevPos, dest, board, keyboard);
        if (board.promotionLegal()) { // If a pawn can be promoted
            board = makePromotion(board, keyboard, dest);
        }
        displayBoard(board);
        // Checks to see if we have checkMate
        board.checkMate();
        return board;
    }
    
    // Makes a pawn promotion
    public static ChessBoard makePromotion(ChessBoard board, Scanner keyboard, String dest) {
        System.out.println("\nYou may promote your pawn!");
        System.out.println("1) Enter 1 to promote pawn to Queen.");
        System.out.println("2) Enter 2 to promote pawn to Knight.");
        System.out.println("3) Enter 3 to promote pawn to Bishop.");
        System.out.println("4) Enter 4 to promote pawn to Rook.");
        System.out.print("\nPlease enter your choice: ");
        String choice = keyboard.nextLine();
        // Checks to see the choice is legal
        while (choice.length() != 1 && ((choice.charAt(0) - '0') > 4 || (choice.charAt(0) - '0') < 1)) {
            System.out.println("Thats not a valid choice.");
            System.out.print("Please enter your choice: ");
            choice = keyboard.nextLine();
        }
        board.makePromotion(choice.charAt(0) - '0', dest);
        return board;
    }
    
    // Checks to see if the move is legal
    private static void checkMove(String prev, String dest, ChessBoard board, Scanner keyboard) {
        // Get the rows and columns
        int prevRow = SIZE - (prev.charAt(1) - '0');
        int prevCol = prev.charAt(0) - 'a';
        int destRow = SIZE - (dest.charAt(1) - '0');
        int destCol = dest.charAt(0) - 'a';
        // Check to see if the move is legal
        while (!board.makeMove(prevRow, prevCol, destRow, destCol)) {
            System.out.println("That is not a legal move!\n");
            System.out.print("Please enter the position of the piece you wish to move: ");
            prev = getPrevPos(board, keyboard, true);
            System.out.print("\nPlease enter the position you wish your piece to move to: ");
            dest = getPrevPos(board, keyboard, false);
            prevRow = SIZE - (prev.charAt(1) - '0');
            prevCol = prev.charAt(0) - 'a';
            destRow = SIZE - (dest.charAt(1) - '0');
            destCol = dest.charAt(0) - 'a';
        }
    }
    
    // Get the position of the piece the player wishes to move
    public static String getPrevPos(ChessBoard board, Scanner keyboard, boolean checkEmpty) {
        String pos = keyboard.nextLine();
        // Checks to see that the position is valid
        pos = checkPosition(board, pos, keyboard, checkEmpty);
        System.out.println("\nThe position you have entered is " + pos);
        return pos;
    }
    
    // Checks to see that the position the user entered is valid
    public static String checkPosition(ChessBoard board, String pos, Scanner keyboard, boolean checkEmpty) {
        // Make sure position is valid
        while (pos.length() != 2 || (pos.charAt(0) < 'a' || pos.charAt(0) > 'h') ||
                ((pos.charAt(1) - '0') < 1 || (pos.charAt(1) - '0') > 8)) {
            System.out.println("\nThat's not a valid position.");
            System.out.print("Please enter the position again: ");
            pos = keyboard.nextLine();
        }
        if (checkEmpty) {
            // Make sure the position contains the player's piece and is not empty
            while (board.getPiece(pos) == null || board.getPiece(pos).getColor() != board.getColor()
                    || board.getPiece(pos).getAllMoves().size() == 0) {
                System.out.println("\nThe tile is empty or the piece is not one of yours"
                        + " or the piece cannot currently be moved.\n");
                System.out.print("Please enter the position again: ");
                pos = keyboard.nextLine();
            }
        }
        return pos;
    }
    
    // Implement the game option based on the choice
    public static void implementChoice(int choice, ChessBoard board, Scanner keyboard) {
        // While the user has not entered quit
        while (choice != FOUR) {
            if (choice == ONE) { // The user wants to make a move
                board = playGame(board, keyboard);
                if (board.isCheckMate() || board.staleMate()) { // Game is over
                    break;
                }
            } else if (choice == TWO) { // The user wants to display all the moves of a piece
                displayMoves(board, keyboard);
            } else if (choice == THREE) { // The user wants to undo a move
                board = displayPrevBoard(board, keyboard);
            }
            choice = displayPanel(board, keyboard);
        }
        printResults(choice, board);
    }
    
    // Prints out the final results of the game
    public static void printResults(int choice, ChessBoard board) {
        // If the user has decided not to play anymore
        if (choice == FOUR) {
            System.out.println("The game has ended.");
        } else if (board.isCheckMate()) { // The game is over with a winner 
            System.out.println(board.getOtherPlayer() + " has won!");
            System.out.println("Here is the final board: ");
            board.printBoard();
        } else { // The game ended in a stale mate
            System.out.println("The game is a draw!");
            System.out.println("Here is the final board: ");
            board.printBoard();
        }
        System.out.println("Thank you for playing! ");
    }
    
    // Displays the gaming panel
    public static int displayPanel(ChessBoard board, Scanner keyboard) {
        // Display the panel
        System.out.println("\n1) Enter 1 to make a move.");
        System.out.println("2) Enter 2 to display all possible moves of a piece.");
        System.out.println("3) Enter 3 to undo the last move.");
        System.out.println("4) Enter 4 to quit the game.");
        System.out.print("\nPlease enter your choice: ");
        String choice = keyboard.nextLine();
        // Check that the choice is valid
        while (choice.length() != 1 || (choice.charAt(0) - '0' < 1 ) ||
                (choice.charAt(0) - '0' > 4)) {
            System.out.println("\nThats not a valid choice.");
            System.out.print("Please enter your choice: ");
            choice = keyboard.nextLine();
        }
        return choice.charAt(0) - '0';
    }
    
    // Displays the board
    public static void displayBoard(ChessBoard board) {
        System.out.println("\nIts " + board.getPlayer() + "\'s turn!");
        System.out.println("Number of current moves: " + board.getMoves());
        System.out.println("Here is the current board: \n");
        System.out.println();
        board.printBoard();
    }
    
    // Prints out the introduction
    public static void intro() {
        System.out.println("Hello! Welcome to Bohan's chess game!");
        System.out.println("This program simulates an ASCII version of chess");
        System.out.println("and is playable with two players. Enjoy!");
        System.out.println();
        System.out.println("To play, you will first enter the position of the piece");
        System.out.println("you wish to move, and then the destination position you wish");
        System.out.println("to move the piece to. However, in order for the game to work");
        System.out.println("You MUST enter the position in the form of column then row.");
        System.out.println("For instance, e4, d3, d5, are all valid moves.");
    }
}