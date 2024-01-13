package Parsing.ParsingExceptions;

import Lexing.Token;

public class UnexpectedBlockWordException extends ParsingException{
    private static String message = "Unexpected blockword at ";
    public UnexpectedBlockWordException(Token t) {
        super(t, message);
    }

    public UnexpectedBlockWordException(String line, int lineNum, int posNum) {
        super(line, lineNum, posNum, message);
    }
}
