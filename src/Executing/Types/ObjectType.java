package Executing.Types;

import Executing.Executor;

import java.util.HashMap;
import java.util.LinkedList;

public class ObjectType{
    HashMap<String, ObjectType> members = new HashMap<String, ObjectType>();

    public ObjectType() {

    }

    public ObjectType(HashMap<String, ObjectType> members) {
        this.members = members;
    }

    public static ObjectType getMember(ObjectType object, String name) throws ExecutionException {
        return object.getMember(name);
    }

    public ObjectType getMemberNoBound(String name) throws ExecutionException {
        if (this.members.containsKey(name)){
            ObjectType member = this.members.get(name);
            return member;
        }
        else {
            ObjectType member = this.getMember("__class__").getMember(name);
            return member;
        }
    }

    public ObjectType getMember(String name) throws ExecutionException {
        if (this.members.containsKey(name)){
            ObjectType member = this.members.get(name);
            if(member.getClass() == FunctionType.class){
                return new BoundMethodType(this, (FunctionType) member, name);
            }
            else
                return member;
        }
        else {
            ObjectType member = this.getMember("__class__").getMember(name);
            if (member == null){
                throw new ExecutionException(new ErrorType("No such member"));
            }
            if (member.getClass() == FunctionType.class) {
                return new BoundMethodType(this, (FunctionType) member, name);
            } else
                return member;
        }
    }

    public ObjectType getMemberOfObject(String name) throws ExecutionException {
        ObjectType result = this.members.getOrDefault(name, null);
        if (result == null) {
            ErrorType error = new ErrorType("NoSuchMember");
            throw new ExecutionException(error);
        }
        if(result.getClass() == FunctionType.class){
            return new BoundMethodType(this, (FunctionType) result, name);
        }
        return result;
    }

    public ObjectType getMemberOfObjectNoBound(String name) throws ExecutionException {
        ObjectType result = this.members.getOrDefault(name, null);
        if (result == null) {
            ErrorType error = new ErrorType("NoSuchMember");
            throw new ExecutionException(error);
        }
        return result;
    }

    public ObjectType getMemberOfClass(String name) throws ExecutionException {
        return this.getMember("__class__").getMemberOfObject(name);
    }

    public ObjectType getMemberOfClassNoBound(String name) throws ExecutionException {
        return this.getMember("__class__").getMemberOfObjectNoBound(name);
    }

    public ObjectType call(LinkedList<ObjectType> args) throws ExecutionException {
        if (members.containsKey("__call__"))
            return members.get("__call__").call(args);
        else
            return new ErrorType();
    }

    public ObjectType call(ObjectType arg) throws ExecutionException {
        if (members.containsKey("__call__"))
            return members.get("__call__").call(arg);
        else
            return new ErrorType();
    }

    public ObjectType call(ObjectType... args) throws ExecutionException {
        if (members.containsKey("__call__"))
            return members.get("__call__").call(args);
        else
            return new ErrorType();
    }


    public ObjectType call() throws ExecutionException {
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

    public ObjectType getType() throws ExecutionException {
        return this.getMember("__class__");
    }

    public boolean equals(ObjectType other) throws ExecutionException {
        boolean result = false;
        if (this.getType().equals(other.getType()))
            if(this.getMembers().equals(other.getMembers()))
                result = true;
        return result;
    }

    public boolean contains(String name){
        return this.members.containsKey(name);
    }
}
