package Executing.ExecutionTokens;

import Executing.ExecutionTokens.Builtin.Types.ObjectType;
import Executing.Executor;
import Lexing.Token;

public class VariableExecutionToken extends ExecutionToken {
    public VariableExecutionToken(Token t) {
        super(t);
    }

    public ObjectType execute() {
        return Executor.getVariable(token.getValue());
    }
}
