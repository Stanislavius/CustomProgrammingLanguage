package Parsing.ParsingExceptions;

import Lexing.Token;

public class NoOperandException extends ParsingException{
    public NoOperandException(Token t) {
        super(t);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Operand is expected in line ");
        sb.append(errorToken.getLine());
        sb.append(", position is ");
        sb.append(errorToken.getPos());
        return sb.toString();
    }

}
