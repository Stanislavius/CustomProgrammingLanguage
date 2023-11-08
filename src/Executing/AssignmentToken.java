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

    public ReturnValue execute() throws ExecutionException {
        String name = assignTo.getToken().getValue();
        ReturnValue val = value.execute();
        Executor.setVariable(name, val);
        return new ReturnValue(null, ReturnType.VOID);
    }

}
