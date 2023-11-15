package Executing.ExecutionTokens;

import Executing.ExecutionExceptions.ExecutionException;
import Executing.ReturnType;
import Executing.ReturnValue;
import Lexing.Token;

import java.util.LinkedList;

public class ListType extends ExecutionToken{
    LinkedList<ExecutionToken> values;

    public ListType(Token t, LinkedList<ExecutionToken> values){
        super(t);
        this.values = values;

    }
    public ReturnValue execute() throws ExecutionException {
        return new ReturnValue(values, ReturnType.LIST);
    }

}
