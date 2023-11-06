package Executing;

import Lexing.Token;

public class BinaryNumericalOperation extends ExecutionToken {
    ExecutionToken left;
    ExecutionToken right;

    public BinaryNumericalOperation(Token token, ExecutionToken left, ExecutionToken right) {
        super(token);
        this.left = left;
        this.right = right;
    }

    public ReturnType execute() throws ExecutionException {
        ReturnType result;
        ReturnType lRes;
        ReturnType rRes;
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
                result = new ReturnType<String>(null, ReturnTypes.ERROR);
        }
        return result;
    }

    static ReturnType mul(ReturnType lRes, ReturnType rRes) throws ExecutionException {
        if (lRes.getType() == ReturnTypes.INT && rRes.getType() == ReturnTypes.INT) {
            int lInt = (int) lRes.getValue();
            int rInt = (int) rRes.getValue();
            return new ReturnType<Integer>(rInt * lInt, ReturnTypes.INT);
        }
        if (lRes.getType() == ReturnTypes.FLOAT && rRes.getType() == ReturnTypes.INT) {
            float lInt = (float) lRes.getValue();
            int rInt = (int) rRes.getValue();
            return new ReturnType<Float>(rInt * lInt, ReturnTypes.FLOAT);
        }
        if (lRes.getType() == ReturnTypes.INT && rRes.getType() == ReturnTypes.FLOAT) {
            int lInt = (int) lRes.getValue();
            float rInt = (float) rRes.getValue();
            return new ReturnType<Float>(rInt * lInt, ReturnTypes.FLOAT);
        }
        if (lRes.getType() == ReturnTypes.FLOAT && rRes.getType() == ReturnTypes.FLOAT) {
            float lInt = (float) lRes.getValue();
            float rInt = (float) rRes.getValue();
            return new ReturnType<Float>(rInt * lInt, ReturnTypes.FLOAT);
        }
        return new ReturnType(null, ReturnTypes.ERROR);
    }

    static ReturnType div(ReturnType lRes, ReturnType rRes, Token t) throws ExecutionException {
        if (lRes.getType() == ReturnTypes.INT && rRes.getType() == ReturnTypes.INT) {
            int lInt = (int) lRes.getValue();
            int rInt = (int) rRes.getValue();
            if (rInt == 0) {
                throw new ZeroDivisionException(t);
            }
            return new ReturnType<Integer>(rInt / lInt, ReturnTypes.INT);
        }
        if (lRes.getType() == ReturnTypes.FLOAT && rRes.getType() == ReturnTypes.INT) {
            float lInt = (float) lRes.getValue();
            int rInt = (int) rRes.getValue();
            if (rInt == 0) {
                throw new ZeroDivisionException(t);
            }
            return new ReturnType<Float>(rInt / lInt, ReturnTypes.FLOAT);
        }
        if (lRes.getType() == ReturnTypes.INT && rRes.getType() == ReturnTypes.FLOAT) {
            int lInt = (int) lRes.getValue();
            float rInt = (float) rRes.getValue();
            if (rInt == 0.0) {
                throw new ZeroDivisionException(t);
            }
            return new ReturnType<Float>(rInt / lInt, ReturnTypes.FLOAT);
        }
        if (lRes.getType() == ReturnTypes.FLOAT && rRes.getType() == ReturnTypes.FLOAT) {
            float lInt = (float) lRes.getValue();
            float rInt = (float) rRes.getValue();
            if (rInt == 0.0) {
                throw new ZeroDivisionException(t);
            }
            return new ReturnType<Float>(rInt / lInt, ReturnTypes.FLOAT);
        }
        return new ReturnType(null, ReturnTypes.ERROR);
    }

    static ReturnType sum(ReturnType lRes, ReturnType rRes) {
        if (lRes.getType() == ReturnTypes.INT && rRes.getType() == ReturnTypes.INT) {
            int lInt = (int) lRes.getValue();
            int rInt = (int) rRes.getValue();
            return new ReturnType<Integer>(rInt + lInt, ReturnTypes.INT);
        }
        if (lRes.getType() == ReturnTypes.FLOAT && rRes.getType() == ReturnTypes.INT) {
            float lInt = (float) lRes.getValue();
            int rInt = (int) rRes.getValue();
            return new ReturnType<Float>(rInt + lInt, ReturnTypes.FLOAT);
        }
        if (lRes.getType() == ReturnTypes.INT && rRes.getType() == ReturnTypes.FLOAT) {
            int lInt = (int) lRes.getValue();
            float rInt = (float) rRes.getValue();
            return new ReturnType<Float>(rInt + lInt, ReturnTypes.FLOAT);
        }
        if (lRes.getType() == ReturnTypes.FLOAT && rRes.getType() == ReturnTypes.FLOAT) {
            float lInt = (float) lRes.getValue();
            float rInt = (float) rRes.getValue();
            return new ReturnType<Float>(rInt + lInt, ReturnTypes.FLOAT);
        }
        return new ReturnType(null, ReturnTypes.ERROR);
    }

    static ReturnType sub(ReturnType lRes, ReturnType rRes) {
        if (lRes.getType() == ReturnTypes.INT && rRes.getType() == ReturnTypes.INT) {
            int lInt = (int) lRes.getValue();
            int rInt = (int) rRes.getValue();
            return new ReturnType<Integer>(rInt - lInt, ReturnTypes.INT);
        }
        if (lRes.getType() == ReturnTypes.FLOAT && rRes.getType() == ReturnTypes.INT) {
            float lInt = (float) lRes.getValue();
            int rInt = (int) rRes.getValue();
            return new ReturnType<Float>(rInt - lInt, ReturnTypes.FLOAT);
        }
        if (lRes.getType() == ReturnTypes.INT && rRes.getType() == ReturnTypes.FLOAT) {
            int lInt = (int) lRes.getValue();
            float rInt = (float) rRes.getValue();
            return new ReturnType<Float>(rInt - lInt, ReturnTypes.FLOAT);
        }
        if (lRes.getType() == ReturnTypes.FLOAT && rRes.getType() == ReturnTypes.FLOAT) {
            float lInt = (float) lRes.getValue();
            float rInt = (float) rRes.getValue();
            return new ReturnType<Float>(rInt - lInt, ReturnTypes.FLOAT);
        }
        return new ReturnType(null, ReturnTypes.ERROR);
    }
}
