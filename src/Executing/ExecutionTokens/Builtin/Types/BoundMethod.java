package Executing.ExecutionTokens.Builtin.Types;

import java.util.LinkedList;

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
}
