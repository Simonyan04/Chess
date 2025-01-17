package am.aua.chess.exceptions;

public class InvalidNumberOfKingsException extends IllegalArrangementException {
    public InvalidNumberOfKingsException(){
        super("InvalidNumberOfKings");
    }
    public InvalidNumberOfKingsException(String message){
        super(message);
    }
}
