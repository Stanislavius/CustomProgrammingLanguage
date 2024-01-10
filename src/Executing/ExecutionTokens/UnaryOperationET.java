package Executing.ExecutionTokens;

import Executing.Types.ErrorType;
import Executing.Types.ExecutionException;
import Executing.Types.ObjectType;
import Lexing.Token;

public class UnaryOperationET extends ExecutionToken {
    ExecutionToken right;

    public UnaryOperationET(Token token, ExecutionToken right) {
        super(token);
        this.right = right;
    }

    public ObjectType execute() throws ExecutionException {
        ObjectType arg = right.execute();
        try {
            switch (token.getValue()) {
                case "+":
                    return arg.getMember("__class__").getMember("__pos__").call(arg);
                case "-":
                    return arg.getMember("__class__").getMember("__neg__").call(arg);
            }
        }

        catch (ExecutionException e){
            ErrorType et = e.getError();
            et.setLine(token.getLineNum());
            et.setPosition(token.getPos());
            throw e;
        }
        return new ErrorType();
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(token.getValue());
        sb.append("(");
        sb.append(right.toString());
        sb.append(")");
        return sb.toString();
    }
}
