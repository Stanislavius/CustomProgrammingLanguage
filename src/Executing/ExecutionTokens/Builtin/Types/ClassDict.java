package Executing.ExecutionTokens.Builtin.Types;

import Executing.Executor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class ClassDict extends ObjectType {
    static ClassType type;
    public static void createType()
    {
        type = new ClassType();
        type.setMember("__name__", new StringType("dict"));
        type.setMember("__append__", new FunctionType("__append__", new SourceFunction(){
            public ObjectType execute(LinkedList<ObjectType> args){
                ListType list = ((ListType)(args.get(0)));
                ObjectType val2 = args.get(1);
                list.values.add(args.get(1));
                return VoidType.voidObject;
            }
        }
        ));

        type.setMember("__str__", new FunctionType("__str__", new SourceFunction(){
            public ObjectType execute(LinkedList<ObjectType> args){
                ClassDict v1 = ((ClassDict)(args.get(0)));
                return new StringType(v1.toString());
            }
        }
        ));


        type.setMember("__len__", new FunctionType("__len__", new SourceFunction(){
            public ObjectType execute(LinkedList<ObjectType> args){
                return new IntType(((ClassDict)(args.get(0))).dict.size());
            }
        }

        ));

        type.setMember("__getitem__", new FunctionType("__getitem__", new SourceFunction(){
            public ObjectType execute(LinkedList<ObjectType> args){
                ClassDict v1 = ((ClassDict)(args.get(0)));
                return v1.get(args.get(1));
            }
        }
        ));

        type.setMember("__setitem__", new FunctionType("__setitem__", new SourceFunction(){
            public ObjectType execute(LinkedList<ObjectType> args){
                ClassDict v1 = ((ClassDict)(args.get(0)));
                return v1.set(args.get(1), args.get(2));
            }
        }
        ));

        Executor.setVariable("dict", type);
    }
    HashMap<ObjectType, ObjectType> dict;

    public ClassDict(LinkedList<ObjectType> keys, LinkedList<ObjectType> values){
        this.dict = new HashMap<ObjectType, ObjectType>();
        for (int i = 0; i < keys.size(); ++i){
            this.dict.put(keys.get(i), values.get(i));
        }
        this.setMember("__class__", type);
    }

    public ClassDict(){
        this.dict = new HashMap<ObjectType, ObjectType>();
        this.setMember("__class__", type);
    }

    public ClassDict(HashMap<ObjectType, ObjectType> dict){
        this.dict = dict;
        this.setMember("__class__", type);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        Set keys  = dict.keySet();
        Iterator iter = keys.iterator();
        while(iter.hasNext()){
            ObjectType key = (ObjectType)iter.next();
            ObjectType value = dict.get(key);
            sb.append(key.toString());
            sb.append(":");
            sb.append(value.toString());
            if (iter.hasNext())
                sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }


    public boolean containsKey(String name) {
        return this.dict.containsKey(name);
    }

    public ObjectType get(ObjectType key){
        return dict.get(key);
    }

    public ObjectType set(ObjectType key, ObjectType value){
       this.dict.put(key, value);
       return new VoidType();
    }
}