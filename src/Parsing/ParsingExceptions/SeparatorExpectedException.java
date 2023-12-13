package Parsing.ParsingExceptions;

import Lexing.Token;

public class SeparatorExpectedException extends ParsingException{
    private static String message = "Two values didn't separated by separator at";
    public SeparatorExpectedException(Token t) {
        super(t, message);
    }

    public SeparatorExpectedException(String line, int lineNum, int posNum) {
        super(line, lineNum, posNum, message);
    }
}
