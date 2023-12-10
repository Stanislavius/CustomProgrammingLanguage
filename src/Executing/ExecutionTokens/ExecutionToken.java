package Executing.ExecutionTokens;

import Executing.Types.ExecutionException;
import Executing.Types.ObjectType;
import Lexing.Token;

public abstract class ExecutionToken {
    protected Token token;

    public ExecutionToken(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public abstract ObjectType execute() throws ExecutionException;
}
