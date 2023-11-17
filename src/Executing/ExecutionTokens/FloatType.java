package Executing.ExecutionTokens;

import Executing.ReturnType;
import Executing.ReturnValue;
import Lexing.Token;
import Lexing.TokenType;

public class FloatType extends ExecutionToken {
    public FloatType(Token token) {
        super(token);
    }
    public ReturnValue execute() {
        return new ReturnValue<Float>(Float.parseFloat(this.token.getValue()), ReturnType.FLOAT);
    }
}
