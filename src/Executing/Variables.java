package Executing;


import java.util.HashMap;

public class Variables {
    static HashMap<String, ReturnValue> variables = new HashMap<String, ReturnValue>();


    public void setVariable(String name, ReturnValue value) {
        variables.put(name, value);
    }

    public ReturnValue getVariable(String name) {
        ReturnValue value = null;
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
}
