package Executing.ExecutionTokens;

import Executing.Types.ErrorType;
import Executing.Types.ExecutionException;
import Executing.Types.IntType;
import Executing.Types.ObjectType;
import Lexing.Token;

import java.util.Arrays;
import java.util.LinkedList;

public class BinaryOperationET extends ExecutionToken {
    ExecutionToken left;
    ExecutionToken right;

    public BinaryOperationET(Token token, ExecutionToken left, ExecutionToken right) {
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
            VariableET veb = (VariableET) right;
            return lRes.getMemberNoBound(veb.token.getValue());
        }
        else{
            lRes = left.execute();
            if (right.getClass() == FunctionCallET.class)
            {
                FunctionCallET fct = (FunctionCallET) right;
                args = fct.executeArgs();
                return lRes.call(args);
            }
            else {
                rRes = right.execute();
                args = new LinkedList<ObjectType>(Arrays.asList(lRes, rRes));
                try {
                    switch (token.getValue()) {
                        case "+":
                            return lRes.getMemberNoBound("__class__").getMemberNoBound("__add__").call(args);
                        case "-":
                            return lRes.getMemberNoBound("__class__").getMemberNoBound("__sub__").call(args);
                        case "*":
                            return lRes.getMemberNoBound("__class__").getMemberNoBound("__mul__").call(args);
                        case "/":
                            return lRes.getMemberNoBound("__class__").getMemberNoBound("__div__").call(args);
                        case "==":
                            if (lRes.getMemberNoBound("__class__").contains("__eq__")) {
                                ObjectType member = lRes.getMemberNoBound("__class__").getMemberNoBound("__eq__");
                                return lRes.getMemberNoBound("__class__").getMemberNoBound("__eq__").call(args);
                            }
                            else {
                                boolean identity = (lRes == rRes);
                                if (identity)
                                    return new IntType(1);
                                else
                                    return new IntType(0);
                            }
                        case "<":
                            return lRes.getMemberNoBound("__class__").getMemberNoBound("__lt__").call(args);
                        case ">":
                            return lRes.getMemberNoBound("__class__").getMemberNoBound("__gt__").call(args);
                        default:
                            return new ErrorType();
                    }
                }
                catch (ExecutionException e){
                    ErrorType error = e.getError();
                    error.setLine(this.getToken().getLineNum());
                    error.setPosition(this.getToken().getPos());
                    throw e;
                }
            }
        }
    }

}
