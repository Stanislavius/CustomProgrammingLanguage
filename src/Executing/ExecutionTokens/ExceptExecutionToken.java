package Executing.ExecutionTokens;

import Executing.ExecutionTokens.Builtin.Types.ClassType;
import Executing.ExecutionTokens.Builtin.Types.ErrorType;
import Executing.ExecutionTokens.Builtin.Types.ObjectType;
import Executing.ExecutionTokens.Builtin.Types.VoidType;
import Executing.Executor;
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
    public ObjectType execute() {
        ObjectType result = new VoidType();
        for(int i = 0; i < toDo.size(); ++i){
            result = toDo.get(i).execute();
        }
        return result;
    }

    public void addErrorType(ExecutionToken error){
        this.errorTypes.add(error);
    }

    public boolean isCatched(ErrorType err){
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
