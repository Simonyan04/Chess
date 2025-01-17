package Chess.ui;
import Chess.core.Piece;
import javax.swing.*;
import java.awt.*;

public class BoardSquare extends JButton {
    private static final Color RED_COLOR = new Color(255, 0, 0);
    private static final Color LIGHT_COLOR = new Color(255, 255, 255);
    private static final Color DARK_COLOR = new Color(78,120,55);
    private int x;
    private int y;
    private Color color;

    public BoardSquare(boolean isWhite, int x, int y){
        this.x = x;
        this.y = y;
        this.color = isWhite ? LIGHT_COLOR : DARK_COLOR;
    }
    public int[] getCoordinates(){
        return new int[]{x, y};
    }
    public Color getColor(){
        return color;
    }
    public void setPiece(String piece){
        setIcon(new ImageIcon(getPngPath(piece)));
    }
    public void setPiece(Piece piece){
        if (piece == null){
            setPiece((String) null);
        }
        else{
            setPiece(piece.toString());
        }
    }
    public void setPiece(){
        setIcon(null);
    }
    public void setHighlight(boolean highlight){
        if (highlight) color = RED_COLOR;
    }
    private String getPngPath(String piece){
        StringBuilder builder = new StringBuilder();
        builder.append("Chess/ui/gfx/");
        if (piece == null){
            return null;
        }
        switch (piece) {
            case "n" -> builder.append("KnightB");
            case "N" -> builder.append("KnightW");
            case "R", "S" -> builder.append("RookW");
            case "r", "s" -> builder.append("RookB");
            case "K", "L" -> builder.append("KingW");
            case "k", "l" -> builder.append("KingB");
            case "Q" -> builder.append("QueenW");
            case "q" -> builder.append("QueenB");
            case "P" -> builder.append("PawnW");
            case "p" -> builder.append("PawnB");
            case "B" -> builder.append("BishopW");
            case "b" -> builder.append("BishopB");
        }
        builder.append(".png");
        return builder.toString();
    }
}
