package Parsing.ParsingExceptions;

import Lexing.Token;

public class IndexException extends ParsingException{
    private static String message = "Wrong index in line";
    public IndexException(Token t) {
        super(t, message);
    }

    public IndexException(String line, int lineNum, int posNum) {
        super(line, lineNum, posNum, message);
    }
}
