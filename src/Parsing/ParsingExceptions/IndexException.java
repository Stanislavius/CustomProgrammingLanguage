package Parsing.ParsingExceptions;

import Lexing.Token;

public class IndexException extends ParsingException{
    public IndexException(Token t) {
        super(t);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Wrong index in line ");
        sb.append(errorToken.getLineNum());
        sb.append(", position is ");
        sb.append(errorToken.getPos());
        return sb.toString();
    }
}
