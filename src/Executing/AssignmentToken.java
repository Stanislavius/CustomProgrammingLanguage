package Executing;

import Lexing.Token;

public class AssignmentToken extends ExecutionToken {
    ExecutionToken assignTo;
    ExecutionToken value;

    public AssignmentToken(Token t, ExecutionToken assignTo, ExecutionToken value) {
        super(t);
        this.assignTo = assignTo;
        this.value = value;
    }

    public ReturnType execute() throws ExecutionException {
        String name = assignTo.getToken().getValue();
        ReturnType val = value.execute();
        Executor.setVariable(name, val);
        return new ReturnType(null, ReturnTypes.EMPTY);
    }

}
