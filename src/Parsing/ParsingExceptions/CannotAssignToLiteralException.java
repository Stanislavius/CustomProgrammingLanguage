package Parsing.ParsingExceptions;

import Lexing.Token;

public class CannotAssignToLiteralException extends ParsingException{
    private static String message = "Cannot assign to literal ";
    public CannotAssignToLiteralException(Token t) {
        super(t, message);
    }

    public CannotAssignToLiteralException(String line, int lineNum, int posNum) {
        super(line, lineNum, posNum, message);
    }
}
