package Executing.ExecutionTokens;

import Executing.ReturnType;
import Executing.ReturnValue;
import Lexing.Token;

public class ElseExecutionToken extends ExecutionToken{
    public ElseExecutionToken(Token token) {
        super(token);
    }
    public ReturnValue execute(){
        return new ReturnValue<Integer>(1, ReturnType.INT);
    }
}
