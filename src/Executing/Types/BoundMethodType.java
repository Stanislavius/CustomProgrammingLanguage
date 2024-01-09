package Executing.Types;

import java.util.LinkedList;
import java.util.List;

public class BoundMethodType extends FunctionType implements WrapperInterface{
    ObjectType object;
    public BoundMethodType(ObjectType object, FunctionType function, String name){
        super(name, function.getCallable());
        this.object = object;
    }
    public ObjectType execute(LinkedList<ObjectType> args) throws ExecutionException {
        args.add(0, object);
        return super.call(args);
    }

    public ObjectType call(LinkedList<ObjectType> args) throws ExecutionException {
        return this.execute(args);
    }

    public ObjectType call(ObjectType arg) throws ExecutionException {
        LinkedList<ObjectType> args = new LinkedList<ObjectType>();
        args.add(arg);
        return this.execute(args);
    }

    public ObjectType call() throws ExecutionException {
        LinkedList<ObjectType> args = new LinkedList<ObjectType>();
        return this.execute(args);
    }

    public ObjectType call(ObjectType... args) throws ExecutionException {
        return this.execute(new LinkedList<ObjectType>(List.of(args)));
    }

    public String toString(){
        try {
            return "<BoundMethod " + this.getMemberNoBound("__name__") + " for object of "
                    + object.getMember("__class__") + ">";
        }
        catch (ExecutionException e){
            return "ERROR IN toString method of BoundMethod, should not happen under any circumstances";
        }
    }
}
