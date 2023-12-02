package Executing.ExecutionTokens;

import Executing.ExecutionTokens.Builtin.Types.ErrorType;
import Executing.ExecutionTokens.Builtin.Types.ExecutionError;
import Executing.ExecutionTokens.Builtin.Types.ObjectType;
import Executing.Executor;
import Lexing.Token;

public class VariableExecutionToken extends ExecutionToken {
    public VariableExecutionToken(Token t) {
        super(t);
    }

    public ObjectType execute() throws ExecutionError {
        ObjectType variable = Executor.getVariable(token.getValue());
        if (variable == null) {
            ErrorType error = new ErrorType("NoSuchVariable");
            error.setLine(token.getLine());
            error.setPosition(token.getPos());
            throw new ExecutionError(error);
        }
        return Executor.getVariable(token.getValue());
    }
}
