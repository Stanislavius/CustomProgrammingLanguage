package Executing.ExecutionTokens;

import Executing.ExecutionTokens.Builtin.Types.ErrorType;

public class EndOfExecutionError extends Exception{
    ErrorType error;
    public EndOfExecutionError(ErrorType error){
        this.error = error;
    }
    public String toString(){
        return error.toString();
    }

    public ErrorType getError(){
        return error;
    }
}
