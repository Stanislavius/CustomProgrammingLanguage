package Executing.ExecutionTokens;

import Executing.ExecutionExceptions.ExecutionException;
import Executing.ExecutionExceptions.ZeroDivisionException;
import Executing.ExecutionTokens.Builtin.Types.ErrorType;
import Executing.ExecutionTokens.Builtin.Types.ObjectType;
import Lexing.Token;

import java.util.Arrays;
import java.util.LinkedList;

public class BinaryOperation extends ExecutionToken {
    ExecutionToken left;
    ExecutionToken right;

    public BinaryOperation(Token token, ExecutionToken left, ExecutionToken right) {
        super(token);
        this.left = left;
        this.right = right;
    }

    public ObjectType execute() {
        ObjectType lRes;
        ObjectType rRes;
        lRes = left.execute();
        rRes = right.execute();
        LinkedList<ObjectType> args = new LinkedList<ObjectType>(Arrays.asList(lRes, rRes));
        switch (token.getValue()) {
            case "+":
                return lRes.getMember("__class__").getMember("__add__").call(args);
            case "-":
                return lRes.getMember("__class__").getMember("__sub__").call(args);
            case "*":
                return lRes.getMember("__class__").getMember("__mul__").call(args);
            case "/":
                return lRes.getMember("__class__").getMember("__div__").call(args);
            case "==":
                return lRes.getMember("__class__").getMember("__eq__").call(args);
            case "<":
                return lRes.getMember("__class__").getMember("__lt__").call(args);
            case ">":
                return lRes.getMember("__class__").getMember("__gt__").call(args);
            default:
                return new ErrorType();
        }
    }

}
