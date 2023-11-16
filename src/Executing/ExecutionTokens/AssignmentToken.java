package Executing.ExecutionTokens;

import Executing.ExecutionExceptions.ExecutionException;
import Executing.Executor;
import Executing.ReturnType;
import Executing.ReturnValue;
import Executing.Variables;
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
        if (assignTo.getClass() == VariableExecutionToken.class) {
            String name = assignTo.getToken().getValue();
            ReturnValue val = value.execute();
            Executor.setVariable(name, val);
        }
        else{
            if (assignTo.getClass() == MemberExecutionToken.class) {
                MemberExecutionToken met = (MemberExecutionToken)  assignTo;
                String name = met.getObject().getToken().getValue();
                ReturnValue rObject = Executor.getVariable(name);
                ObjectType object = (ObjectType) rObject.getValue();
                object.setVariable(((VariableExecutionToken)met.getMember()).getToken().getValue(), value.execute());
            }
        }
        return new ReturnValue(null, ReturnType.VOID);
    }

}
