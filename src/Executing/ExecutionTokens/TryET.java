package Executing.ExecutionTokens;

import Executing.Types.ErrorType;
import Executing.Types.ExecutionException;
import Executing.Types.ObjectType;
import Executing.Types.VoidType;
import Executing.Executor;
import Lexing.Token;

import java.util.LinkedList;

public class TryET extends ExecutionToken{
    LinkedList<ExecutionToken> toDo;
    LinkedList<ExceptET> excepts;
    boolean stop = false;
    ObjectType result;

    public TryET(Token token, LinkedList<ExecutionToken> toDo, LinkedList<ExceptET> excepts) {
        super(token);
        this.toDo = toDo;
        this.excepts = excepts;
    }

    public ObjectType execute() throws ExecutionException {
        result = new VoidType();
        Executor.enterTryBlock(this);
        for(int i = 0; i < toDo.size(); ++i){
                result = toDo.get(i).execute();
        }
        Executor.exitTryBlock();
        return result;
    }

    public ObjectType doExcept(ErrorType caught) throws ExecutionException {
        result = new VoidType();
        for(int i = 0; i < excepts.size(); ++i)
            if (excepts.get(i).isCatched(caught)){
                result = excepts.get(i).execute();
            }
        return result;
    }

    public void replaceOuterVariableIfHasAny(LinkedList<String> args) throws ExecutionException {
        for(int i = 0; i < toDo.size(); ++i){
            toDo.get(i).replaceOuterVariableIfHasAny(args);
        }
    }
}
