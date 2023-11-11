package Executing.ExecutionTokens;

import Executing.ExecutionExceptions.ExecutionException;
import Executing.ReturnType;
import Executing.ReturnValue;
import Lexing.Token;

public class UnaryOperation extends ExecutionToken {
    ExecutionToken right;

    public UnaryOperation(Token token, ExecutionToken right) {
        super(token);
        this.right = right;
    }

    public ReturnValue execute() throws ExecutionException {
        switch (token.getValue()) {
            case "+":
                return right.execute();
            case "-":
                return UnaryOperation.sub(right.execute());
        }
        return new ReturnValue<>(null, ReturnType.ERROR);
    }

    public static ReturnValue sub(ReturnValue arg) {
        if (arg.getType() == ReturnType.INT) {
            int argInt = (int) arg.getValue();
            return new ReturnValue<Integer>(-argInt, ReturnType.INT);
        }
        if (arg.getType() == ReturnType.FLOAT) {
            float argFloat = (float) arg.getValue();
            return new ReturnValue<Float>(-argFloat, ReturnType.FLOAT);
        }
        return new ReturnValue(null, ReturnType.ERROR);
    }
}
