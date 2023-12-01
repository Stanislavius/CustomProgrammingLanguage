package Executing.ExecutionTokens;

import Executing.ExecutionTokens.Builtin.Types.*;
import Executing.Executor;
import Lexing.Token;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class ClassDefinitionToken extends ExecutionToken{
    HashMap<String, ExecutionToken> members = new HashMap<String, ExecutionToken>();
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

    public ObjectType execute() throws ExecutionError {
        HashMap<String, ObjectType> classMembers = new HashMap<String, ObjectType>();
        CustomObject newClass = new CustomObject();
        Executor.setVariable(name, newClass);
        Iterator iter = members.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            ExecutionToken member = members.get(key);
            if (key.equals("__init__")) {
                FunctionDefinitionToken initFunc = (FunctionDefinitionToken) member;
                newClass.setMember("__call__", new FunctionType("__call__", new SourceFunction() {
                    public ObjectType execute(LinkedList<ObjectType> args) throws ExecutionError {
                        ObjectType newObject = new CustomObject();
                        args.add(0, newObject);
                        newClass.getMember("__init__").call(args);
                        newObject.setMember("__class__", newClass);
                        return newObject;
                    }
                }
                ));
                newClass.setMember("__init__", new FunctionType(name = "__init__", new CustomFunction(initFunc)));
            }
            else {
                newClass.setMember(key, member.execute());
            }
        }
        newClass.setMember("__class__", ClassType.getTypeClass());
        Executor.setVariable(name, newClass);
        return new VoidType();
    }

    public HashMap<String, ExecutionToken> getMembers(){
        return members;
    }

}
