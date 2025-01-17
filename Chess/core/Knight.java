package Chess.core;

/**
 * The class Knight is a child class of piece which represents a knight piece in a chess game and provides a utility method to determine the positions
 * reachable by a knight from a given position on the chessboard.
 */
public class Knight extends Piece {
    private static final long STANDARD_WHITE_POSITIONING = 66L;
    private static final long STANDARD_BLACK_POSITIONING = 4755801206503243776L;
    public Knight(){
        super();
    }

    public Knight(Chess.PieceColor color){
        super(color.equals(Chess.PieceColor.WHITE) ? new Positions(STANDARD_WHITE_POSITIONING) : new Positions(STANDARD_BLACK_POSITIONING), color,  3);
    }
    /**
     * Calculates the positions a knight can reach from a given starting position, considering the chessboard's current state.
     * A knight can move in an "L" shape: two squares in one direction and then one square perpendicular to that, or vice versa.
     * This method takes into account the positions occupied by other pieces and ensures the knight only moves to valid squares.
     * A position is considered valid if it is within the bounds of the chessboard and either empty or occupied by an opponent's piece.
     *
     * @param board The current state of the chess game, used to check the positions of other pieces on the board.
     * @param p     The starting position of the knight.
     * @return An array of am.aua.chess.core.Position objects representing all possible positions the knight can move to from its starting position.
     */
    public Positions allDestinations(Board board, Position p) {
        Positions result = new Positions();
        for (int i = -1; i < 2; i += 2) {
            for (int j = -2; j < 3; j += 4) {
                Position currentPosition = p.shift(i, j);
                if(currentPosition == null) continue;
                if (board.isEmpty(currentPosition) || (board.getPieceAt(p).getPieceColor() != board.getPieceAt(currentPosition).getPieceColor())){
                    result.add(currentPosition);
                }
            }
        }
        for (int i = -2; i < 3; i += 4) {
            for (int j = -1; j < 2; j += 2) {
                Position currentPosition = p.shift(i, j);
                if(currentPosition == null) continue;
                if (board.isEmpty(currentPosition) || (board.getPieceAt(p).getPieceColor() != board.getPieceAt(currentPosition).getPieceColor())){
                    result.add(currentPosition);
                }
            }
        }
        return result;
    }
    /**
     *The method is the overriden toString method of the class Object.
     * @return Either String "N" or "n" depending on the color of the object.
     */
    public String toString(){
        if(this.getPieceColor() == Chess.PieceColor.BLACK){
            return BLACK_KNIGHT;
        }
        return WHITE_KNIGHT;
    }
}
