package Executing;

import Lexing.Token;
import Lexing.TokenType;

public class NumericType extends ExecutionToken {
    ReturnValue value;

    public NumericType(Token token) {
        super(token);
    }

    public ReturnValue execute() {
        if (this.token.getType() == TokenType.INT) {
            return new ReturnValue<Integer>(Integer.parseInt(this.token.getValue()), ReturnType.INT);
        }
        if (this.token.getType() == TokenType.FLOAT) {
            return new ReturnValue<Float>(Float.parseFloat(this.token.getValue()), ReturnType.FLOAT);
        }
        return new ReturnValue(null, ReturnType.ERROR);
    }

}
