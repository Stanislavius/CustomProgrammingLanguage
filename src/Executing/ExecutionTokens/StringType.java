package Executing.ExecutionTokens;

import Executing.ReturnType;
import Executing.ReturnValue;
import Lexing.Token;
import Lexing.TokenType;

public class StringType extends ExecutionToken {
    ReturnValue value;

    public StringType(Token token) {
        super(token);
    }

    public ReturnValue execute() {
        return new ReturnValue(token.getValue(), ReturnType.STRING);
    }

}
