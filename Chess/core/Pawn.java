package Chess.core;

public class Pawn extends Piece{
    private static final long STANDARD_WHITE_POSITIONING = 65280L;
    private static final long STANDARD_BLACK_POSITIONING = 71776119061217280L;

    public Pawn(){
        super();
    }

    public Pawn(Chess.PieceColor color){
        super(color.equals(Chess.PieceColor.WHITE) ? new Positions(STANDARD_WHITE_POSITIONING) : new Positions(STANDARD_BLACK_POSITIONING), color, 1);
    }
    public String toString() {
        if (this.getPieceColor() == Chess.PieceColor.BLACK)
            return BLACK_PAWN;
        return WHITE_PAWN;
    }
    public Positions allDestinations(Board board, Position p) {
        Positions result = positionsUnderPawnAttack(board, p);
        if(board.getPieceColorAt(p) == Chess.PieceColor.WHITE){
            Position oneStep = p.shift(0, 1);
            Position twoStep = p.shift(0,2);
            if(oneStep != null && board.isEmpty(oneStep)){
                result.add(oneStep);
                if(twoStep != null && p.shift(0,-2) == null && board.isEmpty(twoStep)){
                    result.add(twoStep);
                }
            }
        }
        else{
            Position oneStep = p.shift(0, -1);
            Position twoStep = p.shift(0,-2);
            if(oneStep != null && board.isEmpty(oneStep)){
                result.add(oneStep);
                if(twoStep != null && p.shift(0,2) == null && board.isEmpty(twoStep)){
                    result.add(twoStep);
                }
            }
        }

        return result;
    }
    public Positions positionsUnderPawnAttack(Board board, Position p){
        Positions positions = new Positions();
        if(board.getPieceColorAt(p) == Chess.PieceColor.WHITE){
            Position hitRight = p.shift(1, 1);
            Position hitLeft = p.shift(-1,1);
            if(hitRight != null && !board.isEmpty(hitRight) && board.getPieceColorAt(hitRight) != Chess.PieceColor.WHITE){
                positions.add(hitRight);
            }
            if(hitLeft != null && !board.isEmpty(hitLeft) && board.getPieceColorAt(hitLeft) != Chess.PieceColor.WHITE){
                positions.add(hitLeft);
            }
        }
        else{
            Position hitRight = p.shift(-1, -1);
            Position hitLeft = p.shift(1, -1);
            if(hitRight != null && !board.isEmpty(hitRight) && board.getPieceColorAt(hitRight) == Chess.PieceColor.WHITE){
                positions.add(hitRight);
            }
            if(hitLeft != null && !board.isEmpty(hitLeft) && board.getPieceColorAt(hitLeft) == Chess.PieceColor.WHITE){
                positions.add(hitLeft);
            }
        }
        return positions;
    }
}
