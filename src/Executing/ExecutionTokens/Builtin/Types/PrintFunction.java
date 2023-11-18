package Executing.ExecutionTokens.Builtin.Types;

import java.util.LinkedList;

public class PrintFunction implements WrapperInterface {
    String name;

    public PrintFunction(){
        name = "print";
    }
    public ObjectType execute(LinkedList<ObjectType> args) {
        ObjectType arg = args.get(0);
        args.removeFirst();
        StringType resultObj = ((StringType)arg.getMember("__str__").call(args));
        System.out.println(resultObj.getValue());
        return resultObj;
    }
}
