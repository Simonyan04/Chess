package source.exceptions;
public class IllegalArrangementException extends Exception{
    public IllegalArrangementException(){
        super("IllegalArrangement");
    }
    public IllegalArrangementException(String message){
        super(message);
    }
}
