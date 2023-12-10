package Executing.ExecutionTokens;

import Executing.Types.ExecutionException;
import Executing.Types.ObjectType;
import Lexing.Token;

public class ItemExecutionToken extends ExecutionToken{
    ExecutionToken object;
    ExecutionToken item;
    public ItemExecutionToken(Token token, ExecutionToken object, ExecutionToken item) {
        super(token);
        this.object = object;
        this.item = item;
    }

    public ObjectType execute() throws ExecutionException {
        ObjectType result = object.execute();
        ObjectType func = result.getMember("__getitem__");
        return func.call(item.execute());
    }

    public ObjectType executeSetItem(ObjectType whatToSet) throws ExecutionException {
        ObjectType result = object.execute();
        return result.getMember("__setitem__").call(item.execute(), whatToSet);
    }

    public ObjectType executeObject() throws ExecutionException {
        return object.execute();
    }

    public ExecutionToken getObject(){
        return object;
    }

    public ExecutionToken getMember(){
        return item;
    }
}
