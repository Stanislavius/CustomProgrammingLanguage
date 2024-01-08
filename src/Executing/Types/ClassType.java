package Executing.Types;

import java.util.HashMap;
import java.util.LinkedList;

public class ClassType extends ObjectType {
    static ClassType type;
    public static void createType(){
        type = new ClassType();
        type.setMember("__class__", type);
    }

    public static void createType2(){
        type.setMember("__name__", new StringType("type"));
        type.setMember("__str__", new FunctionType("__str__", new SourceFunctionType(){
            public ObjectType execute(LinkedList<ObjectType> args){
                ObjectType val1 = args.get(0);
                return new StringType("<type " + val1.toString() + ">");
            }
        }
        ));
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

    public static ClassType getTypeClass(){
        return ClassType.type;
    }

}
