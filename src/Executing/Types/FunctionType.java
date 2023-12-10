package Executing.Types;

import java.util.LinkedList;

public class FunctionType extends ObjectType{
    WrapperInterface call = null;
    static ClassType type;
    public static void createType()
    {
        type = new ClassType();
        type.setMember("__name__", new StringType("Function"));

    }
    public FunctionType(String name, WrapperInterface call){
        this.setMember("__name__", new StringType(name));
        this.setMember("__class__", type);
        this.call = call;
    }

    public ObjectType call(LinkedList<ObjectType> args) throws ExecutionException {
        return call.execute(args);
    }

    public ObjectType call(ObjectType arg) throws ExecutionException {
        LinkedList<ObjectType> args = new LinkedList<ObjectType>();
        args.add(arg);
        return call.execute(args);
    }

    public ObjectType call() throws ExecutionException {
        LinkedList<ObjectType> args = new LinkedList<ObjectType>();
        return call.execute(args);
    }

    public WrapperInterface getCallable(){
        return this.call;
    }

    public boolean equals(FunctionType other){
        return false; //TODO: equality for object
    }

    public boolean equals(Object other){
        if (other.getClass() == FunctionType.class)
            return this.equals((FunctionType) other);
        else
            return false;
    }

}
