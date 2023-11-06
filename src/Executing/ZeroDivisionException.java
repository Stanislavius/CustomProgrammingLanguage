package Executing;

import Executing.*;
import Lexing.Token;


public class ZeroDivisionException extends ExecutionException {
    public ZeroDivisionException(Token t){
        super(t);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Division by zero in line ");
        sb.append(error_token.getLine());
        sb.append(", position is ");
        sb.append(error_token.getPos());
        return sb.toString();
    }

}
