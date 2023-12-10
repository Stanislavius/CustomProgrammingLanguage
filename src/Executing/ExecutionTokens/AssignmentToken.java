package Executing.ExecutionTokens;

import Executing.Types.ExecutionException;
import Executing.Types.ObjectType;
import Executing.Types.VoidType;
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

    public ObjectType execute() throws ExecutionException {
        if(assignTo.getClass().equals(VariableExecutionToken.class)){
            String name = assignTo.getToken().getValue();
            ObjectType val = value.execute();
            Executor.setVariable(name, val);
        }

        if (assignTo.getClass().equals(MemberExecutionToken.class)){
            MemberExecutionToken met = (MemberExecutionToken)  assignTo;
            ObjectType object = met.executeObject();
            object.setMember(met.getNameMember(), value.execute());
        }

        if(assignTo.getClass().equals(ItemExecutionToken.class)){
            ((ItemExecutionToken) assignTo).executeSetItem(value.execute());
        }

        return new VoidType();
    }
}
