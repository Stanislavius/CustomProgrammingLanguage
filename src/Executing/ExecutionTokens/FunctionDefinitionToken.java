package Executing.ExecutionTokens;
import Executing.ExecutionTokens.Builtin.Types.*;
import Executing.Executor;
import Lexing.Token;

import java.util.LinkedList;

public class FunctionDefinitionToken extends ExecutionToken{
    LinkedList<String> args = new LinkedList<String>();
    LinkedList<ExecutionToken> toDo;
    String name;
    public FunctionDefinitionToken(Token t, String name, LinkedList<String> args, LinkedList<ExecutionToken> toDo){
        super(t);
        this.args = args;
        this.toDo = toDo;
        this.name = name;
    }

    public ObjectType execute(){
        Executor.setVariable(name, new FunctionType(name, new CustomFunction(this)));
        return new VoidType();
    }

    public static void addFun(String funName, FunctionDefinitionToken fun){
        Executor.setVariable(funName, new FunctionType(funName, new CustomFunction(fun)));
    }

    public ObjectType execute(LinkedList<ObjectType> funcArgs) throws ExecutionError {
        Executor.addToStack(this);
        for (int i = 0; i < args.size(); ++i){
            Executor.setVariable(args.get(i), funcArgs.get(i));
        }
        ObjectType result = new VoidType();
        for (int i = 0; i < toDo.size(); ++i){
            result = toDo.get(i).execute();
        }
        Executor.removeFromStack();
        return result;
    }

    public String getName(){
        return name;
    }
}
