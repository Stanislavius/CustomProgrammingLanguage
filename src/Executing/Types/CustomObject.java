package Executing.Types;

import java.util.LinkedList;

public class CustomObject extends ObjectType{
    ClassDict dict = new ClassDict();
    public static ObjectType setMember(ObjectType object, String name, ObjectType member) {
        object.setMember(name, member);
        return VoidType.voidObject;
    }

    public ObjectType setMember(String name, ObjectType member) {
        this.dict.set(new StringType(name), member);
        return VoidType.voidObject;
    }

    public ObjectType call(LinkedList<ObjectType> args) throws ExecutionError {
        if (dict.containsKey("__call__"))
            return dict.get(new StringType("__call__")).call(args);
        else
            return new ErrorType();
    }

    public ObjectType call(ObjectType arg) throws ExecutionError {
        if (dict.containsKey("__call__"))
            return dict.get(new StringType("__call__")).call(arg);
        else
            return new ErrorType();
    }

    public ObjectType call() throws ExecutionError {
        if (dict.containsKey("__call__"))
            return dict.get(new StringType("__call__")).call();
        else
            return new ErrorType();
    }

    public ObjectType getMember(String name){
        return this.getMember(new StringType(name));
    }

    public ObjectType getMember(StringType key){
        if (this.dict.containsKey(key)){
            ObjectType member = this.dict.get(key);
            if(member.getClass() == FunctionType.class){
                return new BoundMethod(this, (FunctionType) member, key.getValue());
            }
            else
                return member;
        }
        else {
            ObjectType member = this.getMember("__class__").getMember(key.getValue());
            if (member.getClass() == FunctionType.class) {
                return new BoundMethod(this, (FunctionType) member, key.getValue());
            } else
                return member;
        }
    }

}
