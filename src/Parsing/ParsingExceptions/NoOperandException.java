package Parsing.ParsingExceptions;

import Lexing.Token;

public class NoOperandException extends ParsingException{
    private static String message = "Operand is expected in line ";
    public NoOperandException(Token t) {
        super(t, message);
    }

    public NoOperandException(String line, int lineNum, int posNum) {
        super(line, lineNum, posNum, message);
    }
}
