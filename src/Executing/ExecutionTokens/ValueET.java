package Executing.ExecutionTokens;

import Executing.Executor;
import Executing.Types.ExecutionException;
import Executing.Types.ObjectType;
import Executing.Types.StringType;
import Lexing.Token;

public class ValueET extends ExecutionToken{
    ObjectType obj;
    public ValueET(Token t, ObjectType obj){
        super(t);
        this.obj = obj;
    }

    public ObjectType execute() throws ExecutionException {
        Executor.logger.info("Create object " + obj.toString() + " of class " + obj.getMemberNoBound("__class__"));
        return obj;
    }

    public String toString(){
        if (obj.getClass() == StringType.class)
            return "\"" + obj.toString() + "\"";
        return obj.toString();
    }
}
