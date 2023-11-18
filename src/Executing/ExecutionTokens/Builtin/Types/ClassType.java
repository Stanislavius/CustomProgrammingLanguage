package Executing.ExecutionTokens.Builtin.Types;

import Executing.ExecutionTokens.ClassDefinitionToken;
import Executing.ExecutionTokens.ExecutionToken;

import java.util.HashMap;
import java.util.LinkedList;

public class ClassType extends ObjectType {
    static ClassType type;
    public static void createType(){
        type = new ClassType();
        type.setMember("__class__", type);
        type.setMember("__name__", new StringType("type"));
    }

    public ClassType() {
        setMember("__class__", type);
    }

    public ClassType(HashMap<String, ObjectType> members){
        this.members = members;
        setMember("__class__", type);
    }

    public String toString(){
        return ((StringType)this.getMember("__name__")).getValue();
    }

    public ObjectType getMember(String name){
        return members.get(name);
    }

}
