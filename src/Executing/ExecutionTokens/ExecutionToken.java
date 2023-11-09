package Executing.ExecutionTokens;

import Executing.ExecutionExceptions.ExecutionException;
import Executing.ReturnValue;
import Lexing.Token;

public abstract class ExecutionToken {
    protected Token token;

    public ExecutionToken(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public abstract ReturnValue execute() throws ExecutionException;
}
