package Parsing.ParsingExceptions;

import Lexing.Token;

public class UsingKeywordAsVariableException extends ParsingException{
    private static String message = "Keyword is used as variable in line ";
    public UsingKeywordAsVariableException (Token t) {
        super(t, message);
    }

    public UsingKeywordAsVariableException (String line, int lineNum, int posNum) {
        super(line, lineNum, posNum, message);
    }
}
