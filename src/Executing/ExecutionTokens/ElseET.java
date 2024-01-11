package Executing.ExecutionTokens;

import Executing.Types.IntType;
import Executing.Types.ObjectType;
import Lexing.Token;

import java.util.LinkedList;

public class ElseET extends ExecutionToken{
    public ElseET(Token token) {
        super(token);
    }
    public ObjectType execute(){
        return new IntType(1);
    }

}
