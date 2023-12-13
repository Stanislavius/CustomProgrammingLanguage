package Parsing.ParsingExceptions;

import Lexing.Token;

public class NoOperationException extends ParsingException{
    private static String message = "Operation is expected in line ";
    public NoOperationException(Token t) {
        super(t, message);
    }

    public NoOperationException(String line, int lineNum, int posNum) {
        super(line, lineNum, posNum, message);
    }

}
