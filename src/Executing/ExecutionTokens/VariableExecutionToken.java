package Executing.ExecutionTokens;

import Executing.ExecutionExceptions.ExecutionException;
import Executing.Executor;
import Executing.ReturnValue;
import Lexing.Token;

public class VariableExecutionToken extends ExecutionToken {
    public VariableExecutionToken(Token t) {
        super(t);
    }

    public ReturnValue execute() throws ExecutionException {
        return Executor.getVariable(token.getValue());
    }

}
