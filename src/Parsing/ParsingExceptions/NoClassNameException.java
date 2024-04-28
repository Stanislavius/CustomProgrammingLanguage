package Parsing.ParsingExceptions;

import Lexing.Token;

public class NoClassNameException extends ParsingException{
    private static String message = "Expect class name at ";
    public NoClassNameException(Token t) {
        super(t);
    }

    public NoClassNameException(String line, int lineNum, int posNum) {
        super(line, lineNum, posNum);
    }

    public NoClassNameException(Token t, String message) {
        super(t, message);
    }

    public NoClassNameException(String line, int lineNum, int posNum, String message) {
        super(line, lineNum, posNum, message);
    }
}
