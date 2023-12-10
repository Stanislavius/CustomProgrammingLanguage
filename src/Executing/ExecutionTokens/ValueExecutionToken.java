package Executing.ExecutionTokens;

import Executing.Types.ObjectType;
import Lexing.Token;

public class ValueExecutionToken extends ExecutionToken{
    ObjectType obj;
    public ValueExecutionToken(Token t, ObjectType obj){
        super(t);
        this.obj = obj;
    }

    public ObjectType execute(){
        return obj;
    }
}
