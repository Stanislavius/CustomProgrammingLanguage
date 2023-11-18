package Executing.ExecutionTokens;

import Executing.ExecutionTokens.Builtin.Types.IntType;
import Executing.ExecutionTokens.Builtin.Types.ObjectType;
import Lexing.Token;

public class ElseExecutionToken extends ExecutionToken{
    public ElseExecutionToken(Token token) {
        super(token);
    }
    public ObjectType execute(){
        return new IntType(1);
    }
}
