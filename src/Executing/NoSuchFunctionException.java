package Executing;

import Lexing.Token;

public class NoSuchFunctionException extends ExecutionException {
    public NoSuchFunctionException(Token t) {
        super(t);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("No such function ");
        sb.append(error_token.getValue());
        sb.append(" ");
        sb.append(error_token.getLine());
        sb.append(", position is ");
        sb.append(error_token.getPos());
        return sb.toString();
    }
}
