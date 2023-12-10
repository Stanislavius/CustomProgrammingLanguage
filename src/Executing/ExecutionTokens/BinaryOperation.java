package Executing.ExecutionTokens;

import Executing.Types.ErrorType;
import Executing.Types.ExecutionException;
import Executing.Types.ObjectType;
import Lexing.Token;

import java.util.Arrays;
import java.util.LinkedList;

public class BinaryOperation extends ExecutionToken {
    ExecutionToken left;
    ExecutionToken right;

    public BinaryOperation(Token token, ExecutionToken left, ExecutionToken right) {
        super(token);
        this.left = left;
        this.right = right;
    }

    public ObjectType execute() throws ExecutionException {
        ObjectType lRes;
        ObjectType rRes;
        LinkedList<ObjectType> args;
        if (token.getValue().equals(".")){
            lRes = left.execute();
            VariableExecutionToken veb = (VariableExecutionToken) right;
            return lRes.getMember(veb.token.getValue());
        }
        else{
            lRes = left.execute();
            if (right.getClass() == FunctionCallToken.class)
            {
                FunctionCallToken fct = (FunctionCallToken) right;
                args = fct.executeArgs();
                return lRes.call(args);
            }
            else {
                rRes = right.execute();
                args = new LinkedList<ObjectType>(Arrays.asList(lRes, rRes));
                try {
                    switch (token.getValue()) {
                        case "+":
                            return lRes.getMember("__class__").getMember("__add__").call(args);
                        case "-":
                            return lRes.getMember("__class__").getMember("__sub__").call(args);
                        case "*":
                            return lRes.getMember("__class__").getMember("__mul__").call(args);
                        case "/":
                            return lRes.getMember("__class__").getMember("__div__").call(args);
                        case "==":
                            return lRes.getMember("__class__").getMember("__eq__").call(args);
                        case "<":
                            return lRes.getMember("__class__").getMember("__lt__").call(args);
                        case ">":
                            return lRes.getMember("__class__").getMember("__gt__").call(args);
                        default:
                            return new ErrorType();
                    }
                }
                catch (ExecutionException e){
                    ErrorType error = e.getError();
                    error.setLine(this.getToken().getLine());
                    error.setPosition(this.getToken().getPos());
                    throw e;
                }
            }
        }
    }

}
