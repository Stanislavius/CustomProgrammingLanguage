package Executing.ExecutionTokens;

import Executing.Types.*;
import Executing.Executor;
import Lexing.Token;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class ClassDefinitionET extends ExecutionToken{
    HashMap<String, ExecutionToken> members = new HashMap<String, ExecutionToken>();
    LinkedList<ExecutionToken> toDo;
    String name;
    public ClassDefinitionET(Token t, String name, LinkedList<ExecutionToken> toDo){
        super(t);
        this.toDo = toDo;
        this.name = name;
        for(int i = 0; i < toDo.size(); ++i){
            if (toDo.get(i).getClass() == FunctionDefinitionET.class){
                FunctionDefinitionET ft = (FunctionDefinitionET)toDo.get(i);
                String functionName = ft.getName();
                members.put(functionName, ft);
            }
            if (toDo.get(i).getClass() == AssignmentET.class){
                FunctionDefinitionET ft = (FunctionDefinitionET)toDo.get(i);
                String functionName = ft.getName();
                members.put(functionName, ft);
            }
        }
    }

    public ObjectType execute() throws ExecutionException {
        HashMap<String, ObjectType> classMembers = new HashMap<String, ObjectType>();
        CustomObject newClass = new CustomObject();
        Executor.setVariable(name, newClass);
        Iterator iter = members.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            ExecutionToken member = members.get(key);
            if (key.equals("__init__")) {
                FunctionDefinitionET initFunc = (FunctionDefinitionET) member;
                newClass.setMember("__call__", new FunctionType("__call__", new SourceFunctionType() {
                    public ObjectType execute(LinkedList<ObjectType> args) throws ExecutionException {
                        ObjectType newObject = new CustomObject();
                        args.add(0, newObject);
                        newClass.getMemberNoBound("__init__").call(args);
                        newObject.setMember("__class__", newClass);
                        return newObject;
                    }
                }
                ));
                newClass.setMember("__init__", new FunctionType("__init__", new CustomFunctionType(initFunc)));
            }
            else {
                if (member.getClass() == FunctionDefinitionET.class){
                    FunctionDefinitionET func = (FunctionDefinitionET) member;
                    newClass.setMember(key, new FunctionType(key, new CustomFunctionType(func)));
                }
                else
                    newClass.setMember(key, member.execute());
            }
        }
        newClass.setMember("__class__", ClassType.getTypeClass());
        newClass.setMember("__name__", new StringType(name));
        newClass.setMember("__str__", new FunctionType("__str__", new SourceFunctionType() {
            public ObjectType execute(LinkedList<ObjectType> args) throws ExecutionException {
                return new StringType(name);
            }
        }
        ));

        Executor.setVariable(name, newClass);
        return new VoidType();
    }

    public HashMap<String, ExecutionToken> getMembers(){
        return members;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("class");
        sb.append(" ");
        sb.append(name);
        sb.append("\n");
        for(int i = 0; i < toDo.size(); ++i){
            sb.append("\t");
            sb.append(toDo.get(i).toString());
        }
        return sb.toString();
    }

}
