package Parsing.ParsingExceptions;

import Lexing.Token;

public class NoOperationException extends ParsingException{
    public NoOperationException(Token t) {
        super(t);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Operation is expected in line ");
        sb.append(errorToken.getLine());
        sb.append(", position is ");
        sb.append(errorToken.getPos());
        return sb.toString();
    }

}
