package Executing.Types;

public class EndOfExecutionException extends Exception{
    ErrorType error;
    public EndOfExecutionException(ErrorType error){
        this.error = error;
    }
    public String toString(){
        return error.toString();
    }

    public ErrorType getError(){
        return error;
    }
}
