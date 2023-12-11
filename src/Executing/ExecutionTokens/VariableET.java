package Executing.ExecutionTokens;

import Executing.Types.ErrorType;
import Executing.Types.ExecutionException;
import Executing.Types.ObjectType;
import Executing.Executor;
import Lexing.Token;

public class VariableET extends ExecutionToken {
    public VariableET(Token t) {
        super(t);
    }

    public ObjectType execute() throws ExecutionException {
        ObjectType variable = Executor.getVariable(token.getValue());
        if (variable == null) {
            ErrorType error = new ErrorType("NoSuchVariable");
            error.setLine(token.getLineNum());
            error.setPosition(token.getPos());
            throw new ExecutionException(error);
        }
        return Executor.getVariable(token.getValue());
    }
}
