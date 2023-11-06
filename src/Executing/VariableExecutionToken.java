package Executing;

import Lexing.Token;

public class VariableExecutionToken extends ExecutionToken {
    public VariableExecutionToken(Token t) {
        super(t);
    }

    public ReturnType execute() throws ExecutionException {
        return Executor.getVariable(token.getValue());
    }

}
