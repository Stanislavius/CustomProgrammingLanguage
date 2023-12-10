package Executing;


import Executing.Types.ObjectType;

import java.util.HashMap;

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
        for(int i = 0; i < variables.size(); ++i) {
            sb.append(variables.get(i));
            sb.append(" ");
        }
        return sb.toString();
    }
}
