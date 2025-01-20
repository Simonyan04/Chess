package source.core;

public abstract class Piece implements Cloneable{
    public static final String WHITE_KING = "K";
    public static final String BLACK_KING = "k";
    public static final String WHITE_QUEEN = "Q";
    public static final String BLACK_QUEEN = "q";
    public static final String WHITE_BISHOP = "B";
    public static final String BLACK_BISHOP = "b";
    public static final String WHITE_ROOK = "R";
    public static final String BLACK_ROOK = "r";
    public static final String WHITE_KNIGHT = "N";
    public static final String BLACK_KNIGHT = "n";
    public static final String WHITE_PAWN = "P";
    public static final String BLACK_PAWN = "p";
    private final Chess.PieceColor pieceColor;
    private Positions positions;
    private int score;
    public Piece(){
        pieceColor = Chess.PieceColor.WHITE;
    }
    public Piece(Positions positions, Chess.PieceColor color, int score){
        this.positions = positions;
        pieceColor = color;
        this.score = score;
    }
    public final Chess.PieceColor getPieceColor() {
        return pieceColor;
    }
    public final Positions getPositions(){
        return positions;
    }
    public void setPositions(Positions positions){
        this.positions = positions;
    }
    public abstract Positions allDestinations(Chess chess, Position p);
    public double getScore(double ratio) {
        boolean isWhite = getPieceColor().equals(Chess.PieceColor.WHITE);
        int[] bonusTable = getBonusTable();
        double bonusSum = 0;
        for (Position p : positions) {
            int zeroesCount = Long.numberOfTrailingZeros(p.getPosition());
            int index = isWhite ? (63 - zeroesCount) : zeroesCount;
            bonusSum += bonusTable[index];
        }
        return isWhite ? score * positions.size() + bonusSum : -(score * positions.size() + bonusSum);
    }


    public Piece clone(){
        try {
            Piece clone = (Piece)super.clone();
            clone.positions = positions.clone();
            return clone;
        }
        catch (CloneNotSupportedException e) {
            return null;
        }
    }
    public Positions allDestinations(Chess chess){
        Positions result = new Positions();
        for (Position p : positions){
            result.add(allDestinations(chess, p));
        }
        return result;
    }
    public abstract int[] getBonusTable();
    public double getPureScore(){
        return score * positions.size();
    }

}