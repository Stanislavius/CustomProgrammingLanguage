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

    public ObjectType getMember(String name) throws ExecutionException {
        return this.getMember(new StringType(name));
    }

    public ObjectType getMemberNoBound(StringType key) throws ExecutionException {
        if (this.dict.containsKey(key)){
            ObjectType member = this.dict.get(key);
            return member;
        }
        else {
            ObjectType member = this.getMember("__class__").getMember(key.getValue());
            return member;
        }
    }

    public ObjectType getMemberNoBound(String key) throws ExecutionException {
        return this.getMemberNoBound(new StringType(key));
    }

    public ObjectType getMember(StringType key) throws ExecutionException {
        if (this.dict.containsKey(key)){
            ObjectType member = this.dict.get(key);
            if(member.getClass() == FunctionType.class){
                return new BoundMethodType(this, (FunctionType) member, key.getValue());
            }
            else
                return member;
        }
        else {
            ObjectType member = this.getMemberOfObject("__class__").getMemberOfObjectNoBound(key.getValue());
            if (member.getClass() == FunctionType.class) {
                return new BoundMethodType(this, (FunctionType) member, key.getValue());
            } else
                return member;
        }
    }

    public ObjectType getMemberOfObject(String name) throws ExecutionException {
        return getMemberOfObject(new StringType(name));
    }

    public ObjectType getMemberOfObject(StringType name) throws ExecutionException {
        ObjectType result = this.members.getOrDefault(name.getValue(), null);
        if (result == null){
            result = this.dict.get(name);
        }
        if (result == null)
            if (result == null) {
                ErrorType error = new ErrorType("NoSuchMember");
                throw new ExecutionException(error);
            }
        if(result.getClass() == FunctionType.class){
            return new BoundMethodType(this, (FunctionType) result, name.getValue());
        }
        return result;
    }

    public ObjectType getMemberOfObjectNoBound(String name) throws ExecutionException {
        return getMemberOfObjectNoBound(new StringType(name));
    }

    public ObjectType getMemberOfObjectNoBound(StringType name) throws ExecutionException {
        ObjectType result = this.members.getOrDefault(name.getValue(), null);
        if (result == null){
            result = this.dict.get(name);
        }
        if (result == null)
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

    public boolean contains(String name){
        boolean result = this.members.containsKey(name);
        if (!result)
            result = this.dict.containsKey(name);
        return result;
    }

    public String toString(){
        try {
            if (this.contains("__name__"))
                return "<class " + this.getMemberOfObjectNoBound("__name__") + ">";
            else {
                ObjectType classObject = this.getMemberOfObjectNoBound("__class__");
                return classObject.toString();
            }
        } catch (ExecutionException e) {
            return "Error in CustomObject, should never happen";
        }
    }

}
