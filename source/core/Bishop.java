package source.core;

public class Bishop extends Piece {
    private static final long STANDARD_WHITE_POSITIONING = 36L;
    private static final long STANDARD_BLACK_POSITIONING = 2594073385365405696L;
    public Bishop(){
        super();
    }
    public Bishop(Chess.PieceColor color){
        super(color.equals(Chess.PieceColor.WHITE) ? new Positions(STANDARD_WHITE_POSITIONING) : new Positions(STANDARD_BLACK_POSITIONING), color, 330);
    }
    public String toString() {
        if (this.getPieceColor() == Chess.PieceColor.BLACK) {
            return BLACK_BISHOP;
        }
        return WHITE_BISHOP;
    }
    public Positions allDestinations(Chess chess, Position p) {
        return Bishop.reachablePositions(chess, p);
    }
    public static Positions reachablePositions(Chess chess, Position p) {
        int[] rankOffsets = {1, 1, -1, -1};
        int[] fileOffsets = {1, -1, 1, -1};
        Positions result = new Positions();
        for (int d = 0; d < 4; d++) {
            Position current = p.shift(rankOffsets[d], fileOffsets[d]);
            while (current != null) {
                if (chess.isEmpty(current))
                    result.add(current);
                else {
                    if ((chess.getPieceColorAt(p) != chess.getPieceColorAt(current)))
                        result.add(current);
                    break;
                }
                current = current.shift(rankOffsets[d], fileOffsets[d]);
            }
        }
        return result;
    }

    @Override
    public int[] getBonusTable() {
        return new int[]{
                -20, -10, -10, -10, -10, -10, -10, -20,
                -10,   5,   0,   0,   0,   0,   5, -10,
                -10,  10,  10,  10,  10,  10,  10, -10,
                -10,   0,  10,  10,  10,  10,   0, -10,
                -10,   5,   5,  10,  10,   5,   5, -10,
                -10,   0,   5,  10,  10,   5,   0, -10,
                -10,   0,   0,   0,   0,   0,   0, -10,
                -20, -10, -10, -10, -10, -10, -10, -20
        };
    }
}