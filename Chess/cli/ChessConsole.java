package Chess.cli;
import Chess.core.*;
import Chess.exceptions.IllegalArrangementException;
import Chess.puzzles.Puzzle;
import Chess.puzzles.PuzzleDatabase;
import Chess.ui.ChessUI;

import java.util.Scanner;
/**
 * The ChessConsole class provides a text-based console interface for playing a chess game.
 * It uses an instance of the Chess class to manage the game logic and processes user input
 * to perform moves. Moves are entered via standard input in a specific format, and the game state
 * is printed to standard output after each move. The game continues until a checkmate or a stalemate
 * is reached.
 */

public class ChessConsole {
    public static final String RED = "\u001B[31m";
    public static final String Blue = "\u001B[34m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RESET = "\u001B[0m";

    private Chess game;
    private PuzzleDatabase database;

    public ChessConsole(){
        database = new PuzzleDatabase();
    }
    public void play() {
        Scanner sc = new Scanner(System.in);
        String inputLine;
        print();
        while (!game.isGameOver()) {
            if (game.getTurn() == Chess.PieceColor.WHITE) {
                System.out.println("White’s move: ");
            } else {
                System.out.println("Black’s move: ");
            }
            inputLine = sc.nextLine();
            String[] input = inputLine.split(" ");
            if (input[0].equalsIgnoreCase("resign")) {
                System.out.println(game.getTurn() + " has resigned.");
                return;
            }
            if (input[0].equalsIgnoreCase("debug")) {
                debug();
                print();
                continue;
            }
            Position origin = Position.generateFromString(input[0]);
            if(!(origin == null || game.isEmpty(origin) || game.getPieceAt(origin).getPieceColor() != game.getTurn())){
                if (input.length == 1) {
                    print(Position.generateFromString(input[0]));
                }
                else if (input.length == 2) {
                    Move m = new Move(Position.generateFromString(input[0]),
                            Position.generateFromString(input[1]));
                    boolean success = game.performMove(m);
                    if (!success) {
                        System.out.println("Invalid move. Please try again.");
                    }
                    print();
                }
            }
            else{
                System.out.println("Invalid input");
                print();
            }
        }
    }
    private void print(Position position) {
        Positions reachablePositions = game.reachableFrom(position);
        Board board = game.getBoard();
        for (int i = Chess.ROW - 1; i >= 0; i--) {
            for (int j = 0; j < Chess.COLUMN; j++) {
                if (shouldHighlight(i, j, reachablePositions)) {
                    if (board.getPieceAt(new Position(i, j)) == null){
                        System.out.print(RED + "." + RESET);
                        continue;
                    }
                    System.out.print(RED + board.getPieceAt(new Position(i, j)) + RESET);
                }
                else if(i == position.getY() && j == position.getX()){
                    System.out.print(Blue + board.getPieceAt(new Position(i, j)) + RESET);
                }
                else {
                    if (board.getPieceAt(new Position(i, j)) == null){
                        System.out.print("  ");
                        continue;
                    }
                    System.out.print(board.getPieceAt(new Position(i, j)));
                }
            }
            System.out.println();
        }
    }
    public void run(){
        Scanner sc = new Scanner(System.in);
        String inputLine;
        System.out.println("Welcome to Chess Console!");
        printInstructions();
        inputLine = sc.nextLine();
        while (!inputLine.equals("q")){
            try {
                if (inputLine.equals("debug")){
                    debug();
                }
                if(inputLine.equals("p")) {
                    game = new Chess();
                    play();
                }
                else if (inputLine.equals("l")) {
                    int databaseSize = database.getSize();
                    for (int i = 0; i < databaseSize; i++)
                        System.out.println (i + ": " + database.getPuzzle(i) + "\n");
                }
                else if (inputLine.startsWith("a"))
                    database.addPuzzlesFromFile(inputLine.substring(2));
                else if (inputLine.startsWith("p")) {
                    int puzzleNumber = Integer.parseInt(inputLine.substring(2));
                    Puzzle puzzle = database.getPuzzle(puzzleNumber);
                    game = new Chess(puzzle.getArrangement(), puzzle.getTurn());
                    play();
                }
                else
                    System.out.println("Unknown instruction. Please try again.");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            printInstructions();
            inputLine = sc.nextLine();
        }
        database.save();
    }
    private void printInstructions () {
        System.out.println ("Input ’p’ to play chess.");
        System.out.println ("Input ’l’ to list the puzzles in the database.");
        System.out.println ("Input ’a <filename>’ to add new puzzles into the database.");
        System.out.println("Input ’p <number>’ to play a puzzle.");
        System.out.println("If you want to end the program, input ’q’.");
    }

    private void print() {
        Board board = game.getBoard();
        for (int i = game.ROW - 1; i >= 0; i--) {
            for (int j = 0; j < game.COLUMN; j++) {
                if (board.getPieceAt(new Position(i, j)) == null){
                    System.out.print("  ");
                    continue;
                }
                System.out.print(board.getPieceAt(new Position(i, j)));
            }
            System.out.println();
        }
    }

    private boolean shouldHighlight(int i, int j, Positions reachablePositions) {
        for (Position p : reachablePositions) {
            if (p.getY() == i && p.getX() == j) {
                return true;
            }
        }
        return false;
    }
    public void debug() {
//        System.out.println("This method is for testing purposes.");
//        System.out.println(Arrays.toString(game.getAllDestinationsByColor(Chess.PieceColor.WHITE)));
        try {
            ChessUI play = new ChessUI();
        } catch (IllegalArrangementException e) {
            throw new RuntimeException(e);
        }

    }
}
