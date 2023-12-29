package Executing.Types;

import java.util.LinkedList;

public class CustomObject extends ObjectType{
    DictType dict = new DictType();
    public static ObjectType setMember(ObjectType object, String name, ObjectType member) {
        object.setMember(name, member);
        return VoidType.voidObject;
    }

    public ObjectType setMember(String name, ObjectType member) {
        this.dict.set(new StringType(name), member);
        return VoidType.voidObject;
    }

    public ObjectType call(LinkedList<ObjectType> args) throws ExecutionException {
        if (dict.containsKey("__call__"))
            return dict.get(new StringType("__call__")).call(args);
        else
            return new ErrorType();
    }

    public ObjectType call(ObjectType arg) throws ExecutionException {
        if (dict.containsKey("__call__"))
            return dict.get(new StringType("__call__")).call(arg);
        else
            return new ErrorType();
    }

    public ObjectType call() throws ExecutionException {
        if (dict.containsKey("__call__"))
            return dict.get(new StringType("__call__")).call();
        else
            return new ErrorType();
    }

    public ObjectType getMember(String name){
        return this.getMember(new StringType(name));
    }

    public ObjectType getMemberNoBound(StringType key){
        if (this.dict.containsKey(key)){
            ObjectType member = this.dict.get(key);
            return member;
        }
        else {
            ObjectType member = this.getMember("__class__").getMember(key.getValue());
            return member;
        }
    }

    public ObjectType getMemberNoBound(String key){
        return this.getMemberNoBound(new StringType(key));
    }

    public ObjectType getMember(StringType key){
        if (this.dict.containsKey(key)){
            ObjectType member = this.dict.get(key);
            if(member.getClass() == FunctionType.class){
                return new BoundMethodType(this, (FunctionType) member, key.getValue());
            }
            else
                return member;
        }
        else {
            ObjectType member = this.getMember("__class__").getMember(key.getValue());
            if (member.getClass() == FunctionType.class) {
                return new BoundMethodType(this, (FunctionType) member, key.getValue());
            } else
                return member;
        }
    }

    public boolean contains(String name){
        boolean result = this.members.containsKey(name);
        if (!result)
            result = this.dict.containsKey(name);
        return result;
    }

}
