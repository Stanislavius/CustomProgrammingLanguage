package Executing.ExecutionTokens;

import Executing.ExecutionExceptions.ExecutionException;
import Executing.ExecutionExceptions.ZeroDivisionException;
import Executing.ReturnType;
import Executing.ReturnValue;
import Lexing.Token;

public class BinaryOperation extends ExecutionToken {
    ExecutionToken left;
    ExecutionToken right;

    public BinaryOperation(Token token, ExecutionToken left, ExecutionToken right) {
        super(token);
        this.left = left;
        this.right = right;
    }

    public ReturnValue execute() throws ExecutionException {
        ReturnValue result;
        ReturnValue lRes;
        ReturnValue rRes;
        lRes = left.execute();
        rRes = right.execute();
        switch (token.getValue()) {
            case "+":
                result = BinaryOperation.sum(lRes, rRes);
                break;
            case "-":
                result = BinaryOperation.sub(rRes, lRes);
                break;
            case "*":
                result = BinaryOperation.mul(lRes, rRes);
                break;
            case "/":
                result = BinaryOperation.div(rRes, lRes, this.token);
                break;
            case "==":
                return equalsOperation(lRes, rRes);
            case "<":
                return lesserOperation(lRes, rRes);
            case ">":
                return greaterOperation(lRes, rRes);
            default:
                result = new ReturnValue<String>(null, ReturnType.ERROR);
        }
        return result;
    }

    static ReturnValue mul(ReturnValue lRes, ReturnValue rRes) throws ExecutionException {
        if (lRes.getType() == ReturnType.INT && rRes.getType() == ReturnType.INT) {
            int lInt = (int) lRes.getValue();
            int rInt = (int) rRes.getValue();
            return new ReturnValue<Integer>(rInt * lInt, ReturnType.INT);
        }
        if (lRes.getType() == ReturnType.FLOAT && rRes.getType() == ReturnType.INT) {
            float lInt = (float) lRes.getValue();
            int rInt = (int) rRes.getValue();
            return new ReturnValue<Float>(rInt * lInt, ReturnType.FLOAT);
        }
        if (lRes.getType() == ReturnType.INT && rRes.getType() == ReturnType.FLOAT) {
            int lInt = (int) lRes.getValue();
            float rInt = (float) rRes.getValue();
            return new ReturnValue<Float>(rInt * lInt, ReturnType.FLOAT);
        }
        if (lRes.getType() == ReturnType.FLOAT && rRes.getType() == ReturnType.FLOAT) {
            float lInt = (float) lRes.getValue();
            float rInt = (float) rRes.getValue();
            return new ReturnValue<Float>(rInt * lInt, ReturnType.FLOAT);
        }
        return new ReturnValue(null, ReturnType.ERROR);
    }

    static ReturnValue div(ReturnValue lRes, ReturnValue rRes, Token t) throws ExecutionException {
        if (lRes.getType() == ReturnType.INT && rRes.getType() == ReturnType.INT) {
            int lInt = (int) lRes.getValue();
            int rInt = (int) rRes.getValue();
            if (rInt == 0) {
                throw new ZeroDivisionException(t);
            }
            return new ReturnValue<Integer>(rInt / lInt, ReturnType.INT);
        }
        if (lRes.getType() == ReturnType.FLOAT && rRes.getType() == ReturnType.INT) {
            float lInt = (float) lRes.getValue();
            int rInt = (int) rRes.getValue();
            if (rInt == 0) {
                throw new ZeroDivisionException(t);
            }
            return new ReturnValue<Float>(rInt / lInt, ReturnType.FLOAT);
        }
        if (lRes.getType() == ReturnType.INT && rRes.getType() == ReturnType.FLOAT) {
            int lInt = (int) lRes.getValue();
            float rInt = (float) rRes.getValue();
            if (rInt == 0.0) {
                throw new ZeroDivisionException(t);
            }
            return new ReturnValue<Float>(rInt / lInt, ReturnType.FLOAT);
        }
        if (lRes.getType() == ReturnType.FLOAT && rRes.getType() == ReturnType.FLOAT) {
            float lInt = (float) lRes.getValue();
            float rInt = (float) rRes.getValue();
            if (rInt == 0.0) {
                throw new ZeroDivisionException(t);
            }
            return new ReturnValue<Float>(rInt / lInt, ReturnType.FLOAT);
        }
        return new ReturnValue(null, ReturnType.ERROR);
    }

