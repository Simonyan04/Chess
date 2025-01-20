package source.core;

public class Pawn extends Piece{
    private static final long STANDARD_WHITE_POSITIONING = 65280L;
    private static final long STANDARD_BLACK_POSITIONING = 71776119061217280L;

    public Pawn(Chess.PieceColor color){
        super(color.equals(Chess.PieceColor.WHITE) ? new Positions(STANDARD_WHITE_POSITIONING) : new Positions(STANDARD_BLACK_POSITIONING), color, 100);
    }
    public String toString() {
        if (this.getPieceColor() == Chess.PieceColor.BLACK)
            return BLACK_PAWN;
        return WHITE_PAWN;
    }
    public Positions allDestinations(Chess chess, Position p) {
        Positions result = positionsUnderPawnAttack(chess, p);
        if(chess.getPieceColorAt(p) == Chess.PieceColor.WHITE){
            Position oneStep = p.shift(0, 1);
            Position twoStep = p.shift(0,2);
            if(oneStep != null && chess.isEmpty(oneStep)){
                result.add(oneStep);
                if(twoStep != null && p.shift(0,-2) == null && chess.isEmpty(twoStep)){
                    result.add(twoStep);
                }
            }
        }
        else{
            Position oneStep = p.shift(0, -1);
            Position twoStep = p.shift(0,-2);
            if(oneStep != null && chess.isEmpty(oneStep)){
                result.add(oneStep);
                if(twoStep != null && p.shift(0,2) == null && chess.isEmpty(twoStep)){
                    result.add(twoStep);
                }
            }
        }

        return result;
    }

    @Override
    public int[] getBonusTable() {
        return new int[]{
                0, 0, 0, 0, 0, 0, 0, 0,
                5, 10, 10, -20, -20, 10, 10, 5,
                5, -5, -10, 0, 0, -10, -5, 5,
                0, 0, 0, 20, 20, 0, 0, 0,
                5, 5, 10, 25, 25, 10, 5, 5,
                10, 10, 20, 30, 30, 20, 10, 10,
                50, 50, 50, 50, 50, 50, 50, 50,
                0, 0, 0, 0, 0, 0, 0, 0};
    }

    public Positions positionsUnderPawnAttack(Chess chess, Position p){
        Positions positions = new Positions();
        if(chess.getPieceColorAt(p) == Chess.PieceColor.WHITE){
            Position hitRight = p.shift(1, 1);
            Position hitLeft = p.shift(-1,1);
            if(hitRight != null && !chess.isEmpty(hitRight) && chess.getPieceColorAt(hitRight) != Chess.PieceColor.WHITE){
                positions.add(hitRight);
            }
            if(hitLeft != null && !chess.isEmpty(hitLeft) && chess.getPieceColorAt(hitLeft) != Chess.PieceColor.WHITE){
                positions.add(hitLeft);
            }
        }
        else{
            Position hitRight = p.shift(-1, -1);
            Position hitLeft = p.shift(1, -1);
            if(hitRight != null && !chess.isEmpty(hitRight) && chess.getPieceColorAt(hitRight) == Chess.PieceColor.WHITE){
                positions.add(hitRight);
            }
            if(hitLeft != null && !chess.isEmpty(hitLeft) && chess.getPieceColorAt(hitLeft) == Chess.PieceColor.WHITE){
                positions.add(hitLeft);
            }
        }
        return positions;
    }
}
