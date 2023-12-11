package Executing.ExecutionExceptions;

import Lexing.Token;

public class ExecutionException extends Exception {
    protected Token errorToken;

    public ExecutionException(Token t) {
        errorToken = t;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Runtime error in line ");
        sb.append(errorToken.getLineNum());
        sb.append(", position is ");
        sb.append(errorToken.getPos());
        return sb.toString();
    }

}
