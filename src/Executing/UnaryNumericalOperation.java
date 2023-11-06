package Executing;

import Lexing.Token;

public class UnaryNumericalOperation extends ExecutionToken {
    ExecutionToken right;

    public UnaryNumericalOperation(Token token, ExecutionToken right) {
        super(token);
        this.right = right;
    }

    public ReturnType execute() throws ExecutionException {
        switch (token.getValue()) {
            case "+":
                return right.execute();
            case "-":
                return UnaryNumericalOperation.sub(right.execute());
        }
        return new ReturnType<>(null, ReturnTypes.ERROR);
    }

    public static ReturnType sub(ReturnType arg) {
        if (arg.getType() == ReturnTypes.INT) {
            int argInt = (int) arg.getValue();
            return new ReturnType<Integer>(-argInt, ReturnTypes.INT);
        }
        if (arg.getType() == ReturnTypes.FLOAT) {
            float argFloat = (float) arg.getValue();
            return new ReturnType<Float>(-argFloat, ReturnTypes.FLOAT);
        }
        return new ReturnType(null, ReturnTypes.ERROR);
    }
}
