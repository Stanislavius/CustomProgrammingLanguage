package Parsing;

import Lexing.Token;

public class ParsingException extends Exception {
    protected Token error_token;

    public ParsingException(Token t) {
        error_token = t;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Error in line ");
        sb.append(error_token.getLine());
        sb.append(", position is ");
        sb.append(error_token.getPos());
        return sb.toString();
    }

}
