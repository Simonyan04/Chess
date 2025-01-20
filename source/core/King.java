package source.core;

public class King extends Piece{
    private static final long STANDARD_WHITE_POSITIONING = 16L;
    private static final long STANDARD_BLACK_POSITIONING = 1152921504606846976L;
    private boolean hasMoved;
    private static int[] KING_DEBUT_TABLE = {
            -30, -40, -40, -50, -50, -40, -40, -30,
            -30, -40, -40, -50, -50, -40, -40, -30,
            -30, -40, -40, -50, -50, -40, -40, -30,
            -30, -40, -40, -50, -50, -40, -40, -30,
            -20, -30, -30, -40, -40, -30, -30, -20,
            -10, -20, -20, -20, -20, -20, -20, -10,
            20,   20,   0,   0,   0,   0,  20,  20,
            20,   30,  10,   0,   0,  10,  30,  20
    };
    private static int[] KING_ENDGAME_TABLE = {
            -50, -30, -30, -30, -30, -30, -30, -50,
            -30, -10, 0, 0, 0, 0, -10, -30,
            -30, 0, 10, 15, 15, 10, 0, -30,
            -30, 5, 15, 20, 20, 15, 5, -30,
            -30, 0, 15, 20, 20, 15, 0, -30,
            -30, 0, 10, 15, 15, 10, 0, -30,
            -30, -20, 0, 0, 0, 0, -20, -30,
            -50, -40, -30, -20, -20, -30, -40, -50
    };


    public King() {
        this(Chess.PieceColor.WHITE);
    }
    public King(Chess.PieceColor color) {
        this(color, false);
    }
    public King(Chess.PieceColor color, boolean hasMoved) {
        super(color.equals(Chess.PieceColor.WHITE) ? new Positions(STANDARD_WHITE_POSITIONING) : new Positions(STANDARD_BLACK_POSITIONING), color,0);
        this.hasMoved = hasMoved;
    }
    public String toString() {
        if (this.getPieceColor() == Chess.PieceColor.BLACK){
            return BLACK_KING;
        }
        return WHITE_KING;
    }
    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public boolean hasMoved() {
        return hasMoved;
    }
    public Positions allDestinations(Chess chess, Position p) {
        int[] rankOffsets = {1, 1, 1, 0, 0, -1, -1, -1};
        int[] fileOffsets = {0, 1, -1, 1, -1, 0, 1, -1};
        Positions result = new Positions();
        for (int d = 0; d < 8; d++) {
            Position current = p.shift(rankOffsets[d], fileOffsets[d]);
            if (current != null && (chess.isEmpty(current) || (chess.getPieceColorAt(p) != chess.getPieceColorAt(current)))) {
                result.add(current);
            }
        }

        return result;
    }
    @Override
    public double getScore(double ratio) {
        double result = 0;
        boolean isWhite = getPieceColor().equals(Chess.PieceColor.WHITE);

        if(hasMoved) result = isWhite ? -50 : 50;

        int zeroesCount = Long.numberOfTrailingZeros(getPositions().first().getPosition());
        int index = isWhite ? (63 - zeroesCount) : zeroesCount;

        double bonus = ratio * KING_DEBUT_TABLE[index] + (1-ratio) * KING_ENDGAME_TABLE[index];

        if(!isWhite) bonus = -bonus;
        return result + bonus;
    }

    @Override
    public int[] getBonusTable() {
        return KING_DEBUT_TABLE;
    }
}
