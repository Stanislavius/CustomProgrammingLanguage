package Executing.ExecutionTokens;

import Executing.Types.ErrorType;
import Executing.Types.ExecutionException;
import Executing.Types.IntType;
import Executing.Types.ObjectType;
import Lexing.Token;
import Executing.Executor;

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
            ObjectType result = lRes.getMember(veb.token.getValue());
            Executor.logger.info("Get member" + left.toString() + "." + right.toString() + " result is " + result.toString());
            return result;
        }
        else{
            lRes = left.execute();
            if (right.getClass() == FunctionCallET.class)
            {
                FunctionCallET fct = (FunctionCallET) right;
                args = fct.executeArgs();
                Executor.logger.info("Call function " + left.toString() + " with args: (" +
                        args.toString().substring(1, args.toString().length()-1) + ")");
                try {
                    ObjectType result = lRes.call(args);
                    Executor.logger.info("Result of calling function " + left.toString() + " with args: (" +
                            args.toString().substring(1, args.toString().length() - 1) + ") is " + result.toString());
                    return result;
                }
                catch (ExecutionException e){
                    ErrorType et = e.getError();
                    if(et.getMessage().equals("WrongNumberOfArguments")) {
                        et.setLine(token.getLineNum());
                        et.setPosition(token.getPos());
                    }
                    throw e;
                }
            }
            else {
                rRes = right.execute();
                Executor.logger.info("Binary operation " + token.getValue() + " for " + lRes.toString() + " and " + rRes.toString());
                args = new LinkedList<ObjectType>(Arrays.asList(lRes, rRes));
                ObjectType result;
                try {
                    switch (token.getValue()) {
                        case "+":
                            result = lRes.getMemberNoBound("__class__").getMemberNoBound("__add__").call(args);
                            break;
                        case "-":
                            result = lRes.getMemberNoBound("__class__").getMemberNoBound("__sub__").call(args);
                            break;
                        case "*":
                            result = lRes.getMemberNoBound("__class__").getMemberNoBound("__mul__").call(args);
                            break;
                        case "/":
                            result = lRes.getMemberNoBound("__class__").getMemberNoBound("__div__").call(args);
                            break;
                        case "==":
                            if (lRes.getMemberNoBound("__class__").contains("__eq__")) {
                                ObjectType member = lRes.getMemberNoBound("__class__").getMemberNoBound("__eq__");
                                result =  lRes.getMemberNoBound("__class__").getMemberNoBound("__eq__").call(args);
                            }
                            else {
                                boolean identity = (lRes == rRes);
                                if (identity)
                                    result = new IntType(1);
                                else
                                    result = new IntType(0);
                            }
                            break;
                        case "<":
                            result = lRes.getMemberNoBound("__class__").getMemberNoBound("__lt__").call(args);
                            break;
                        case ">":
                            result = lRes.getMemberNoBound("__class__").getMemberNoBound("__gt__").call(args);
                            break;
                        default:
                            result = new ErrorType();
                    }
                    Executor.logger.info("Binary operation " + token.getValue() + " for " + lRes.toString() + " and " + rRes.toString() + ", result is " + result.toString());
                    return result;
                }
                catch (ExecutionException e){
                    ErrorType error = e.getError();
                    if (!error.hasLineNum()) {
                        error.setLine(this.getToken().getLineNum());
                        error.setPosition(this.getToken().getPos());
                    }
                    throw e;
                }
            }
        }
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        if (right.getClass() == FunctionCallET.class){
            sb.append(left.toString());
            sb.append(token.getValue());
            sb.append(right.toString());
            sb.append(")");
        }
        else {
            sb.append("(");
            sb.append(left.toString());
            sb.append(token.getValue());
            sb.append(right.toString());
            sb.append(")");
        }
        return sb.toString();
    }

}
