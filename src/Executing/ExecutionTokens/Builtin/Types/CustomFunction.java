package Executing.ExecutionTokens.Builtin.Types;

import Executing.ExecutionTokens.FunctionDefinitionToken;

import java.util.LinkedList;

public class CustomFunction implements WrapperInterface {
    FunctionDefinitionToken fDT;
    public CustomFunction(FunctionDefinitionToken fDT) {
        this.fDT = fDT;
    }
    public ObjectType execute(LinkedList<ObjectType> args){
        return fDT.execute(args);
    }
}
