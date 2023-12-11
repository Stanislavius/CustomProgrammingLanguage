package Parsing.ParsingExceptions;

import Lexing.Token;

public class SeparatorExpectedExcveption extends ParsingException{
    public SeparatorExpectedExcveption(Token t) {
        super(t);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Two values didn't separated by separator at ");
        sb.append(errorToken.getLineNum());
        sb.append(" line, position is ");
        sb.append(errorToken.getPos());
        return sb.toString();
    }
}
