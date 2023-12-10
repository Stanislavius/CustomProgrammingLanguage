package Executing.Types;

import Executing.ExecutionTokens.FunctionDefinitionToken;

import java.util.LinkedList;

public class CustomFunction implements WrapperInterface {
    FunctionDefinitionToken fDT;
    public CustomFunction(FunctionDefinitionToken fDT) {
        this.fDT = fDT;
    }
    public ObjectType execute(LinkedList<ObjectType> args) throws ExecutionException {
        return fDT.execute(args);
    }
}
