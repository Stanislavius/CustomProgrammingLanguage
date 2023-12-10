package Executing.Types;

public class ExecutionException extends Exception {
    ErrorType e;
    public ExecutionException(ErrorType e){
        this.e = e;
    }

    public ErrorType getError() {
        return e;
    }
}
