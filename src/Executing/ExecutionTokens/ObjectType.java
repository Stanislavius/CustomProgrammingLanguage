package Executing.ExecutionTokens;

import Executing.ExecutionExceptions.ExecutionException;
import Executing.ReturnType;
import Executing.ReturnValue;
import Lexing.Token;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class ObjectType extends ExecutionToken{
    ClassDefinitionToken cls;
    HashMap<String, ExecutionToken> members = new HashMap<String, ExecutionToken>();
    HashMap<String, ReturnValue> variables = new HashMap<String, ReturnValue>();

    public ObjectType(Token t, ClassDefinitionToken cls) {
        super(t);
        this.cls = cls;
        HashMap<String, ExecutionToken> clsMembers = cls.getMembers();
        Set<String> keys = clsMembers.keySet();
        Iterator iter = keys.iterator();
        while (iter.hasNext()){
            String key = (String)iter.next();
            if (clsMembers.get(key).getClass() == FunctionDefinitionToken.class){
                members.put(key, clsMembers.get(key));
            }
        }
    }

    public static ReturnValue accessMember(ReturnValue object, ExecutionToken member) throws ExecutionException {
        if (member.getClass() == FunctionToken.class){
            FunctionToken ft = (FunctionToken)member;
            String functionName = ft.getToken().getValue();
            ObjectType objectValue = (ObjectType)object.getValue();
            if (objectValue.getMembers().containsKey(functionName)){
                FunctionDefinitionToken fdt = (FunctionDefinitionToken) objectValue.getMembers().get(functionName);
                return fdt.execute(ft.executeArgs());
            }

        }
        if (member.getClass() == VariableExecutionToken.class){
            VariableExecutionToken vet = (VariableExecutionToken) member;
            String variableName  = vet.getToken().getValue();
            ObjectType objectValue = (ObjectType)object.getValue();
            return objectValue.getVariable(variableName);


        }
        return null;
    }

    public static ReturnValue setMember(ReturnValue object, ExecutionToken member) throws ExecutionException {
        if (member.getClass() == FunctionToken.class){
            FunctionToken ft = (FunctionToken)member;
            String functionName = ft.getToken().getValue();
            ObjectType objectValue = (ObjectType)object.getValue();
            if (objectValue.getMembers().containsKey(functionName)){
                FunctionDefinitionToken fdt = (FunctionDefinitionToken) objectValue.getMembers().get(functionName);
                return fdt.execute(ft.executeArgs());
            }

        }
        if (member.getClass() == VariableExecutionToken.class){
            VariableExecutionToken vet = (VariableExecutionToken) member;
            String variableName  = vet.getToken().getValue();
            ObjectType objectValue = (ObjectType)object.getValue();
            objectValue.getVariable(variableName);

        }
        return null;
    }

    public ReturnValue execute(){
        return new ReturnValue(this, ReturnType.OBJECT);
    }

    public HashMap<String, ExecutionToken> getMembers(){
        return members;
    }

    public void setVariable(String name, ReturnValue value){
        variables.put(name, value);
    }

    public ReturnValue getVariable(String name){
        return variables.get(name);
    }

}
