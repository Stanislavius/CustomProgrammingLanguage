package Parsing.ParsingExceptions;

import Lexing.Token;

public class NoBodyException extends ParsingException{
    private static String message = "Expected body at ";
    public NoBodyException(Token t) {
        super(t);
    }

    public NoBodyException(String line, int lineNum, int posNum) {
        super(line, lineNum, posNum, message);
    }
}
