package Executing.ExecutionTokens;

import Executing.Types.ObjectType;
import Lexing.Token;

public class ValueET extends ExecutionToken{
    ObjectType obj;
    public ValueET(Token t, ObjectType obj){
        super(t);
        this.obj = obj;
    }

    public ObjectType execute(){
        return obj;
    }
}
