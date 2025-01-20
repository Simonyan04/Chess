package source.core;

/**
 * The class Queen is a child class of piece which represents a Queen piece in a chess game and provides a utility method to determine the positions
 * reachable by a queen from a given position on the chessboard.
 */
public class Queen extends Piece{
    private static final long STANDARD_WHITE_POSITIONING = 8L;
    private static final long STANDARD_BLACK_POSITIONING = 576460752303423488L;
    public Queen(){
        super();
    }
    /**
     *The method is the overridden toString method of the class Object.
     * @return either String "♛" or "♕" depending on the color of the object.
     */
    public Queen(Chess.PieceColor color){
        super(color.equals(Chess.PieceColor.WHITE) ? new Positions(STANDARD_WHITE_POSITIONING) : new Positions(STANDARD_BLACK_POSITIONING), color, 900);
    }
    public String toString() {
        if (this.getPieceColor() == Chess.PieceColor.BLACK){
            return BLACK_QUEEN;
        }
        return WHITE_QUEEN;
    }

    public Positions allDestinations(Chess chess, Position p) {
        Positions result = new Positions();
        result.add(Rook.reachablePositions(chess, p));
        result.add(Bishop.reachablePositions(chess, p));
        return result;
    }

    @Override
    public int[] getBonusTable() {
        return new int[]{
        -20, -10, -10,  -5,  -5, -10, -10, -20,
        -10,   0,   0,   0,   0,   0,   0, -10,
        10,   0,   5,   5,   5,   5,   0, -10,
        -5,   0,   5,   5,   5,   5,   0,  -5,
        0,   0,   5,   5,   5,   5,   0, -10,
        -10,   5,   5,   5,   5,   5,   0, -10,
        -10,   0,   5,   0,   0,   0,   0, -10,
        -20, -10, -10,  -5,  -5, -10, -10, -20
        };
    }
}
