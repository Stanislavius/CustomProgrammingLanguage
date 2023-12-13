package Parsing.ParsingExceptions;

import Lexing.Token;

public class EmptyLineException extends ParsingException{
    private static String message = "Empty line at ";
    public EmptyLineException(Token t) {
        super(t, message);
    }

    public EmptyLineException(String line, int lineNum, int posNum) {
        super(line, lineNum, posNum, message);
    }
}
