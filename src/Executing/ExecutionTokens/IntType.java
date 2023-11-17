package Executing.ExecutionTokens;

import Executing.ReturnType;
import Executing.ReturnValue;
import Lexing.Token;
import Lexing.TokenType;

public class IntType extends ExecutionToken{
    public IntType(Token token) {
        super(token);
    }
    public ReturnValue execute() {
        return new ReturnValue<Integer>(Integer.parseInt(this.token.getValue()), ReturnType.INT);
    }
}
