package Executing.ExecutionTokens;

import Executing.ExecutionExceptions.ExecutionException;
import Executing.ExecutionExceptions.ZeroDivisionException;
import Executing.ReturnType;
import Executing.ReturnValue;
import Lexing.Token;

public class BinaryNumericalOperation extends ExecutionToken {
    ExecutionToken left;
    ExecutionToken right;

    public BinaryNumericalOperation(Token token, ExecutionToken left, ExecutionToken right) {
        super(token);
        this.left = left;
        this.right = right;
    }

    public ReturnValue execute() throws ExecutionException {
        ReturnValue result;
        ReturnValue lRes;
        ReturnValue rRes;
        switch (token.getValue()) {
            case "+":
                lRes = left.execute();
                rRes = right.execute();
                result = BinaryNumericalOperation.sum(lRes, rRes);
                break;
            case "-":
                lRes = left.execute();
                rRes = right.execute();
                result = BinaryNumericalOperation.sub(rRes, lRes);
                break;
            case "*":
                lRes = left.execute();
                rRes = right.execute();
                result = BinaryNumericalOperation.mul(lRes, rRes);
                break;
            case "/":
                lRes = left.execute();
                rRes = right.execute();
                result = BinaryNumericalOperation.div(rRes, lRes, this.token);
                break;
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
}
