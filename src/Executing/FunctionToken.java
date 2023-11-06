package Executing;

import Lexing.Token;

import java.util.LinkedList;

public class FunctionToken extends ExecutionToken {
    LinkedList<ExecutionToken> args;

    public FunctionToken(Token token, LinkedList<ExecutionToken> args) {
        super(token);
        this.args = args;
    }

    public ReturnType execute() throws ExecutionException {
        switch (token.getValue()) {
            case "abs":
                if (args.size() != 1)
                    throw new WrongNumberOfArgumentsException(token, 1, args.size());
                return FunctionToken.abs(args.get(0).execute());
            case "neg":
                if (args.size() != 1)
                    throw new WrongNumberOfArgumentsException(token, 1, args.size());
                return FunctionToken.neg(args.get(0).execute());
            case "int":
                if (args.size() != 1)
                    throw new WrongNumberOfArgumentsException(token, 1, args.size());
                return FunctionToken.convertToInt(args.get(0).execute());
            case "float":
                if (args.size() != 1)
                    throw new WrongNumberOfArgumentsException(token, 1, args.size());
                return FunctionToken.convertToFloat(args.get(0).execute());
            case "print":
                if (args.size() != 1)
                    throw new WrongNumberOfArgumentsException(token, 1, args.size());
                return new ReturnType<>(args.get(0).execute(), ReturnTypes.PRINT);
            case "argmax":
                if (args.size() != 2)
                    throw new WrongNumberOfArgumentsException(token, 2, args.size());
                return FunctionToken.argMax(args.get(0).execute(), args.get(1).execute());
            default:
                throw new NoSuchFunctionException(token);
        }

    }

    static ReturnType abs(ReturnType arg) {
        if (arg.getType() == ReturnTypes.INT) {
            int argInt = (int) arg.getValue();
            return new ReturnType(Math.abs(argInt), ReturnTypes.INT);
        }
        if (arg.getType() == ReturnTypes.FLOAT) {
            float floatInt = (float) arg.getValue();
            return new ReturnType(Math.abs(floatInt), ReturnTypes.FLOAT);
        }
        return new ReturnType(null, ReturnTypes.ERROR);
    }

    static ReturnType convertToInt(ReturnType arg) {
        if (arg.getType() == ReturnTypes.INT) {
            return arg;
        }
        if (arg.getType() == ReturnTypes.FLOAT) {
            float val = (float) arg.getValue();
            int floatInt = (int) val;
            return new ReturnType(floatInt, ReturnTypes.INT);
        }
        return new ReturnType(null, ReturnTypes.ERROR);
    }

    static ReturnType convertToFloat(ReturnType arg) {
        if (arg.getType() == ReturnTypes.INT) {
            int val = (int) arg.getValue();
            float floatInt = (float) val;
            return new ReturnType(floatInt, ReturnTypes.FLOAT);
        }
        if (arg.getType() == ReturnTypes.FLOAT) {
            return arg;
        }
        return new ReturnType(null, ReturnTypes.ERROR);
    }

    static ReturnType neg(ReturnType arg) {
        if (arg.getType() == ReturnTypes.INT) {
            int argInt = (int) arg.getValue();
            return new ReturnType(-Math.abs(argInt), ReturnTypes.INT);
        }
        if (arg.getType() == ReturnTypes.FLOAT) {
            float floatInt = (int) arg.getValue();
            return new ReturnType(-Math.abs(floatInt), ReturnTypes.FLOAT);
        }
        return new ReturnType(null, ReturnTypes.ERROR);
    }

    static ReturnType argMax(ReturnType arg1, ReturnType arg2) {
        float f1, f2;
        if (arg1.getType() == ReturnTypes.INT) {
            f1 = (float) ((int) arg1.getValue());
        } else {
            f1 = (float) arg1.getValue();
        }
        if (arg2.getType() == ReturnTypes.INT) {
            f2 = (float) ((int) arg2.getValue());
        } else {
            f2 = (float) arg2.getValue();
        }
        int res = 0;
        if (f1 > f2) {
            res = 1;
        }
        if (f1 < f2) {
            res = -1;
        }
        if (f1 == f2) {
            res = 0;
        }
        return new ReturnType(res, ReturnTypes.INT);
    }
}
