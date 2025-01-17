package am.aua.chess.core;

import am.aua.chess.exceptions.IllegalArrangementException;
import am.aua.chess.exceptions.InvalidNumberOfKingsException;

import java.util.ArrayList;

public class Board implements Cloneable{
    public static final int WHITE_START_INDEX = 0;
    public static final int WHITE_END_INDEX = 6;
    public static final int BLACK_START_INDEX = WHITE_END_INDEX;
    public static final int BLACK_END_INDEX = 12;
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
    public Chess.PieceColor getPieceColorAt(Position position){
        Piece p = getPieceAt(position);
        return p == null  ? null : p.getPieceColor();
    }
    public boolean isEmpty(Position position){
        return getPieceAt(position) == null;
    }
    public Positions getAllDestinationsByColor(Chess.PieceColor color) {
        Positions result = new Positions();
        boolean isWhite = color.equals(Chess.PieceColor.WHITE);
        for (int i = isWhite ? WHITE_START_INDEX : BLACK_START_INDEX; i < (isWhite ? WHITE_END_INDEX : BLACK_END_INDEX); i++) {
            result.add(pieces[i].allDestinations(this));
        }
        return result;
    }
    public Iterable<Move> getMovesByColor(Chess.PieceColor color){
        ArrayList<Move> result = new ArrayList<>();
        boolean isWhite = color.equals(Chess.PieceColor.WHITE);
        for (int i = isWhite ? WHITE_START_INDEX : BLACK_START_INDEX; i < (isWhite ? WHITE_END_INDEX : BLACK_END_INDEX); i++) {
            Piece currentPiece = pieces[i];
            for (Position origin : currentPiece.getPositions()){
                for (Position destination : currentPiece.allDestinations(this, origin)) {
                    result.add(new Move(origin, destination));
                }
            }
        }
        return result;
    }
}
