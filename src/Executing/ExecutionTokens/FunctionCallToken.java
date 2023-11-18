package Executing.ExecutionTokens;

import Executing.ExecutionExceptions.ExecutionException;
import Executing.ExecutionExceptions.NoSuchFunctionException;
import Executing.ExecutionExceptions.WrongNumberOfArgumentsException;
import Executing.ExecutionTokens.Builtin.Types.ObjectType;
import Executing.Executor;
import Lexing.Token;

import java.util.LinkedList;

public class FunctionCallToken extends ExecutionToken {
    LinkedList<ExecutionToken> args;

    public FunctionCallToken(Token token, LinkedList<ExecutionToken> args) {
        super(token);
        this.args = args;
    }

    public ObjectType execute() {
        return Executor.getVariable(token.getValue()).call(executeArgs(args));
    }

    public LinkedList<ExecutionToken> getArgs() {
        return args;
    }

    public LinkedList<ObjectType> executeArgs() throws ExecutionException {
        LinkedList<ObjectType> executedArgs = new LinkedList<ObjectType>();
        for(int i = 0; i < args.size(); ++i){
            executedArgs.add(args.get(i).execute());
        }
        return executedArgs;
    }

    public static LinkedList<ObjectType> executeArgs (LinkedList<ExecutionToken> argsToExecute){
        LinkedList<ObjectType> executedArgs = new LinkedList<ObjectType>();
        for(int i = 0; i < argsToExecute.size(); ++i){
            executedArgs.add(argsToExecute.get(i).execute());
        }
        return executedArgs;
    }
}
