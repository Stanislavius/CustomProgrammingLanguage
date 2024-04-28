package Parsing.ParsingExceptions;

import Lexing.Token;

public class NoConditionException extends ParsingException{
    private static String message = "Condition is expected at ";

    public NoConditionException(Token t) {
        super(t);
    }

    public NoConditionException(String line, int lineNum, int posNum) {
        super(line, lineNum, posNum);
    }

    public NoConditionException(Token t, String message) {
        super(t, message);
    }

    public NoConditionException(String line, int lineNum, int posNum, String message) {
        super(line, lineNum, posNum, message);
    }

}
