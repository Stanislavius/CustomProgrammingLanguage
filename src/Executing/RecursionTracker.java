package Executing;

import Executing.Types.ObjectType;

import java.util.LinkedList;

public class RecursionTracker {
    LinkedList<ObjectType> processedObjects;
    public RecursionTracker(){
        processedObjects = new LinkedList<ObjectType>();
    }
    public void add(ObjectType value){
        processedObjects.add(value);
    }
    public boolean contains(ObjectType value){
        return processedObjects.contains(value);
    }

    public void clear(){
        processedObjects.clear();
    }

    public boolean isEmpty(){
        return processedObjects.isEmpty();
    }

    public boolean isFirst(ObjectType value){
        return processedObjects.getFirst() == value;
    }
}