    static ReturnValue sum(ReturnValue lRes, ReturnValue rRes) {
        if (lRes.getType() == ReturnType.INT && rRes.getType() == ReturnType.INT) {
            int lInt = (int) lRes.getValue();
            int rInt = (int) rRes.getValue();
            return new ReturnValue<Integer>(rInt + lInt, ReturnType.INT);
        }
        if (lRes.getType() == ReturnType.FLOAT && rRes.getType() == ReturnType.INT) {
            float lInt = (float) lRes.getValue();
            int rInt = (int) rRes.getValue();
            return new ReturnValue<Float>(rInt + lInt, ReturnType.FLOAT);
        }
        if (lRes.getType() == ReturnType.INT && rRes.getType() == ReturnType.FLOAT) {
            int lInt = (int) lRes.getValue();
            float rInt = (float) rRes.getValue();
            return new ReturnValue<Float>(rInt + lInt, ReturnType.FLOAT);
        }
        if (lRes.getType() == ReturnType.FLOAT && rRes.getType() == ReturnType.FLOAT) {
            float lInt = (float) lRes.getValue();
            float rInt = (float) rRes.getValue();
            return new ReturnValue<Float>(rInt + lInt, ReturnType.FLOAT);
        }
        return new ReturnValue(null, ReturnType.ERROR);
    }

    static ReturnValue sub(ReturnValue lRes, ReturnValue rRes) {
        if (lRes.getType() == ReturnType.INT && rRes.getType() == ReturnType.INT) {
            int lInt = (int) lRes.getValue();
            int rInt = (int) rRes.getValue();
            return new ReturnValue<Integer>(rInt - lInt, ReturnType.INT);
        }
        if (lRes.getType() == ReturnType.FLOAT && rRes.getType() == ReturnType.INT) {
            float lInt = (float) lRes.getValue();
            int rInt = (int) rRes.getValue();
            return new ReturnValue<Float>(rInt - lInt, ReturnType.FLOAT);
        }
        if (lRes.getType() == ReturnType.INT && rRes.getType() == ReturnType.FLOAT) {
            int lInt = (int) lRes.getValue();
            float rInt = (float) rRes.getValue();
            return new ReturnValue<Float>(rInt - lInt, ReturnType.FLOAT);
        }
        if (lRes.getType() == ReturnType.FLOAT && rRes.getType() == ReturnType.FLOAT) {
            float lInt = (float) lRes.getValue();
            float rInt = (float) rRes.getValue();
            return new ReturnValue<Float>(rInt - lInt, ReturnType.FLOAT);
        }
        return new ReturnValue(null, ReturnType.ERROR);
    }

    public static ReturnValue equalsOperation (ReturnValue left, ReturnValue right) {
        if (left.getType() == ReturnType.INT && right.getType() == ReturnType.INT) {
            int val1 = (int) left.getValue();
            int val2 = (int) right.getValue();
            int res = 0;
            if (val1 == val2)
                res = 1;
            return new ReturnValue(res, ReturnType.INT);
        }
        return new ReturnValue(null, ReturnType.VOID);
    }

    public static ReturnValue greaterOperation (ReturnValue left, ReturnValue right) {
        if (left.getType() == ReturnType.INT && right.getType() == ReturnType.INT) {
            int val1 = (int) left.getValue();
            int val2 = (int) right.getValue();
            int res = 0;
            if (val1 > val2)
                res = 1;
            if(val1 < val2)
                res = 0;
            return new ReturnValue(res, ReturnType.INT);
        }
        return new ReturnValue(null, ReturnType.VOID);
    }

    public static ReturnValue lesserOperation (ReturnValue left, ReturnValue right) {
        if (left.getType() == ReturnType.INT && right.getType() == ReturnType.INT) {
            int val1 = (int) left.getValue();
            int val2 = (int) right.getValue();
            int res = 0;
            if (val1 > val2)
                res = 0;
            if(val1 < val2)
                res = 1;
            return new ReturnValue(res, ReturnType.INT);
        }
        return new ReturnValue(null, ReturnType.VOID);
    }

}
