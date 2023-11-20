package Executing.ExecutionTokens;

import Executing.ExecutionExceptions.ExecutionException;
import Executing.ExecutionTokens.Builtin.Types.ClassType;
import Executing.ExecutionTokens.Builtin.Types.ObjectType;
import Executing.ExecutionTokens.Builtin.Types.VoidType;
import Executing.Executor;
import Lexing.Token;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class ClassDefinitionToken extends ExecutionToken{
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
                members.put(functionName, ft);
            }
            if (toDo.get(i).getClass() == AssignmentToken.class){
                FunctionDefinitionToken ft = (FunctionDefinitionToken)toDo.get(i);
                String functionName = ft.getName();
                members.put(functionName, ft);
            }
        }

    }

    public ObjectType execute(){
        HashMap<String, ObjectType> classMembers = new HashMap<String, ObjectType>();
        Iterator iter = members.keySet().iterator();
        while (iter.hasNext()){
            String key = (String)iter.next();
            ExecutionToken member = members.get(key);
            classMembers.put(name, member.execute());
        }
        Executor.setVariable(name, new ClassType(classMembers));
        return new VoidType();
    }

    public HashMap<String, ExecutionToken> getMembers(){
        return members;
    }

    public ObjectType execute(LinkedList<ObjectType> args, Token t) throws ExecutionException {
        ObjectType newObject = new ObjectType();
        args.add(0, newObject);
        init.execute(args);
        return newObject;
    }

}
