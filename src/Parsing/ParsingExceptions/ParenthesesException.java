package Parsing.ParsingExceptions;

import Lexing.Token;

public class ParenthesesException extends ParsingException {
    private static String message = "Parentheses don't match. Starts at ";
    public ParenthesesException(Token t) {
        super(t, message);
    }

    public ParenthesesException(String line, int lineNum, int posNum) {
        super(line, lineNum, posNum, message);
    }

}
