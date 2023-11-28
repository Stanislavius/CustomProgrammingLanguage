package Parsing.ParsingExceptions;

import Lexing.Token;

public class ParenthesesException extends ParsingException {
    public ParenthesesException(Token t) {
        super(t);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Parentheses don't match. Starts at ");
        sb.append(errorToken.getLine());
        sb.append(" line, position is ");
        sb.append(errorToken.getPos());
        return sb.toString();
    }

}
