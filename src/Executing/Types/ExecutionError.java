package Executing.Types;

public class ExecutionError extends Exception {
    ErrorType e;
    public ExecutionError(ErrorType e){
        this.e = e;
    }

    public ErrorType getError() {
        return e;
    }
}
