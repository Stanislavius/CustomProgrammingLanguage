package Executing.ExecutionExceptions;

import Lexing.Token;

public class NoSuchFunctionException extends ExecutionException {
    public NoSuchFunctionException(Token t) {
        super(t);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("No such function ");
        sb.append(errorToken.getValue());
        sb.append(" ");
        sb.append(errorToken.getLineNum());
        sb.append(", position is ");
        sb.append(errorToken.getPos());
        return sb.toString();
    }
}
