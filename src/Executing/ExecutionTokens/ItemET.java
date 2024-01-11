package Executing.ExecutionTokens;

import Executing.Types.ErrorType;
import Executing.Types.ExecutionException;
import Executing.Types.ObjectType;
import Lexing.Token;

import java.util.LinkedList;

public class ItemET extends ExecutionToken{
    ExecutionToken object;
    ExecutionToken item;
    public ItemET(Token token, ExecutionToken object, ExecutionToken item) {
        super(token);
        this.object = object;
        this.item = item;
    }

    public ObjectType execute() throws ExecutionException {
        try {
            ObjectType result = object.execute();
            ObjectType func = result.getMember("__getitem__");
            return func.call(item.execute());
        }
        catch (ExecutionException e){
            ErrorType error = e.getError();
            error.setLine(this.getToken().getLineNum());
            error.setPosition(this.getToken().getPos());
            throw e;
        }
    }

    public ObjectType executeSetItem(ObjectType whatToSet) throws ExecutionException {
        try {
            ObjectType result = object.execute();
            return result.getMember("__setitem__").call(item.execute(), whatToSet);
        }
        catch (ExecutionException e){
            ErrorType error = e.getError();
            error.setLine(this.getToken().getLineNum());
            error.setPosition(this.getToken().getPos());
            throw e;
        }
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

    public String toString(){
        return object.toString() + "[" + item.toString() + "]";
    }

    public void replaceOuterVariableIfHasAny(LinkedList<String> args) throws ExecutionException {
        object.replaceOuterVariableIfHasAny(args);
        item.replaceOuterVariableIfHasAny(args);
    }
}
