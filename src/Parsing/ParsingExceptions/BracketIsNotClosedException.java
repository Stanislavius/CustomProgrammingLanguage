package Parsing.ParsingExceptions;

import Lexing.Token;

public class BracketIsNotClosedException extends ParsingException{

    public BracketIsNotClosedException(Token t) {
        super(t);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bracket at ");
        sb.append(errorToken.getLineNum());
        sb.append(" line, position is ");
        sb.append(errorToken.getPos());
        sb.append(", is not closed");
        return sb.toString();
    }

}
