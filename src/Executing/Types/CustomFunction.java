package Executing.Types;

import Executing.ExecutionTokens.FunctionDefinitionET;

import java.util.LinkedList;

public class CustomFunction implements WrapperInterface {
    FunctionDefinitionET fDT;
    public CustomFunction(FunctionDefinitionET fDT) {
        this.fDT = fDT;
    }
    public ObjectType execute(LinkedList<ObjectType> args) throws ExecutionException {
        return fDT.execute(args);
    }
}
