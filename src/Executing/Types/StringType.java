package Executing.Types;

import Executing.Executor;

import java.util.LinkedList;

public class StringType extends ObjectType {
    static ClassType type;
    public static void createType()
    {
        type = new ClassType();
        type.setMember("__name__", new StringType("str"));

        type.setMember("__str__", new FunctionType("__str__", new SourceFunctionType(){
            public ObjectType execute(LinkedList<ObjectType> args){
                StringType s1 = ((StringType)(args.get(0)));
                return s1;
            }
        }
        ));

        type.setMember("__call__", new FunctionType("__call__", new SourceFunctionType(){
            public ObjectType execute(LinkedList<ObjectType> args) throws ExecutionException {
                ObjectType val1 = args.get(0);
                return val1.getMemberNoBound("__str__").call(val1);
            }
        }
        ));

        type.setMember("__add__", new FunctionType("__add__", new SourceFunctionType(){
            public ObjectType execute(LinkedList<ObjectType> args) throws ExecutionException {
                String v1 = ((StringType)(args.get(0))).toString();
                ObjectType val2 = args.get(1);
                return new StringType(v1 + ((StringType)val2.getMember(
                        "__class__").getMember(
                                "__str__").call(val2)).getValue());
            }
        }

        ));

        type.setMember("__getitem__", new FunctionType("__getitem__", new SourceFunctionType(){
            public ObjectType execute(LinkedList<ObjectType> args){
                StringType v1 = ((StringType)(args.get(0)));
                return v1.get(args.get(1));
            }
        }
        ));

        type.setMember("__mul__", new FunctionType("__mul__", new SourceFunctionType(){
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

        type.setMember("__len__", new FunctionType("__len__", new SourceFunctionType(){
            public ObjectType execute(LinkedList<ObjectType> args){
                String v1 = ((StringType)(args.get(0))).value;
                return new IntType(v1.length());
            }
        }

        ));

        type.setMember("__eq__", new FunctionType("__eq__", new SourceFunctionType(){
            public ObjectType execute(LinkedList<ObjectType> args) throws ExecutionException {
                String v1 = ((StringType)(args.get(0))).getValue();
                ObjectType val2 = args.get(1);
                if (val2.getType().toString().equals("str")) {
                    String v2 = ((StringType) (args.get(1))).getValue();
                    boolean result = v1.equals(v2);
                    if (result)
                        return new IntType(1);
                    else
                        return new IntType(0);
                }

                if (val2.getType().toString().equals("list")) {
                    return new IntType(0);
                }
                if (val2.getType().toString().equals("dict")) {
                    return new IntType(0);
                }
                if (val2.getType().toString().equals("type")) {
                    return new IntType(0);
                }
                if (val2.getType().toString().equals("int")) {
                    return new IntType(0);
                }
                if (val2.getType().toString().equals("float")) {
                    return new IntType(0);
                }
                LinkedList<ObjectType> revArgs = new LinkedList<ObjectType>();
                if (val2.getMember("__class__").contains("__eq__")) {
                    revArgs.add(val2);
                    revArgs.add(args.get(0));
                    return val2.getMember("__eq__").call(revArgs);
                }
                else{
                    return new IntType(0);
                }
            }
        }
        ));

        Executor.setVariable("str", type);
    }

    public ObjectType get(int inx){
        return new StringType(this.value.substring(inx, inx));
    }

    public ObjectType get(IntType inx){
        return new StringType(this.value.substring(inx.getInt(), inx.getInt()+1));
    }

    public ObjectType get(ObjectType inx){
        IntType obj = (IntType)inx;
        return new StringType(this.value.substring(obj.getInt(), obj.getInt()+1));
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
