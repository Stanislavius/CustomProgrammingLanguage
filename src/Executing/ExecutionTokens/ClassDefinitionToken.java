package Executing.ExecutionTokens;

import Executing.ExecutionExceptions.ExecutionException;
import Executing.Executor;
import Executing.ReturnType;
import Executing.ReturnValue;
import Lexing.Token;

import java.util.HashMap;
import java.util.LinkedList;

public class ClassDefinitionToken extends ExecutionToken{
    static HashMap<String, ClassDefinitionToken> definedClasses = new HashMap<String, ClassDefinitionToken>();
    HashMap<String, ExecutionToken> members = new HashMap<String, ExecutionToken>();
    FunctionDefinitionToken init;
    LinkedList<ExecutionToken> toDo;
    String name;
    public ClassDefinitionToken(Token t, String name, LinkedList<ExecutionToken> toDo){
        super(t);
        this.toDo = toDo;
        this.name = name;
        for(int i = 0; i < toDo.size(); ++i){
            if (toDo.get(i).getClass() == FunctionDefinitionToken.class){
                FunctionDefinitionToken ft = (FunctionDefinitionToken)toDo.get(i);
                String functionName = ft.getName();
                if (functionName.equals("__init__")){
                    init = ft;
                }
                else
                    members.put(functionName, ft);
            }
        }
        FunctionDefinitionToken.addFun(this.name,null);

    }

    public static ReturnValue create(ClassDefinitionToken cls, Token token){
        return new ReturnValue(new ObjectType(token, cls), ReturnType.OBJECT);
    }

    public ReturnValue create(Token token){
        return new ReturnValue(new ObjectType(token, this), ReturnType.OBJECT);
    }

    public ReturnValue execute(){
        definedClasses.put(name, this);
        return new ReturnValue(null, ReturnType.VOID);
    }

    public HashMap<String, ExecutionToken> getMembers(){
        return members;
    }

    public ReturnValue execute(LinkedList<ReturnValue> args, Token t) throws ExecutionException {
        ReturnValue newObject = create(this, t);
        args.add(0, newObject);
        init.execute(args);
        return newObject;
    }

    public static boolean contains(String name){
        return definedClasses.containsKey(name);
    }

    public static ClassDefinitionToken get(String name){
        return definedClasses.get(name);
    }
}
