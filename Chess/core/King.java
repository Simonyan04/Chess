package Chess.core;

public class King extends Piece{
    private static final long STANDARD_WHITE_POSITIONING = 16L;
    private static final long STANDARD_BLACK_POSITIONING = 1152921504606846976L;
    private boolean hasMoved;
    public King() {
        this(Chess.PieceColor.WHITE);
    }

    /**
     * The constructor with only color input which initializes King object as a given color piece that has not moved
     * @param color
     */
    public King(Chess.PieceColor color) {
        this(color, false);
    }
    /**
     * The constructor with both color and hasMoved inputs which initializes King object as a given color piece that has either moved or not
     * @param color
     * @param hasMoved
     */
    public King(Chess.PieceColor color, boolean hasMoved) {
        super(color.equals(Chess.PieceColor.WHITE) ? new Positions(STANDARD_WHITE_POSITIONING) : new Positions(STANDARD_BLACK_POSITIONING), color,0);
        this.hasMoved = hasMoved;
    }
    /**
     *The method is the overridden toString method of the class Object.
     * @return either String "♔" or "♚" depending on the color of the object.
     */
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

    /**
     * Calculates the positions a King can reach from a given starting position, considering the chessboard's current state.
     *
     * @param board The current state of the chess game, used to check the positions of other pieces on the board.
     * @param p     The starting position of the King.
     * @return An array of am.aua.chess.core.Position objects representing all possible positions the knight can move to from its starting position.
     */
    public Positions allDestinations(Board board, Position p) {
        int[] rankOffsets = {1, 1, 1, 0, 0, -1, -1, -1};
        int[] fileOffsets = {0, 1, -1, 1, -1, 0, 1, -1};
        Positions result = new Positions();
        for (int d = 0; d < 8; d++) {
            Position current = p.shift(rankOffsets[d], fileOffsets[d]);
            if (current != null && (board.isEmpty(current) || (board.getPieceColorAt(p) != board.getPieceColorAt(current)))) {
                result.add(current);
            }
        }

        return result;
    }
}
