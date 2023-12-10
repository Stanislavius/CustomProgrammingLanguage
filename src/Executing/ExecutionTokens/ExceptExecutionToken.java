package Executing.ExecutionTokens;

import Executing.Types.*;
import Lexing.Token;

import java.util.LinkedList;

public class ExceptExecutionToken extends ExecutionToken{
    protected LinkedList<ExecutionToken> errorTypes = new LinkedList<ExecutionToken>();
    protected LinkedList<ExecutionToken> toDo;

    public ExceptExecutionToken(Token token) {
        super(token);
    }

    public ExceptExecutionToken(Token token, LinkedList<ExecutionToken> errorTypes, LinkedList<ExecutionToken> toDo) {
        super(token);
        this.toDo=toDo;
        this.errorTypes = errorTypes;
    }

    @Override
    public ObjectType execute() throws ExecutionException {
        ObjectType result = new VoidType();
        for(int i = 0; i < toDo.size(); ++i){
            result = toDo.get(i).execute();
        }
        return result;
    }

    public void addErrorType(ExecutionToken error){
        this.errorTypes.add(error);
    }

    public boolean isCatched(ErrorType err) throws ExecutionException {
        if (errorTypes.isEmpty())
            return true;
        else {
            for (int i = 0; i < errorTypes.size(); ++i) {
                if (err.getMember("__class__").equals(errorTypes.get(i).execute()))
                    return true;
            }
            return false;
        }
    }
}
