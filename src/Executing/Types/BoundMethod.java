package Executing.Types;

import java.util.LinkedList;
import java.util.List;

public class BoundMethod extends FunctionType implements WrapperInterface{
    ObjectType object;
    public BoundMethod(ObjectType object, FunctionType function, String name){
        super(name, function.getCallable());
        this.object = object;
    }
    public ObjectType execute(LinkedList<ObjectType> args) throws ExecutionError {
        args.add(0, object);
        return super.call(args);
    }

    public ObjectType call(LinkedList<ObjectType> args) throws ExecutionError {
        return this.execute(args);
    }

    public ObjectType call(ObjectType arg) throws ExecutionError {
        LinkedList<ObjectType> args = new LinkedList<ObjectType>();
        args.add(arg);
        return this.execute(args);
    }

    public ObjectType call() throws ExecutionError {
        LinkedList<ObjectType> args = new LinkedList<ObjectType>();
        return this.execute(args);
    }

    public ObjectType call(ObjectType... args) throws ExecutionError {
        return this.execute(new LinkedList<ObjectType>(List.of(args)));
    }
}
