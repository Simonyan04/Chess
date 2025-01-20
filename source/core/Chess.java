package source.core;

import source.exceptions.IllegalArrangementException;
import source.exceptions.InvalidNumberOfKingsException;

import java.util.ArrayList;



public class Chess implements Cloneable{
    private class Board implements Cloneable{
        private Piece[] pieces = new Piece[PieceType.values().length];
        public enum PieceType {
            WHITE_KING,
            WHITE_QUEEN,
            WHITE_ROOK,
            WHITE_BISHOP,
            WHITE_KNIGHT,
            WHITE_PAWN,
            BLACK_KING,
            BLACK_QUEEN,
            BLACK_ROOK,
            BLACK_BISHOP,
            BLACK_KNIGHT,
            BLACK_PAWN
        }
        public Board() throws IllegalArrangementException {
            pieces[PieceType.WHITE_KING.ordinal()]   = new King(Chess.PieceColor.WHITE);
            pieces[PieceType.BLACK_KING.ordinal()]   = new King(Chess.PieceColor.BLACK);
            pieces[PieceType.WHITE_QUEEN.ordinal()]  = new Queen(Chess.PieceColor.WHITE);
            pieces[PieceType.BLACK_QUEEN.ordinal()]  = new Queen(Chess.PieceColor.BLACK);
            pieces[PieceType.WHITE_ROOK.ordinal()]   = new Rook(Chess.PieceColor.WHITE);
            pieces[PieceType.BLACK_ROOK.ordinal()]   = new Rook(Chess.PieceColor.BLACK);
            pieces[PieceType.WHITE_BISHOP.ordinal()] = new Bishop(Chess.PieceColor.WHITE);
            pieces[PieceType.BLACK_BISHOP.ordinal()] = new Bishop(Chess.PieceColor.BLACK);
            pieces[PieceType.WHITE_KNIGHT.ordinal()] = new Knight(Chess.PieceColor.WHITE);
            pieces[PieceType.BLACK_KNIGHT.ordinal()] = new Knight(Chess.PieceColor.BLACK);
            pieces[PieceType.WHITE_PAWN.ordinal()]   = new Pawn(Chess.PieceColor.WHITE);
            pieces[PieceType.BLACK_PAWN.ordinal()]   = new Pawn(Chess.PieceColor.BLACK);
        }
        public Board(String positioning) throws IllegalArrangementException {
            verifyArrangement(positioning);
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    Piece current = pieceFromChar(positioning.charAt(8 * i + j));
                    assert current != null;
                    current.getPositions().add(j, i);
                }
            }
        }
        private Piece pieceFromChar(char pieceChar) {
            boolean hasMoved = false;
            PieceType pieceType = switch (pieceChar) {
                case 'K', 'L' -> PieceType.WHITE_KING;
                case 'k', 'l' -> PieceType.BLACK_KING;
                case 'Q' -> PieceType.WHITE_QUEEN;
                case 'q' -> PieceType.BLACK_QUEEN;
                case 'R', 'S' -> PieceType.WHITE_ROOK;
                case 'r', 's' -> PieceType.BLACK_ROOK;
                case 'B' -> PieceType.WHITE_BISHOP;
                case 'b' -> PieceType.BLACK_BISHOP;
                case 'N' -> PieceType.WHITE_KNIGHT;
                case 'n' -> PieceType.BLACK_KNIGHT;
                case 'P' -> PieceType.WHITE_PAWN;
                case 'p' -> PieceType.BLACK_PAWN;
                default -> null;
            };

            if (pieceType == null) {
                return null;
            }

            if (pieceChar == 'L' || pieceChar == 'l') {
                hasMoved = true;
            }

            if (pieces[pieceType.ordinal()] == null) {
                pieces[pieceType.ordinal()] = createPiece(pieceType, hasMoved);
            }

            return pieces[pieceType.ordinal()];
        }
        private Piece createPiece(PieceType pieceType, boolean hasMoved) {
            return switch (pieceType) {
                case WHITE_KING -> new King(Chess.PieceColor.WHITE, hasMoved);
                case BLACK_KING -> new King(Chess.PieceColor.BLACK, hasMoved);
                case WHITE_QUEEN -> new Queen(Chess.PieceColor.WHITE);
                case BLACK_QUEEN -> new Queen(Chess.PieceColor.BLACK);
                case WHITE_ROOK -> new Rook(Chess.PieceColor.WHITE, hasMoved);
                case BLACK_ROOK -> new Rook(Chess.PieceColor.BLACK, hasMoved);
                case WHITE_BISHOP -> new Bishop(Chess.PieceColor.WHITE);
                case BLACK_BISHOP -> new Bishop(Chess.PieceColor.BLACK);
                case WHITE_KNIGHT -> new Knight(Chess.PieceColor.WHITE);
                case BLACK_KNIGHT -> new Knight(Chess.PieceColor.BLACK);
                case WHITE_PAWN -> new Pawn(Chess.PieceColor.WHITE);
                case BLACK_PAWN -> new Pawn(Chess.PieceColor.BLACK);
            };
        }
        @Override
        public Board clone() {
            try {
                Board clone = (Board) super.clone();
                clone.pieces = new Piece[this.pieces.length];
                for (int i = 0; i < this.pieces.length; i++) {
                    if (this.pieces[i] != null) {
                        clone.pieces[i] = this.pieces[i].clone();
                    }
                }
                return clone;
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }
        public Piece getPiece(PieceType pieceType){
            return pieces[pieceType.ordinal()];
        }
        public Piece getPieceAt(Position position) {
            for (Piece piece : pieces) {
                if (piece.getPositions().contains(position)) {
                    return piece;
                }
            }
            return null;
        }
        public boolean isEmpty(Position position){
            return getPieceAt(position) == null;
        }
        public int getWhiteStartIndex(){
            return 0;
        }
        public int getWhiteEndIndex(){
            return 6;
        }
        public int getBlackStartIndex(){
            return getWhiteEndIndex();
        }
        public int getBlackEndIndex(){
            return 12;
        }
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            // Print rows from top (7) to bottom (0)
            for (int row = 7; row >= 0; row--) {
                // Optionally, label the row numbers:
                sb.append((row + 1) + " ");
                for (int col = 0; col < 8; col++) {
                    Position pos = new Position(col, row);
                    Piece p = getPieceAt(pos);
                    if (p == null) {
                        sb.append(". ");
                    } else {
                        // You may decide how to represent the piece.
                        // For example, if each piece's toString returns a single-letter representation:
                        sb.append(p.toString() + " ");
                    }
                }
                sb.append("\n");
            }
            // Optionally, add the column labels:
            sb.append("  a b c d e f g h\n");
            return sb.toString();
        }
    }
    public void printBoard(){
        System.out.println(board);
    }
    public enum PieceColor {WHITE, BLACK}
    private Board board;
    public int moveCounter = 0;
    private Move lastMove;
    public static final int ROW = 8;
    public static final int COLUMN = 8;
    public Chess clone(){
        try {
            Chess clone = (Chess)super.clone();
            clone.board = board.clone();
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
        this.board = that.board.clone();
        this.lastMove = that.lastMove.clone();
    }
    public PieceColor getTurn(){
        return moveCounter%2 == 0 ? PieceColor.WHITE : PieceColor.BLACK;
    }
    public boolean isGameOver(PieceColor color){
        Piece king = board.getPiece(color == PieceColor.WHITE ? Board.PieceType.WHITE_KING : Board.PieceType.BLACK_KING);
        return king.allDestinations(this).isEmpty() && isKingUnderAttack(color);
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
        return getPieceAt(origin).allDestinations(this, origin);
    }

    public static boolean isValidMove(Chess chess, Move move){
        Positions reachablePositions = chess.reachableFrom(move.getOrigin());
        return reachablePositions.contains(move.getDestination());
    }

    public static boolean isRightTurn(Chess chess, Position position){
        return chess.getTurn() == chess.getPieceAt(position).getPieceColor();
    }

    public Chess.PieceColor getPieceColorAt(Position position){
        Piece p = getPieceAt(position);
        return p == null  ? null : p.getPieceColor();
    }

    public Positions getAllDestinationsByColor(Chess.PieceColor color) {
        Positions result = new Positions();
        boolean isWhite = color.equals(Chess.PieceColor.WHITE);
        for (int i = isWhite ? board.getWhiteStartIndex() : board.getBlackStartIndex(); i < (isWhite ? board.getWhiteEndIndex() : board.getBlackEndIndex()); i++) {
            result.add(board.pieces[i].allDestinations(this));
        }
        return result;
    }
    public Iterable<Move> getMovesByColor(Chess.PieceColor color){
        ArrayList<Move> result = new ArrayList<>();
        boolean isWhite = color.equals(Chess.PieceColor.WHITE);
        for (int i = isWhite ? board.getWhiteStartIndex() : board.getBlackStartIndex(); i < (isWhite ? board.getWhiteEndIndex() : board.getBlackEndIndex()); i++) {
            Piece currentPiece = board.pieces[i];
            for (Position origin : currentPiece.getPositions()){
                for (Position destination : currentPiece.allDestinations(this, origin)) {
                    Move currentMove = new Move(origin, destination);
                    if(isLegalMove(currentMove)){
                        result.add(currentMove);
                    }
                }
            }
        }
        return result;
    }

    public boolean performMove(Move move) {
        if(!isLegalMove(move)) return false;

        Position origin = move.getOrigin();
        Position destination = move.getDestination();

        Piece originPiece = board.getPieceAt(origin);
        Piece destinationPiece = board.getPieceAt(destination);

        assert originPiece != null;
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
        System.out.println(points());
        return true;
    }
    public boolean isLegalMove(Move move){
        Chess game_copy = this.clone();

        Position origin = move.getOrigin();
        Position destination = move.getDestination();

        if (!isRightTurn(game_copy, origin) || !isValidMove(game_copy, move)) return false;

        Piece originPiece = game_copy.getPieceAt(origin);
        Piece destinationPiece = game_copy.getPieceAt(destination);

        assert originPiece != null;
        Positions originPiecePositions = originPiece.getPositions();
        Positions destinationPiecePositions = destinationPiece == null ? null : destinationPiece.getPositions();

        originPiecePositions.remove(origin);
        originPiecePositions.add(destination);

        if(destinationPiecePositions != null) destinationPiecePositions.remove(destination);

        return !game_copy.isKingUnderAttack(getTurn());
    }
    public boolean isKingUnderAttack(PieceColor color) {
        Position kingPosition = (color == PieceColor.WHITE ? board.getPiece(Board.PieceType.WHITE_KING).getPositions() : board.getPiece(Board.PieceType.BLACK_KING).getPositions()).first();
        return isPositionUnderAttack(color, kingPosition);
    }
    public boolean isPositionUnderAttack(PieceColor ourColor, Position position){
        PieceColor opponentColor = ourColor == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
        return getAllDestinationsByColor(opponentColor).contains(position);
    }
    protected double points(){
        double point = 0;
        if(isGameOver(PieceColor.WHITE)) return Integer.MIN_VALUE;
        if(isGameOver(PieceColor.BLACK)) return Integer.MAX_VALUE;
        double ratio = endgameRatio();
        for (Piece p : board.pieces) point += p == null ? 0 : p.getScore(ratio);
        return point;
    }
    public static void verifyArrangement(String s) throws IllegalArrangementException {
        char currentChar;
        boolean whiteKingPresent = false;
        boolean blackKingPresent = false;
        if (s.length() != 64){
            throw new IllegalArrangementException("The length of your arrangement is not 64");
        }
        for (int i = 0; i < s.length(); i++) {
            currentChar = s.charAt(i);
            if ((((currentChar == 'K' || currentChar == 'L') && whiteKingPresent) || ((currentChar == 'k' || currentChar == 'l') && blackKingPresent))){
                throw new InvalidNumberOfKingsException();
            }
            if(currentChar == 'K' || currentChar == 'L'){
                whiteKingPresent = true;
            }
            else if (currentChar == 'k' || currentChar == 'l'){
                blackKingPresent = true;
            }
        }
        if (!whiteKingPresent || !blackKingPresent){
            throw new InvalidNumberOfKingsException();
        }
    }
    public double endgameRatio(){
        double res = 0;
        for (Piece p : board.pieces)res += p.getPureScore();
        return res/8000;
    }
}
