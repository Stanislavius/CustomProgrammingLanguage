package Parsing.ParsingExceptions;

import Lexing.Token;

public class UnexpectedIndentException extends ParsingException{
    private static String message = "Unexpected indentation at ";
    public UnexpectedIndentException(Token t) {
        super(t, message);
    }

    public UnexpectedIndentException(String line, int lineNum, int posNum) {
        super(line, lineNum, posNum, message);
    }
}
