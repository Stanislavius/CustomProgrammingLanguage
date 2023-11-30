package Executing.ExecutionTokens.Builtin.Types;

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

    public ObjectType call(LinkedList<ObjectType> args){
        if (dict.containsKey("__call__"))
            return dict.get(new StringType("__call__")).call(args);
        else
            return new ErrorType();
    }

    public ObjectType call(ObjectType arg){
        if (dict.containsKey("__call__"))
            return dict.get(new StringType("__call__")).call(arg);
        else
            return new ErrorType();
    }

    public ObjectType call(){
        if (dict.containsKey("__call__"))
            return dict.get(new StringType("__call__")).call();
        else
            return new ErrorType();
    }

}
