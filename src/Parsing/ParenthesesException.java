package Parsing;

import Lexing.Token;

public class ParenthesesException extends ParsingException {
    public ParenthesesException(Token t) {
        super(t);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Parentheses don't match. Starts at ");
        sb.append(error_token.getLine());
        sb.append(" line, position is ");
        sb.append(error_token.getPos());
        return sb.toString();
    }

}
