package Executing;

import Executing.ExecutionTokens.FunctionDefinitionET;
import Executing.Types.ObjectType;

import java.util.HashMap;
import java.util.LinkedList;

public class RecursionCallTracker {
    HashMap<FunctionDefinitionET, Integer> processedObjects;
    public RecursionCallTracker(){
        processedObjects = new HashMap<FunctionDefinitionET, Integer>();
    }
    public void add(FunctionDefinitionET value){
        if (processedObjects.containsKey(value))
            processedObjects.put(value, processedObjects.get(value) + 1);
        else
            processedObjects.put(value, 1);
    }

    public void exit(FunctionDefinitionET value){
        if (processedObjects.containsKey(value)) {
            processedObjects.put(value, processedObjects.get(value) - 1);
            if (processedObjects.get(value) == 0){
                processedObjects.remove(value);
            }
        }
        else {
            //THROW EXCEPTION
        }
    }
    public boolean contains(FunctionDefinitionET value){
        return processedObjects.containsKey(value);
    }

    public void clear(){
        processedObjects.clear();
    }

    public boolean isEmpty(){
        return processedObjects.isEmpty();
    }

    public int get(FunctionDefinitionET value){
        return processedObjects.get(value);
    }
}
