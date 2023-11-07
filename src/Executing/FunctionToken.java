package Executing;

import Lexing.Token;

import java.util.LinkedList;

public class FunctionToken extends ExecutionToken {
    LinkedList<ExecutionToken> args;

    public FunctionToken(Token token, LinkedList<ExecutionToken> args) {
        super(token);
        this.args = args;
    }

    public ReturnValue execute() throws ExecutionException {
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
                System.out.println(args.get(0).execute());
                return new ReturnValue<>(args.get(0).execute(), ReturnType.PRINT);
            case "argmax":
                if (args.size() != 2)
                    throw new WrongNumberOfArgumentsException(token, 2, args.size());
                return FunctionToken.argMax(args.get(0).execute(), args.get(1).execute());
            default:
                throw new NoSuchFunctionException(token);
        }

    }

    static ReturnValue abs(ReturnValue arg) {
        if (arg.getType() == ReturnType.INT) {
            int argInt = (int) arg.getValue();
            return new ReturnValue(Math.abs(argInt), ReturnType.INT);
        }
        if (arg.getType() == ReturnType.FLOAT) {
            float floatInt = (float) arg.getValue();
            return new ReturnValue(Math.abs(floatInt), ReturnType.FLOAT);
        }
        return new ReturnValue(null, ReturnType.ERROR);
    }

    static ReturnValue convertToInt(ReturnValue arg) {
        if (arg.getType() == ReturnType.INT) {
            return arg;
        }
        if (arg.getType() == ReturnType.FLOAT) {
            float val = (float) arg.getValue();
            int floatInt = (int) val;
            return new ReturnValue(floatInt, ReturnType.INT);
        }
        return new ReturnValue(null, ReturnType.ERROR);
    }

    static ReturnValue convertToFloat(ReturnValue arg) {
        if (arg.getType() == ReturnType.INT) {
            int val = (int) arg.getValue();
            float floatInt = (float) val;
            return new ReturnValue(floatInt, ReturnType.FLOAT);
        }
        if (arg.getType() == ReturnType.FLOAT) {
            return arg;
        }
        return new ReturnValue(null, ReturnType.ERROR);
    }

    static ReturnValue neg(ReturnValue arg) {
        if (arg.getType() == ReturnType.INT) {
            int argInt = (int) arg.getValue();
            return new ReturnValue(-Math.abs(argInt), ReturnType.INT);
        }
        if (arg.getType() == ReturnType.FLOAT) {
            float floatInt = (int) arg.getValue();
            return new ReturnValue(-Math.abs(floatInt), ReturnType.FLOAT);
        }
        return new ReturnValue(null, ReturnType.ERROR);
    }

    static ReturnValue argMax(ReturnValue arg1, ReturnValue arg2) {
        float f1, f2;
        if (arg1.getType() == ReturnType.INT) {
            f1 = (float) ((int) arg1.getValue());
        } else {
            f1 = (float) arg1.getValue();
        }
        if (arg2.getType() == ReturnType.INT) {
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
        return new ReturnValue(res, ReturnType.INT);
    }
}
