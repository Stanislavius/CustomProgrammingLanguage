package Executing.ExecutionTokens;

import Executing.ExecutionExceptions.ExecutionException;
import Executing.ExecutionTokens.Builtin.Types.ObjectType;
import Executing.ExecutionTokens.Builtin.Types.VoidType;
import Executing.Executor;
import Lexing.Token;

public class AssignmentToken extends ExecutionToken {
    ExecutionToken assignTo;
    ExecutionToken value;

    public AssignmentToken(Token t, ExecutionToken assignTo, ExecutionToken value) {
        super(t);
        this.assignTo = assignTo;
        this.value = value;
    }

    public ObjectType execute() {
        if (assignTo.getClass() == VariableExecutionToken.class) {
            String name = assignTo.getToken().getValue();
            ObjectType val = value.execute();
            Executor.setVariable(name, val);
        }
        else{
            if (assignTo.getClass() == MemberExecutionToken.class) {
                MemberExecutionToken met = (MemberExecutionToken)  assignTo;
                String name = met.getObject().getToken().getValue();
                ObjectType rObject = Executor.getVariable(name);
                rObject.setMember(((VariableExecutionToken)met.getMember()).getToken().getValue(), value.execute());
            }
        }
        return new VoidType();
    }

}
