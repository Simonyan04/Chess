package source.ui;

import source.core.*;
import source.exceptions.IllegalArrangementException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessUI extends JFrame {
    private static final int HEIGHT = 800;
    private static final int WIDTH = 800;
    private static final String TITLE = "Chess game";

    private Chess currentGame;
    private BoardSquare[][] boardSquares;
    private Position origin;

    public ChessUI(Chess game) throws IllegalArrangementException {
        this.currentGame = game;
        boardSquares = new BoardSquare[8][8];

        setSize(WIDTH, HEIGHT);
        setTitle(TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(8, 8));
        boardPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        for (int row = 7; row >= 0; row--) {
            for (int col = 0; col < 8; col++) {
                boolean isWhite = ((row + col) % 2 == 1);
                BoardSquare square = new BoardSquare(isWhite, col, row);

                square.setPiece(currentGame.getPieceAt(new Position(col, row)));

                boardSquares[row][col] = square;
                square.setBackground(square.getColor());
                square.setPreferredSize(new Dimension(100, 100));

                square.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        BoardSquare source = (BoardSquare) e.getSource();
                        int[] coordinates = source.getCoordinates();
                        boardClicked(coordinates);

                    }
                });

                boardPanel.add(square);
            }
        }
        add(boardPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    public ChessUI() throws IllegalArrangementException {
        this(new Chess());
    }

    private void boardClicked(int[] coordinates) {

        Position givenPosition = new Position(coordinates[0], coordinates[1]);
        if (origin == null) {
            origin = givenPosition;
            if (!currentGame.isEmpty(origin) && Chess.isRightTurn(currentGame, origin)) {
                Positions reachablePositions = currentGame.reachableFrom(origin);
                if (reachablePositions != null) {
                    for (Position pos : reachablePositions) {
                        if(currentGame.isLegalMove(new Move(origin, pos))){
                            int x = pos.getX();
                            int y = pos.getY();
                            boardSquares[y][x].setBackground(Color.RED);
                        }
                    }
                }
            } else {
                origin = null;
            }
        } else {
            Move move = new Move(origin, givenPosition);
            if (!origin.equals(givenPosition) && !currentGame.isEmpty(origin) && currentGame.isLegalMove(move)) {
                currentGame.performMove(move);
                updatePieces();
            }
            clearHighlights();
            origin = null;
            if(currentGame instanceof ChessAI && ((ChessAI) currentGame).isAITurn()) performAIMove();
        }
    }
    private void performAIMove(){
        SwingUtilities.invokeLater(() -> {
            Move aiMove = ((ChessAI) currentGame).minimax();
            if (aiMove != null) {
                currentGame.performMove(aiMove);
                updatePieces();
            }
        });
    }


    private void updatePieces() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = currentGame.getPieceAt(new Position(col, row));
                boardSquares[row][col].setPiece(piece);
                boardSquares[row][col].setBackground(boardSquares[row][col].getColor());
                boardSquares[row][col].repaint();
            }
        }
    }

    private void clearHighlights() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                boardSquares[row][col].setBackground(boardSquares[row][col].getColor());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                int choice = JOptionPane.showConfirmDialog(
                        null,
                        "Do you want to play against AI?",
                        "Game Mode Selection",
                        JOptionPane.YES_NO_OPTION);

                Chess game;
                if (choice == JOptionPane.YES_OPTION) {
                    game = new ChessAI();
                } else {
                    game = new Chess();
                }
                new ChessUI(game);
            } catch (IllegalArrangementException e) {
                e.printStackTrace();
            }
        });
    }
}
