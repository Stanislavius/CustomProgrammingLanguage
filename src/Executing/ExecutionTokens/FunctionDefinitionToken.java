package Executing.ExecutionTokens;
import Executing.ExecutionExceptions.ExecutionException;
import Executing.Executor;
import Executing.ReturnType;
import Executing.ReturnValue;
import Lexing.Token;

import java.util.HashMap;
import java.util.LinkedList;

public class FunctionDefinitionToken extends ExecutionToken{
    static HashMap<String, FunctionDefinitionToken> definedFunctions = new HashMap<String, FunctionDefinitionToken>();
    LinkedList<String> args = new LinkedList<String>();
    LinkedList<ExecutionToken> toDo;
    String name;
    public FunctionDefinitionToken(Token t, String name, LinkedList<String> args, LinkedList<ExecutionToken> toDo){
        super(t);
        this.args = args;
        this.toDo = toDo;
        this.name = name;
    }

    public ReturnValue execute(){
        definedFunctions.put(name, this);
        return new ReturnValue(null, ReturnType.VOID);
    }

    public static void addFun(String funName, FunctionDefinitionToken fun){
        definedFunctions.put(funName, fun);
    }

    public ReturnValue execute(LinkedList<ReturnValue> funcArgs) throws ExecutionException {
        Executor.addToStack(this);
        for (int i = 0; i < args.size(); ++i){
            Executor.setVariable(args.get(i), funcArgs.get(i));
        }
        ReturnValue result = new ReturnValue(null, ReturnType.VOID);
        for (int i = 0; i < toDo.size(); ++i){
            result = toDo.get(i).execute();
        }
        Executor.removeFromStack();
        return result;
    }

    public static boolean contains(String name){
        return definedFunctions.containsKey(name);
    }

    public static FunctionDefinitionToken get(String name){
        return definedFunctions.get(name);
    }

    public String getName(){
        return name;
    }
}
