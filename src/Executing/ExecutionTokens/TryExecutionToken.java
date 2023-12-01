package Executing.ExecutionTokens;

import Executing.ExecutionTokens.Builtin.Types.ErrorType;
import Executing.ExecutionTokens.Builtin.Types.ObjectType;
import Executing.ExecutionTokens.Builtin.Types.VoidType;
import Executing.Executor;
import Lexing.Token;

import java.util.LinkedList;

public class TryExecutionToken extends ExecutionToken{
    LinkedList<ExecutionToken> toDo;
    LinkedList<ExceptExecutionToken> excepts;
    boolean stop = false;
    ObjectType result;

    public TryExecutionToken(Token token, LinkedList<ExecutionToken> toDo, LinkedList<ExceptExecutionToken> excepts) {
        super(token);
        this.toDo = toDo;
        this.excepts = excepts;
    }

    public ObjectType execute() {
        result = new VoidType();
        Executor.enterTryBlock(this);
        for(int i = 0; i < toDo.size(); ++i){
            if (!stop)
                result = toDo.get(i).execute();
            else{
                stop = false;
                break;
            }
        }
        if (stop == false)
            Executor.exitTryBlock();
        return result;
    }

    public ObjectType doExcept(ErrorType caught){
        result = new VoidType();
        stop = true;
        Executor.exitTryBlock();
        for(int i = 0; i < excepts.size(); ++i)
            if (excepts.get(i).isCatched(caught)){
                result = excepts.get(i).execute();
            }
        return result;
    }
}
