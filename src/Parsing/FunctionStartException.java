package Parsing;

import Lexing.Token;

public class FunctionStartException extends ParsingException {
    public FunctionStartException(Token t) {
        super(t);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("At this position are expected parentheses: ");
        sb.append(error_token.getLine());
        sb.append(" line, position is ");
        sb.append(error_token.getPos());
        return sb.toString();
    }

}
