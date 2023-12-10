package Executing.Types;

import Executing.Executor;

import java.util.LinkedList;

public class ListType extends ObjectType {
    static ClassType type;
    public static void createType()
    {
        type = new ClassType();
        type.setMember("__name__", new StringType("list"));
        type.setMember("__append__", new FunctionType("__append__", new SourceFunctionType(){
            public ObjectType execute(LinkedList<ObjectType> args){
                ListType list = ((ListType)(args.get(0)));
                ObjectType val2 = args.get(1);
                list.values.add(args.get(1));
                return VoidType.voidObject;
            }
        }
        ));

        type.setMember("__str__", new FunctionType("__str__", new SourceFunctionType(){
            public ObjectType execute(LinkedList<ObjectType> args){
                ListType v1 = ((ListType)(args.get(0)));
                return new StringType(v1.toString());
            }
        }
        ));

        type.setMember("__call__", new FunctionType("__str__", new SourceFunctionType(){
            public ObjectType execute(LinkedList<ObjectType> args){
                return new ListType(args);
            }
        }
        ));

        type.setMember("__getitem__", new FunctionType("__getitem__", new SourceFunctionType(){
            public ObjectType execute(LinkedList<ObjectType> args){
                ListType v1 = ((ListType)(args.get(0)));
                return v1.get(args.get(1));
            }
        }
        ));

        type.setMember("__setitem__", new FunctionType("__setitem__", new SourceFunctionType(){
            public ObjectType execute(LinkedList<ObjectType> args){
                ListType v1 = ((ListType)(args.get(0)));
                return v1.set(args.get(1), args.get(2));
            }
        }
        ));

        type.setMember("__add__", new FunctionType("__add__", new SourceFunctionType(){
            public ObjectType execute(LinkedList<ObjectType> args){
                LinkedList<ObjectType> v1 = ((ListType)(args.get(0))).getValues();
                LinkedList<ObjectType> v2 = ((ListType)(args.get(1))).getValues();
                LinkedList<ObjectType> result = new LinkedList<ObjectType>();
                result.addAll(v1);
                result.addAll(v2);
                return new ListType(result);
            }
        }
        ));

        type.setMember("__len__", new FunctionType("__len__", new SourceFunctionType(){
            public ObjectType execute(LinkedList<ObjectType> args){
                LinkedList<ObjectType> v1 = ((ListType)(args.get(0))).getValues();
                return new IntType(v1.size());
            }
        }

        ));

        type.setMember("__eq__", new FunctionType("__eq__", new SourceFunctionType(){
            public ObjectType execute(LinkedList<ObjectType> args){
                LinkedList<ObjectType> v1 = ((ListType)(args.get(0))).getValues();
                LinkedList<ObjectType> v2 = ((ListType)(args.get(1))).getValues();
                boolean result = v1.equals(v2);
                if (result)
                    return new IntType(1);
                else
                    return new IntType(0);
            }
        }
        ));

        Executor.setVariable("list", type);
    }
    LinkedList<ObjectType> values;

    public ListType(LinkedList<ObjectType> values){
        this.values = values;
        this.setMember("__class__", type);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(int i = 0; i < values.size(); ++i){
                ObjectType rt = values.get(i);
                if (rt.getType().toString().equals("str"))
                    sb.append("\"" + rt.toString() + "\"");
                else
                    sb.append(rt.toString());
            if (i != values.size() - 1)
                sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    public LinkedList<ObjectType> getValues(){
        return values;
    }

    public ObjectType get(int inx){
        return this.values.get(inx);
    }

    public ObjectType get(IntType inx){
        return this.values.get(inx.getInt());
    }

    public ObjectType get(ObjectType inx){
        IntType obj = (IntType)inx;
        return this.values.get(obj.getInt());
    }


    public ObjectType set(int inx, ObjectType value){
        this.values.set(inx, value);
        return new VoidType();
    }

    public ObjectType set(IntType inx, ObjectType value){
        this.values.set(inx.getInt(), value);
        return new VoidType();
    }

    public ObjectType set(ObjectType inx, ObjectType value){
        IntType obj = (IntType)inx;
        this.values.set(obj.getInt(), value);
        return new VoidType();
    }

}
