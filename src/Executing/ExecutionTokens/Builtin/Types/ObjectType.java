package Executing.ExecutionTokens.Builtin.Types;

import Executing.ExecutionTokens.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class ObjectType{
    HashMap<String, ObjectType> members = new HashMap<String, ObjectType>();

    public ObjectType() {

    }

    public ObjectType(HashMap<String, ObjectType> members) {
        this.members = members;
    }

    public static ObjectType getMember(ObjectType object, String name) {
        return object.getMember(name);
    }

    public ObjectType getMember(String name){
        if (this.members.containsKey(name)){
            ObjectType member = this.members.get(name);
            if(member.getClass() == FunctionType.class){
                return new BoundMethod(this, (FunctionType) member, name);
            }
            else
                return member;
        }
        else {
            ObjectType member = this.getMember("__class__").getMember(name);
            if (member.getClass() == FunctionType.class) {
                return new BoundMethod(this, (FunctionType) member, name);
            } else
                return member;
        }
    }

    public ObjectType call(LinkedList<ObjectType> args) throws ExecutionError {
        if (members.containsKey("__call__"))
            return members.get("__call__").call(args);
        else
            return new ErrorType();
    }

    public ObjectType call(ObjectType arg) throws ExecutionError {
        if (members.containsKey("__call__"))
            return members.get("__call__").call(arg);
        else
            return new ErrorType();
    }

    public ObjectType call() throws ExecutionError {
        if (members.containsKey("__call__"))
            return members.get("__call__").call();
        else
            return new ErrorType();
    }

    public static ObjectType setMember(ObjectType object, String name, ObjectType member) {
        object.members.put(name, member);
        return VoidType.voidObject;
    }

    public ObjectType setMember(String name, ObjectType member) {
        this.members.put(name, member);
        return VoidType.voidObject;
    }

    public ObjectType execute(){
        return this;
    }

    public HashMap<String, ObjectType> getMembers(){
        return members;
    }

    public ObjectType getType(){
        return this.getMember("__class__");
    }

    public boolean equals(ObjectType other){
        boolean result = false;
        if (this.getType().equals(other.getType()))
            if(this.getMembers().equals(other.getMembers()))
                result = true;
        return result;
    }

}
