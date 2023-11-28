package Parsing.ParsingExceptions;

import Lexing.Token;

public class ParsingException extends Exception {
    protected Token errorToken;

    public ParsingException(Token t) {
        errorToken = t;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Error in line ");
        sb.append(errorToken.getLine());
        sb.append(", position is ");
        sb.append(errorToken.getPos());
        return sb.toString();
    }

    public String getTestingRepresentation(){
        return this.getClass().getSimpleName() + ": " + errorToken.getLine() + " " + errorToken.getPos();
    }

}
