package Executing.Types;

import java.util.LinkedList;

public interface WrapperInterface {
    public ObjectType execute(LinkedList<ObjectType> args) throws ExecutionError;
}
