package Executing.ExecutionExceptions;

import Lexing.Token;


public class ZeroDivisionException extends ExecutionException {
    public ZeroDivisionException(Token t){
        super(t);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Division by zero in line ");
        sb.append(errorToken.getLineNum());
        sb.append(", position is ");
        sb.append(errorToken.getPos());
        return sb.toString();
    }

}
