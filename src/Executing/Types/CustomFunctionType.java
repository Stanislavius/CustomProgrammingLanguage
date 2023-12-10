package Executing.Types;

import Executing.ExecutionTokens.FunctionDefinitionET;

import java.util.LinkedList;

public class CustomFunctionType implements WrapperInterface {
    FunctionDefinitionET fDT;
    public CustomFunctionType(FunctionDefinitionET fDT) {
        this.fDT = fDT;
    }
    public ObjectType execute(LinkedList<ObjectType> args) throws ExecutionException {
        return fDT.execute(args);
    }
}
