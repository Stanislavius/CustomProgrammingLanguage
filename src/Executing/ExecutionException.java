package Executing;

import Lexing.Token;

public class ExecutionException extends Exception {
    protected Token error_token;

    public ExecutionException(Token t) {
        error_token = t;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Runtime error in line ");
        sb.append(error_token.getLine());
        sb.append(", position is ");
        sb.append(error_token.getPos());
        return sb.toString();
    }

}
