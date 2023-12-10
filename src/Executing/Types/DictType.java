package Executing.Types;

import Executing.Executor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class DictType extends ObjectType {
    static ClassType type;
    public static void createType()
    {
        type = new ClassType();
        type.setMember("__name__", new StringType("dict"));
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
                DictType v1 = ((DictType)(args.get(0)));
                return new StringType(v1.toString());
            }
        }
        ));


        type.setMember("__len__", new FunctionType("__len__", new SourceFunctionType(){
            public ObjectType execute(LinkedList<ObjectType> args){
                return new IntType(((DictType)(args.get(0))).dict.size());
            }
        }

        ));

        type.setMember("__getitem__", new FunctionType("__getitem__", new SourceFunctionType(){
            public ObjectType execute(LinkedList<ObjectType> args){
                DictType v1 = ((DictType)(args.get(0)));
                return v1.get(args.get(1));
            }
        }
        ));

        type.setMember("contains", new FunctionType("contains", new SourceFunctionType(){
            public ObjectType execute(LinkedList<ObjectType> args){
                DictType v1 = ((DictType)(args.get(0)));
                boolean result = v1.containsKey(args.get(1));
                if (result)
                    return new IntType(1);
                else
                    return new IntType(0);
            }
        }
        ));

        type.setMember("__setitem__", new FunctionType("__setitem__", new SourceFunctionType(){
            public ObjectType execute(LinkedList<ObjectType> args){
                DictType v1 = ((DictType)(args.get(0)));
                return v1.set(args.get(1), args.get(2));
            }
        }
        ));

        type.setMember("__getmember__", new FunctionType("__getmember__", new SourceFunctionType(){
            public ObjectType execute(LinkedList<ObjectType> args){
                DictType v1 = ((DictType)(args.get(0)));
                //v1.getMember(args.get(1).
                return v1.get(args.get(1));
            }
        }
        ));

        type.setMember("__setmember__", new FunctionType("__setitem__", new SourceFunctionType(){
            public ObjectType execute(LinkedList<ObjectType> args){
                DictType v1 = ((DictType)(args.get(0)));
                return v1.set(args.get(1), args.get(2));
            }
        }
        ));

        Executor.setVariable("dict", type);
    }
    HashMap<ObjectType, ObjectType> dict;

    public DictType(LinkedList<ObjectType> keys, LinkedList<ObjectType> values){
        this.dict = new HashMap<ObjectType, ObjectType>();
        for (int i = 0; i < keys.size(); ++i){
            this.dict.put(keys.get(i), values.get(i));
        }
        this.setMember("__class__", type);
    }

    public DictType(){
        this.dict = new HashMap<ObjectType, ObjectType>();
        this.setMember("__class__", type);
    }

    public DictType(HashMap<ObjectType, ObjectType> dict){
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


    public boolean containsKey(ObjectType key) {
        return this.dict.containsKey(key);
    }

    public boolean containsKey(String key) {
        return this.dict.containsKey(new StringType(key));
    }

    public ObjectType get(ObjectType key){
        return dict.get(key);
    }

    public ObjectType set(ObjectType key, ObjectType value){
       this.dict.put(key, value);
       return new VoidType();
    }
}
