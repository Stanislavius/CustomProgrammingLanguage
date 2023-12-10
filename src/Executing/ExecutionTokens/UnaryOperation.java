package Executing.ExecutionTokens;

import Executing.Types.ErrorType;
import Executing.Types.ExecutionException;
import Executing.Types.ObjectType;
import Lexing.Token;

public class UnaryOperation extends ExecutionToken {
    ExecutionToken right;

    public UnaryOperation(Token token, ExecutionToken right) {
        super(token);
        this.right = right;
    }

    public ObjectType execute() throws ExecutionException {
        ObjectType arg = right.execute();
        switch (token.getValue()) {
            case "+":
                return arg;
            case "-":
                return arg.getMember("__class__").getMember("__neg__").call(arg);
        }
        return new ErrorType();
    }

}
