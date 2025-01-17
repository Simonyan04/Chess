package Chess.core;

public class Rook extends Piece {
    private static final long STANDARD_WHITE_POSITIONING = 129L;
    private static final long STANDARD_BLACK_POSITIONING = -9151314442816847872L;
    private boolean hasMoved;
    public Rook() {
        this(Chess.PieceColor.WHITE);
    }
    public Rook(Chess.PieceColor color) {
        this(color, false);
    }
    public Rook(Chess.PieceColor color, boolean hasMoved) {
        super(color.equals(Chess.PieceColor.WHITE) ? new Positions(STANDARD_WHITE_POSITIONING) : new Positions(STANDARD_BLACK_POSITIONING), color, 5);
        this.hasMoved = hasMoved;
    }

    public String toString() {
        if (this.getPieceColor() == Chess.PieceColor.BLACK){
            return BLACK_ROOK;
        }
        return WHITE_ROOK;
    }
    public boolean hasMoved() {
        return hasMoved;
    }
    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public Positions allDestinations(Board board, Position p) {
        return Rook.reachablePositions(board, p);
    }

    public static Positions reachablePositions(Board board, Position p) {
        int[] rankOffsets = {1, -1, 0, 0};
        int[] fileOffsets = {0, 0, 1, -1};
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