package Executing.Types;

import Executing.Executor;

import java.util.LinkedList;

public class BuiltinFunctions {
    public static void createFunctions(){
        FunctionType print = new FunctionType("print", new SourceFunctionType(){
            public ObjectType execute(LinkedList<ObjectType> args) throws ExecutionException {
                ObjectType arg = args.get(0);
                StringType resultObj = ((StringType)arg.getMember("__class__").getMember("__str__").call(args));
                System.out.println(resultObj.getValue());
                return resultObj;
            }
        });
        Executor.setVariable("print", print);

        FunctionType abs = new FunctionType("abs", new SourceFunctionType(){
            public ObjectType execute(LinkedList<ObjectType> args) throws ExecutionException {
                return args.get(0).getMember("__class__").getMember("__abs__").call(args.get(0));
            }
        });

        Executor.setVariable("abs", abs);


        FunctionType len = new FunctionType("len", new SourceFunctionType(){
            public ObjectType execute(LinkedList<ObjectType> args) throws ExecutionException {
                return args.get(0).getMember("__class__").getMember("__len__").call(args.get(0));
            }
        });

        Executor.setVariable("len", len);

        FunctionType hash = new FunctionType("hash", new SourceFunctionType(){
            public ObjectType execute(LinkedList<ObjectType> args) throws ExecutionException {
                return args.get(0).getMember("__class__").getMember("__hash__").call(args.get(0));
            }
        });

        Executor.setVariable("hash", hash);


        FunctionType type = new FunctionType("type", new SourceFunctionType(){
            public ObjectType execute(LinkedList<ObjectType> args) throws ExecutionException {
                return args.get(0).getMemberOfObjectNoBound("__class__");
            }
        });

        Executor.setVariable("type", type);

    }
}
