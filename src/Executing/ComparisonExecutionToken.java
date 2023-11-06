package Executing;

import Lexing.Token;
import Lexing.TokenType;

public class ComparisonExecutionToken extends ExecutionToken {
    ReturnValue value;
    ExecutionToken left;
    ExecutionToken right;

    public ComparisonExecutionToken(Token token, ExecutionToken left, ExecutionToken right) {
        super(token);
        this.left = left;
        this.right = right;
    }

    public ReturnValue execute() throws ExecutionException {
        ReturnValue lRes = left.execute();
        ReturnValue rRes = right.execute();
        switch (token.getValue()) {
            case "==":
                return this.equalsOperation(lRes, rRes);
            default:
                return new ReturnValue(null, ReturnType.ERROR);
        }
    }
        public static ReturnValue equalsOperation (ReturnValue left, ReturnValue right) {
            if (left.getType() == ReturnType.INT && right.getType() == ReturnType.INT) {
                int val1 = (int) left.getValue();
                int val2 = (int) right.getValue();
                int res = 0;
                if (val1 == val2)
                    res = 1;
                return new ReturnValue(res, ReturnType.INT);
            }
            return new ReturnValue(null, ReturnType.EMPTY);
        }


}
