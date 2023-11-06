package Executing;

import Lexing.Token;

public abstract class ExecutionToken {
    Token token;

    public ExecutionToken(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public abstract ReturnValue execute() throws ExecutionException;
}
