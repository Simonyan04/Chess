package source.puzzles;
import source.core.Chess;
import source.exceptions.MalformedPuzzleException;

public final class Puzzle implements Comparable<Puzzle>{
    public enum Difficulty {EASY, MEDIUM, HARD, UNSPECIFIED}
    private String arrangement;
    private Chess.PieceColor turn;
    private Difficulty difficulty;
    private String description;

    public Puzzle(String firstLine, String secondLine) throws MalformedPuzzleException {
        arrangement = firstLine.substring(0, 64);
        try {
            Chess.verifyArrangement(arrangement);
        }
        catch (Exception e){
            throw new MalformedPuzzleException();
        }
        description = secondLine;
        String color = firstLine.substring(65,70);
        String stringDifficulty = firstLine.substring(71);
        turn = Chess.PieceColor.valueOf(color);
        difficulty = Difficulty.valueOf(stringDifficulty);
    }
    public Puzzle(String input) throws MalformedPuzzleException{
        this(input.substring(0,input.indexOf("\n")), input.substring(input.indexOf("\n")+1));
    }
    public Puzzle(Puzzle that){
        this.arrangement = that.arrangement;
        this.description = that.description;
        this.turn = that.turn;
        this.difficulty = that.difficulty;
    }
    public Difficulty getDifficulty() {
        return difficulty;
    }
    public Chess.PieceColor getTurn() {
        return turn;
    }
    public String getArrangement() {
        return arrangement;
    }
    public String getDescription() {
        return description;
    }

    public String toString() {
        return arrangement + ',' + turn + "," + difficulty + "\n" + description;
    }
    public boolean equals(Object that){
        if (that == null){
            return false;
        }
        else if (that.getClass() != getClass()){
            return false;
        }
        else{
            Puzzle puzzle = (Puzzle) that;
            return (difficulty == puzzle.getDifficulty() && arrangement.equals(puzzle.getArrangement()) && turn == puzzle.getTurn());
        }
    }
    public int compareTo(Puzzle that) {
        if (difficulty.ordinal() != that.getDifficulty().ordinal()){
            if (difficulty.ordinal() < that.getDifficulty().ordinal()){
                return -1;
            }
            else{
                return 1;
            }
        }
        if (that.getTurn() != turn){
            if (turn.ordinal() < that.getTurn().ordinal()){
                return -1;
            }
            else{
                return 1;
            }
        }
        return arrangement.compareTo(that.getArrangement());
    }
}
