package Executing.ExecutionTokens.Builtin.Types;

import Executing.Executor;
import Lexing.Token;

import java.util.LinkedList;

public class StringType extends ObjectType {
    static ClassType type;
    public static void createType()
    {
        type = new ClassType();
        type.setMember("__name__", new StringType("str"));

        type.setMember("__str__", new FunctionType("__str__", new SourceFunction(){
            public ObjectType execute(LinkedList<ObjectType> args){
                StringType s1 = ((StringType)(args.get(0)));
                return s1;
            }
        }
        ));

        type.setMember("__call__", new FunctionType("__call__", new SourceFunction(){
            public ObjectType execute(LinkedList<ObjectType> args){
                ObjectType val1 = args.get(0);
                return val1.getMember("__class__").getMember("__str__").call(val1);
            }
        }
        ));

        type.setMember("__add__", new FunctionType("__add__", new SourceFunction(){
            public ObjectType execute(LinkedList<ObjectType> args){
                String v1 = ((StringType)(args.get(0))).toString();
                ObjectType val2 = args.get(1);
                return new StringType(v1 + ((StringType)val2.getMember(
                        "__class__").getMember(
                                "__str__").call(val2)).getValue());
            }
        }

        ));
        Executor.setVariable("str", type);
    }
    String value;
    public StringType(String value) {
        this.value = value;
        this.setMember("__class__", type);
    }
    public String getValue(){
        return value;
    }

    public String toString(){
        return value;
    }
}
