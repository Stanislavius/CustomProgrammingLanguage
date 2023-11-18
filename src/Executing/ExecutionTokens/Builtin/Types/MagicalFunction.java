package Executing.ExecutionTokens.Builtin.Types;

import java.util.Arrays;
import java.util.LinkedList;

public class MagicalFunction implements WrapperInterface {
    final static LinkedList<String> names = new LinkedList<>(Arrays.asList(
            "__add__",
            "__sub__",
            "__mul__"));
    String name;

    public MagicalFunction(String name){
        this.name = name;
    }
    public ObjectType execute(LinkedList<ObjectType> args) {
        ObjectType arg = args.get(0);
        args.removeFirst();
        return arg.getMember(name).call(args);
    }

    public static void createdMagicalFunctions(){
    }
}
