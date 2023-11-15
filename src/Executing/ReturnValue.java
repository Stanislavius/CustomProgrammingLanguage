package Executing;

import Executing.ExecutionExceptions.ExecutionException;
import Executing.ExecutionTokens.ExecutionToken;

import java.util.LinkedList;

public class ReturnValue<T> {
    T value;
    ReturnType type;

    public ReturnValue(T value, ReturnType type) {
        this.value = value;
        this.type = type;
    }

    public T execute() {
        return value;
    }

    public ReturnType getType() {
        return type;
    }

    public T getValue() {
        return value;
    }

    public String toString() {
        if (this.type == ReturnType.VOID)
            return "void";
        else
            if (this.type == ReturnType.LIST){
                StringBuilder sb = new StringBuilder();
                LinkedList<ExecutionToken> values = (LinkedList<ExecutionToken>) value;
                sb.append("[");
                for(int i = 0; i < values.size(); ++i){
                    try {
                        ReturnValue rt = values.get(i).execute();
                        if (rt.getType() == ReturnType.STRING)
                            sb.append("\"" + rt.toString() + "\"");
                        else
                            sb.append(rt.toString());
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                    if (i != values.size() - 1)
                        sb.append(", ");
                }
                sb.append("]");
                return sb.toString();
            }
            else
                return value.toString();
    }
}
