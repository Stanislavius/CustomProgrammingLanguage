package Parsing.ParsingExceptions;

import Lexing.Token;

public class NoFunctionSignatureException extends ParsingException{
    private static String message = "Function signature is expected at ";
    public NoFunctionSignatureException(Token t) {
        super(t);
    }

    public NoFunctionSignatureException(String line, int lineNum, int posNum) {
        super(line, lineNum, posNum);
    }

    public NoFunctionSignatureException(Token t, String message) {
        super(t, message);
    }

    public NoFunctionSignatureException(String line, int lineNum, int posNum, String message) {
        super(line, lineNum, posNum, message);
    }
}
