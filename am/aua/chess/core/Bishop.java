package am.aua.chess.core;
import java.util.ArrayList;

public class Bishop extends Piece {
    private static final long STANDARD_WHITE_POSITIONING = 36L;
    private static final long STANDARD_BLACK_POSITIONING = 2594073385365405696L;
    public Bishop(){
        super();
    }
    public Bishop(Chess.PieceColor color){
        super(color.equals(Chess.PieceColor.WHITE) ? new Positions(STANDARD_WHITE_POSITIONING) : new Positions(STANDARD_BLACK_POSITIONING), color, 3);
    }
    public String toString() {
        if (this.getPieceColor() == Chess.PieceColor.BLACK) {
            return BLACK_BISHOP;
        }
        return WHITE_BISHOP;
    }
    public Positions allDestinations(Board board, Position p) {
        return Bishop.reachablePositions(board, p);
    }
    public static Positions reachablePositions(Board board, Position p) {
        int[] rankOffsets = {1, 1, -1, -1};
        int[] fileOffsets = {1, -1, 1, -1};
        Positions result = new Positions();
        for (int d = 0; d < 4; d++) {
            Position current = p.shift(rankOffsets[d], fileOffsets[d]);
            while (current != null) {
                if (board.isEmpty(current))
                    result.add(current);
                else {
                    if ((board.getPieceColorAt(p) != board.getPieceColorAt(current)))
                        result.add(current);
                    break;
                }
                current = current.shift(rankOffsets[d], fileOffsets[d]);
            }
        }
        return result;
    }
}