package Executing;


import java.util.HashMap;

public class Variables {
    HashMap<String, ReturnValue> variables = new HashMap<String, ReturnValue>();


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

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < variables.size(); ++i) {
            sb.append(variables.get(i));
            sb.append(" ");
        }
        return sb.toString();
    }
}
