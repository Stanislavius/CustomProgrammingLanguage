package Executing.ExecutionTokens.Builtin.Types;


import Executing.ExecutionTokens.Builtin.Types.*;
import Executing.Executor;

import java.util.LinkedList;

public class IntType extends ObjectType {
    int value;
    static ClassType type;

    public static void createType(){
        type = new ClassType();
        type.setMember("__name__", new StringType("int"));
        type.setMember("__call__", new FunctionType("__call__", new SourceFunction(){
            public ObjectType execute(LinkedList<ObjectType> args){
                ObjectType val1 = args.get(0);
                switch (val1.getType().toString()) {
                    case "int":
                        return val1;
                    case "str":
                        StringType sT = (StringType) val1;
                        return new IntType(Integer.parseInt(sT.getValue()));
                    case "float":
                        FloatType fT = (FloatType) val1;
                        return new IntType((int)fT.getFloat());
                    default:
                        return val1.getMember("__int__").call();
                }
            }
        }
        ));

        type.setMember("__add__", new FunctionType("__add__", new SourceFunction(){
            public ObjectType execute(LinkedList<ObjectType> args){
                int v1 = ((IntType)(args.get(0))).getInt();
                ObjectType val2 = args.get(1);
                if (val2.getType().toString().equals("int")) {
                    int v2 = ((IntType) (val2)).getInt();
                    return new IntType(v1+v2);
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
                int v1 = ((IntType)(args.get(0))).getInt();
                ObjectType val2 = args.get(1);
                if (val2.getType().toString().equals("int")) {
                    int v2 = ((IntType) (val2)).getInt();
                    return new IntType(v1-v2);
                }
                if (val2.getType().toString().equals("float")) {
                    float v2 = ((FloatType) (val2)).getFloat();
                    return new FloatType(v1-v2);
                }
                return VoidType.voidObject;
            }
        }
        ));

        type.setMember("__eq__", new FunctionType("__eq__", new SourceFunction(){
            public ObjectType execute(LinkedList<ObjectType> args){
                int v1 = ((IntType)(args.get(0))).getInt();
                ObjectType val2 = args.get(1);
                if (val2.getType().toString().equals("int")) {
                    int v2 = ((IntType) (val2)).getInt();
                    if (v1 == v2)
                        return new IntType(1);
                    else
                        return new IntType(0);

                }
                if (val2.getType().toString().equals("float")) {
                    float v2 = ((FloatType) (val2)).getFloat();
                    if (v1 == v2)
                        return new IntType(1);
                    else
                        return new IntType(0);
                }
                return VoidType.voidObject;
            }
        }
        ));

        type.setMember("__lt__", new FunctionType("__lt__", new SourceFunction(){
            public ObjectType execute(LinkedList<ObjectType> args){
                int v1 = ((IntType)(args.get(0))).getInt();
                ObjectType val2 = args.get(1);
                if (val2.getType().toString().equals("int")) {
                    int v2 = ((IntType) (val2)).getInt();
                    if (v1 < v2)
                        return new IntType(1);
                    else
                        return new IntType(0);
                }
                if (val2.getType().toString().equals("float")) {
                    float v2 = ((FloatType) (val2)).getFloat();
                    if (v1 < v2)
                        return new IntType(1);
                    else
                        return new IntType(0);
                }
                return VoidType.voidObject;
            }
        }
        ));

        type.setMember("__gt__", new FunctionType("__gt__", new SourceFunction(){
            public ObjectType execute(LinkedList<ObjectType> args){
                int v1 = ((IntType)(args.get(0))).getInt();
                ObjectType val2 = args.get(1);
                if (val2.getType().toString().equals("int")) {
                    int v2 = ((IntType) (val2)).getInt();
                    if (v1 > v2)
                        return new IntType(1);
                    else
                        return new IntType(0);
                }
                if (val2.getType().toString().equals("float")) {
                    float v2 = ((FloatType) (val2)).getFloat();
                    if (v1 > v2)
                        return new IntType(1);
                    else
                        return new IntType(0);
                }
                return VoidType.voidObject;
            }
        }
        ));

        type.setMember("__mul__", new FunctionType("__mul__", new SourceFunction(){
            public ObjectType execute(LinkedList<ObjectType> args){
                int v1 = ((IntType)(args.get(0))).getInt();
                ObjectType val2 = args.get(1);
                if (val2.getType().toString().equals("int")) {
                    int v2 = ((IntType) (val2)).getInt();
                    return new IntType(v1*v2);
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
                int v1 = ((IntType)(args.get(0))).getInt();
                ObjectType val2 = args.get(1);
                if (val2.getType().toString().equals("int")) {
                    int v2 = ((IntType) (val2)).getInt();
                    return new FloatType(((float)v1)/((float)v2));
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
                int v1 = ((IntType)(args.get(0))).getInt();
                return new IntType(-v1);
            }
        }
        ));

        type.setMember("__hash__", new FunctionType("__hash__", new SourceFunction(){
            public ObjectType execute(LinkedList<ObjectType> args){
                int v1 = ((IntType)(args.get(0))).hashCode();
                return new IntType(v1);
            }
        }
        ));

        type.setMember("__str__", new FunctionType("__str__", new SourceFunction(){
            public ObjectType execute(LinkedList<ObjectType> args){
                int k = 4;
                int v1 = ((IntType)(args.get(0))).getInt();
                return new StringType(v1+"");
            }
        }
        ));

        Executor.setVariable("int", type);
    }

    public IntType(int value) {
        this.value = value;
        setMember("__class__", type);
    }

    public int getInt(){
        return value;
    }

    public boolean equals(IntType other){
        return this.getInt() == other.getInt();
    }

    public boolean equals(FloatType other){
        return this.getInt() == other.getFloat();
    }

    public boolean equals(Object other){
        if (other.getClass() == IntType.class)
            return this.equals((IntType) other);
        else{
            if (other.getClass() == FloatType.class)
                return this.equals((FloatType) other);
            else
                return false;
        }
    }

    public String toString(){
        return "" + value;
    }

    public int hashCode(){
        return value;
    }

}
