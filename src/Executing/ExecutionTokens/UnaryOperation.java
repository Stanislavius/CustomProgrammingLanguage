package Executing.ExecutionTokens;

import Executing.ExecutionExceptions.ExecutionException;
import Executing.ExecutionTokens.Builtin.Types.ErrorType;
import Executing.ExecutionTokens.Builtin.Types.ObjectType;
import Lexing.Token;

import static Executing.ExecutionTokens.Builtin.Types.ObjectType.getMember;

public class UnaryOperation extends ExecutionToken {
    ExecutionToken right;

    public UnaryOperation(Token token, ExecutionToken right) {
        super(token);
        this.right = right;
    }

    public ObjectType execute() {
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
