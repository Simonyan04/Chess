package Chess.core;
import Chess.exceptions.IllegalArrangementException;


public class ChessAI extends Chess {
    private PieceColor AIColor = PieceColor.BLACK;
    private static final int DEPTH = 4;

    public ChessAI() throws IllegalArrangementException {
        super();
    }

    public ChessAI(String positioning, PieceColor color) throws IllegalArrangementException {
        super(positioning, color);
    }

    public ChessAI(Chess that) {
        super(that);
    }

    public Move minimax() {
        int bestScore;
        Move bestMove = null;
        boolean maximizing = (getTurn() == PieceColor.WHITE);
        bestScore = maximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        for (Move move : board.getMovesByColor(getTurn())) {
            System.out.println(move);
            if(!isLegalMove(move)) continue;
            UndoData undoData = doMove(move);
            int score = minimaxScore(DEPTH - 1, !maximizing, alpha, beta);
            undoMove(undoData);

            if (maximizing && score > bestScore) {
                bestScore = score;
                bestMove = move;
                alpha = score;
            } else if (!maximizing && score < bestScore) {
                bestScore = score;
                bestMove = move;
                beta = score;
            }
            if (beta <= alpha) {
                break;
            }
        }
        System.out.println(bestMove);
        return bestMove;
    }

    private int minimaxScore(int depth, boolean maximizing, int alpha, int beta) {
        if (depth == 0 || isGameOver()) {
            return points();
        }
        if (maximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : board.getMovesByColor(getTurn())) {
                System.out.println(move);
                if(!isLegalMove(move)) continue;
                UndoData undoData = doMove(move);
                int eval = minimaxScore(depth - 1, false, alpha, beta);
                undoMove(undoData);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break;
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : board.getMovesByColor(getTurn())) {
                System.out.println(move);
                if(!isLegalMove(move)) continue;
                UndoData undoData = doMove(move);
                int eval = minimaxScore(depth - 1, true, alpha, beta);
                undoMove(undoData);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) break;
            }
            return minEval;
        }
    }

    private class UndoData {
        Move move;
        Piece movingPiece;
        Piece captured;
        boolean originalHasMoved;
        UndoData(Move move, Piece movingPiece, Piece captured, boolean originalHasMoved) {
            this.move = move;
            this.movingPiece = movingPiece;
            this.captured = captured;
            this.originalHasMoved = originalHasMoved;
        }
    }

    private UndoData doMove(Move move) {
        Position origin = move.getOrigin();
        Position destination = move.getDestination();
        Piece movingPiece = board.getPieceAt(origin);

//        System.out.println("doMove: " + movingPiece + " from " + origin + " to " + destination);
//        System.out.println("Before move, movingPiece positions: " + movingPiece.getPositions());

        boolean originalHasMoved = false;
        if (movingPiece instanceof King) {
            originalHasMoved = ((King) movingPiece).hasMoved();
        } else if (movingPiece instanceof Rook) {
            originalHasMoved = ((Rook) movingPiece).hasMoved();
        }
        Piece captured = board.getPieceAt(destination);

        movingPiece.getPositions().remove(origin);
        movingPiece.getPositions().add(destination);
        if (captured != null) {
            captured.getPositions().remove(destination);
        }

        if (movingPiece instanceof King) {
            ((King) movingPiece).setHasMoved(true);
        } else if (movingPiece instanceof Rook) {
            ((Rook) movingPiece).setHasMoved(true);
        }
        moveCounter++;
        //System.out.println("After move, movingPiece positions: " + movingPiece.getPositions());
        return new UndoData(move, movingPiece, captured, originalHasMoved);
    }

    private void undoMove(UndoData data) {
        Move move = data.move;
        Position origin = move.getOrigin();
        Position destination = move.getDestination();
        Piece movingPiece = data.movingPiece;

//        System.out.println("undoMove: " + movingPiece + " from " + destination + " back to " + origin);
//        System.out.println("Before undo, movingPiece positions: " + movingPiece.getPositions());

        movingPiece.getPositions().remove(destination);
        movingPiece.getPositions().add(origin);

        if (movingPiece instanceof King) {
            ((King) movingPiece).setHasMoved(data.originalHasMoved);
        } else if (movingPiece instanceof Rook) {
            ((Rook) movingPiece).setHasMoved(data.originalHasMoved);
        }
        if (data.captured != null) {
            data.captured.getPositions().add(destination);
        }
        moveCounter--;
        //System.out.println("After undo, movingPiece positions: " + movingPiece.getPositions());
    }

    public boolean isAITurn() {
        return getTurn().equals(AIColor);
    }

    public PieceColor getAIColor() {
        return AIColor;
    }
}
