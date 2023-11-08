package Executing;

import Lexing.Token;

import java.util.LinkedList;

public class ElseExecutionToken extends ExecutionToken{
    public ElseExecutionToken(Token token) {
        super(token);
    }
    public ReturnValue execute(){
        return new ReturnValue<Integer>(1, ReturnType.INT);
    }
}
