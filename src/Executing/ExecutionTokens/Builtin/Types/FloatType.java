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

        type.setMember("__call__", new FunctionType("__call__", new SourceFunction(){
            public ObjectType execute(LinkedList<ObjectType> args) throws ExecutionError {
                ObjectType val1 = args.get(0);
                switch (val1.getType().toString()) {
                    case "int":
                        IntType it = (IntType) val1;
                        return new FloatType((float) it.getInt());
                    case "str":
                        StringType sT = (StringType) val1;
                        return new FloatType(Float.parseFloat(sT.getValue()));
                    case "float":
                        return val1;
                    default:
                        return val1.getMember("__float__").call();
                }
            }
        }
        ));


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
            public ObjectType execute(LinkedList<ObjectType> args) throws ExecutionError {
                float v1 = ((FloatType)(args.get(0))).getFloat();
                ObjectType val2 = args.get(1);
                if (val2.getType().toString().equals("int")) {
                    int v2 = ((IntType) (val2)).getInt();
                    if (v2 == 0) {
                        ErrorType error = new ErrorType("DivisionByZero");
                        throw new ExecutionError(error);
                    }
                    return new FloatType(v1/v2);
                }
                if (val2.getType().toString().equals("float")) {
                    float v2 = ((FloatType) (val2)).getFloat();
                    if (v2 == 0.0) {
                        ErrorType error = new ErrorType("DivisionByZero");
                        throw new ExecutionError(error);
                    }
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

        type.setMember("__eq__", new FunctionType("__eq__", new SourceFunction(){
            public ObjectType execute(LinkedList<ObjectType> args){
                float v1 = ((FloatType)(args.get(0))).getFloat();
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

    public boolean equals(IntType other){
        return this.getFloat() == other.getInt();
    }

    public boolean equals(FloatType other){
        return this.getFloat() == other.getFloat();
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
}
