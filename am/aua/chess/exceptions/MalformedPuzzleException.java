package am.aua.chess.exceptions;

public class MalformedPuzzleException extends Exception{
    public MalformedPuzzleException(){
        super("MalformedPuzzle ");
    }
    public MalformedPuzzleException(String message){
        super(message);
    }
}
