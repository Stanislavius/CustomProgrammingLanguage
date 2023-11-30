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

        type.setMember("__mul__", new FunctionType("__mul__", new SourceFunction(){
            public ObjectType execute(LinkedList<ObjectType> args){
                String v1 = ((StringType)(args.get(0))).toString();
                int v2 = ((IntType) args.get(1)).getInt();
                StringBuilder sb = new StringBuilder();
                for(int i = 0; i < v2; ++i)
                    sb.append(v1);
                return new StringType(sb.toString());
            }
        }

        ));

        type.setMember("__len__", new FunctionType("__len__", new SourceFunction(){
            public ObjectType execute(LinkedList<ObjectType> args){
                String v1 = ((StringType)(args.get(0))).value;
                return new IntType(v1.length());
            }
        }

        ));

        type.setMember("__eq__", new FunctionType("__eq__", new SourceFunction(){
            public ObjectType execute(LinkedList<ObjectType> args){
                String v1 = ((StringType)(args.get(0))).getValue();
                String v2 = ((StringType)(args.get(1))).getValue();
                boolean result = v1.equals(v2);
                if (result)
                    return new IntType(1);
                else
                    return new IntType(0);
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

    public boolean equals(Object other){
        if (other.getClass().equals(this.getClass())){
            String v = ((StringType) other).getValue();
            return v.equals(this.value);
        }
        else
            return false;
    }

    public int hashCode(){
        return this.value.hashCode();
    }
}
