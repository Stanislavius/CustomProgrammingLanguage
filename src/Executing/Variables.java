package Executing;


import Executing.Types.ObjectType;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Variables {
    HashMap<String, ObjectType> variables = new HashMap<String, ObjectType>();


    public void setVariable(String name, ObjectType value) {
        variables.put(name, value);
    }

    public ObjectType getVariable(String name) {
        ObjectType value = null;
        if (variables.containsKey(name)) {
            value = variables.get(name);
        } else {
            //throw error
        }
        return value;
    }

    public boolean in(String name){
        return variables.containsKey(name);
    }
    public void clear() {
        variables.clear();
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        Set keys = variables.keySet();
        Iterator iter = keys.iterator();
        while (iter.hasNext()){
            sb.append(variables.get(iter.next()).toString());
            sb.append(" ");
        }
        return sb.toString();
    }
}
