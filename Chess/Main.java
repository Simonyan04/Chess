package Chess;

import Chess.core.Chess;
import Chess.exceptions.IllegalArrangementException;
import Chess.ui.ChessUI;
import Chess.cli.ChessConsole;


public class Main {
    public static void main(String[] args) {
        if ((args.length != 0 && args[0].equals("-console"))) {
            ChessConsole game = new ChessConsole();
            game.run();
        } else {
            javax.swing.SwingUtilities.invokeLater(() -> {
                try {
                    int choice = javax.swing.JOptionPane.showConfirmDialog(
                            null,
                            "Do you want to play against AI?",
                            "Game Mode Selection",
                            javax.swing.JOptionPane.YES_NO_OPTION);
                    Chess game;
                    if (choice == javax.swing.JOptionPane.YES_OPTION) {
                        System.out.println("AI is not finished yet");
                        game = new Chess();
                        //game = new ChessAI();
                    } else {
                        game = new Chess();
                    }
                    new ChessUI(game);
                } catch (IllegalArrangementException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
