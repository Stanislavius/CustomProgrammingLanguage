package Parsing.ParsingExceptions;

import Lexing.Token;

public class BracketIsNotClosedException extends ParsingException{
    private static String message = "Bracket is not closed: ";
    public BracketIsNotClosedException(Token t) {
        super(t, message);
    }

    public BracketIsNotClosedException(String line, int lineNum, int posNum) {
        super(line, lineNum, posNum, message);
    }

}
