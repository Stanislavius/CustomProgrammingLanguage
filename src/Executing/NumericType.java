package Executing;

import Lexing.Token;
import Lexing.TokenTypes;

public class NumericType extends ExecutionToken {
    ReturnType value;

    public NumericType(Token token) {
        super(token);
    }

    public ReturnType execute() {
        if (this.token.getType() == TokenTypes.INT) {
            return new ReturnType<Integer>(Integer.parseInt(this.token.getValue()), ReturnTypes.INT);
        }
        if (this.token.getType() == TokenTypes.FLOAT) {
            return new ReturnType<Float>(Float.parseFloat(this.token.getValue()), ReturnTypes.FLOAT);
        }
        return new ReturnType(null, ReturnTypes.ERROR);
    }

}
