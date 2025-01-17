package am.aua.chess.core;

import am.aua.chess.exceptions.IllegalArrangementException;


import java.util.ArrayList;


public class Chess implements Cloneable{

    public enum PieceColor {WHITE, BLACK}
    public Board board;
    int moveCounter = 0;
    private Move lastMove;
    public static final int ROW = 8;
    public static final int COLUMN = 8;
    public Chess clone(){
        try {
            Chess clone = (Chess)super.clone();
            clone.board = getBoard();
            return clone;
        }
        catch (CloneNotSupportedException e) {
            return null;
        }
    }
    public Chess() throws IllegalArrangementException {
        board = new Board();
    }
    public Chess(String arrangement) throws IllegalArrangementException {
        board = new Board(arrangement);
    }
    public Chess(String arrangement, PieceColor turn) throws IllegalArrangementException {
        this(arrangement);
        moveCounter = turn == PieceColor.WHITE ? 0 : 1;
    }
    public Chess(Chess that){
        this.moveCounter = that.moveCounter;
        this.board = that.getBoard();
        this.lastMove = that.lastMove.clone();
    }
    public Board getBoard(){
        return board.clone();
    }
    public PieceColor getTurn(){
        return moveCounter%2 == 0 ? PieceColor.WHITE : PieceColor.BLACK;
    }
    public boolean isGameOver(PieceColor color){
        Positions kingPosition = color == PieceColor.WHITE ? board.getPiece(Board.PieceType.WHITE_KING).getPositions() : board.getPiece(Board.PieceType.BLACK_KING).getPositions();
        return false;
    }
    public boolean isEmpty(Position origin) {
        return board.isEmpty(origin);
    }
    public boolean isGameOver(){
        return isGameOver(PieceColor.WHITE) || isGameOver(PieceColor.BLACK);
    }
    public Piece getPieceAt(Position position){
        return board.getPieceAt(position);
    }

    public Positions reachableFrom(Position origin){
        if(board.isEmpty(origin)) return null;
        return getPieceAt(origin).allDestinations(board, origin);
    }

    public static boolean isValidMove(Chess chess, Move move){
        Positions reachablePositions = chess.reachableFrom(move.getOrigin());
        return reachablePositions.contains(move.getDestination());
    }

    public static boolean isRightTurn(Chess chess, Position position){
        return chess.getTurn() == chess.getPieceAt(position).getPieceColor();
    }

    public boolean performMove(Move move) {
        if(!isLegalMove(move)) return false;

        Position origin = move.getOrigin();
        Position destination = move.getDestination();

        Piece originPiece = board.getPieceAt(origin);
        Piece destinationPiece = board.getPieceAt(destination);

        Positions originPiecePositions = originPiece.getPositions();
        Positions destinationPiecePositions = destinationPiece == null ? null : destinationPiece.getPositions();

        originPiecePositions.remove(origin);
        originPiecePositions.add(destination);

        if(destinationPiecePositions != null) destinationPiecePositions.remove(destination);

        if (originPiece instanceof King) {
            ((King) originPiece).setHasMoved(true);
        }
        else if (originPiece instanceof Rook) {
            ((Rook) originPiece).setHasMoved(true);
        }

        moveCounter++;
        lastMove = move;

        return true;
    }
    public boolean isLegalMove(Move move){
        Board boardCopy = this.getBoard();

        Position origin = move.getOrigin();
        Position destination = move.getDestination();

        if (!isRightTurn(this, origin) || !isValidMove(this, move)) return false;

        Piece originPiece = board.getPieceAt(origin);
        Piece destinationPiece = board.getPieceAt(destination);

        Positions originPiecePositions = originPiece.getPositions();
        Positions destinationPiecePositions = destinationPiece == null ? null : destinationPiece.getPositions();

        originPiecePositions.remove(origin);
        originPiecePositions.add(destination);

        if(destinationPiecePositions != null) destinationPiecePositions.remove(destination);

        boolean isLegal = !isKingUnderAttack(getTurn());
        board = boardCopy;
        return isLegal;
    }
    public boolean isKingUnderAttack(PieceColor color) {
        Position kingPosition = (color == PieceColor.WHITE ? board.getPiece(Board.PieceType.WHITE_KING).getPositions() : board.getPiece(Board.PieceType.BLACK_KING).getPositions()).first();
        return isPositionUnderAttack(color, kingPosition);
    }
    public boolean isPositionUnderAttack(PieceColor ourColor, Position position){
        PieceColor opponentColor = ourColor == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
        return board.getAllDestinationsByColor(opponentColor).contains(position);
    }
    protected int points(){
        int point = 0;
        if(isGameOver(PieceColor.WHITE)) return Integer.MAX_VALUE;
        if(isGameOver(PieceColor.BLACK)) return Integer.MIN_VALUE;
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                Piece p = getPieceAt(new am.aua.chess.core.Position(j,i));
                point += p == null ? 0 : p.getScore();
            }
        }
        return point;
    }
}
