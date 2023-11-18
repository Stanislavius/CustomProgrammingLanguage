package Executing.ExecutionTokens.Builtin.Types;

import Executing.Executor;
import Lexing.Token;

import java.util.LinkedList;

public class FloatType extends ObjectType {
    Float value;
    static ClassType type;
    public static void createType()
    {
        type = new ClassType();
        type.setMember("__name__", new StringType("float"));
        type.setMember("__add__", new FunctionType("__add__", new SourceFunction(){
            public ObjectType execute(LinkedList<ObjectType> args){
                float v1 = ((FloatType)(args.get(0))).getFloat();
                ObjectType val2 = args.get(1);
                if (val2.getType().toString().equals("int")) {
                    int v2 = ((IntType) (val2)).getInt();
                    return new FloatType(v1+v2);
                }
                if (val2.getType().toString().equals("float")) {
                    float v2 = ((FloatType) (val2)).getFloat();
                    return new FloatType(v1+v2);
                }
                return VoidType.voidObject;
            }
        }
        ));

        type.setMember("__sub__", new FunctionType("__sub__", new SourceFunction(){
            public ObjectType execute(LinkedList<ObjectType> args){
                float v1 = ((FloatType)(args.get(0))).getFloat();
                ObjectType val2 = args.get(1);
                if (val2.getType().toString().equals("int")) {
                    int v2 = ((IntType) (val2)).getInt();
                    return new FloatType(v1-v2);
                }
                if (val2.getType().toString().equals("float")) {
                    float v2 = ((FloatType) (val2)).getFloat();
                    return new FloatType(v1-v2);
                }
                return VoidType.voidObject;
            }
        }
        ));

        type.setMember("__mul__", new FunctionType("__mul__", new SourceFunction(){
            public ObjectType execute(LinkedList<ObjectType> args){
                float v1 = ((FloatType)(args.get(0))).getFloat();
                ObjectType val2 = args.get(1);
                if (val2.getType().toString().equals("int")) {
                    int v2 = ((IntType) (val2)).getInt();
                    return new FloatType(v1*v2);
                }
                if (val2.getType().toString().equals("float")) {
                    float v2 = ((FloatType) (val2)).getFloat();
                    return new FloatType(v1*v2);
                }
                return VoidType.voidObject;
            }
        }
        ));

        type.setMember("__div__", new FunctionType("__div__", new SourceFunction(){
            public ObjectType execute(LinkedList<ObjectType> args){
                float v1 = ((FloatType)(args.get(0))).getFloat();
                ObjectType val2 = args.get(1);
                if (val2.getType().toString().equals("int")) {
                    int v2 = ((IntType) (val2)).getInt();
                    return new FloatType(v1/v2);
                }
                if (val2.getType().toString().equals("float")) {
                    float v2 = ((FloatType) (val2)).getFloat();
                    return new FloatType(v1/v2);
                }
                return VoidType.voidObject;
            }
        }
        ));

        type.setMember("__neg__", new FunctionType("__neg__", new SourceFunction(){
            public ObjectType execute(LinkedList<ObjectType> args){
                float v1 = ((FloatType)(args.get(0))).getFloat();
                return new FloatType(-v1);
            }
        }
        ));

        type.setMember("__str__", new FunctionType("__str__", new SourceFunction(){
            public ObjectType execute(LinkedList<ObjectType> args){
                float v1 = ((FloatType)(args.get(0))).getFloat();
                return new StringType(v1+"");
            }
        }
        ));
        Executor.setVariable("float", type);
    }
    public FloatType(Float value) {
        this.setMember("__class__", type);
        this.value = value;
    }

    public float getFloat(){
        return value;
    }
    public String toString(){
        return "" + value;
    }
}
