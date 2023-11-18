package Executing.ExecutionTokens;

import Executing.ExecutionTokens.Builtin.Types.ObjectType;
import Executing.ExecutionTokens.Builtin.Types.VoidType;
import Lexing.Token;

public class MemberExecutionToken extends ExecutionToken {
    ExecutionToken object;
    ExecutionToken member;
    public MemberExecutionToken(Token token, ExecutionToken object, ExecutionToken member) {
        super(token);
        this.object = object;
        this.member = member;
    }

    public ObjectType execute(){
        ObjectType objectValue = object.execute();
        return ObjectType.getMember(objectValue, ((VariableExecutionToken)member).getToken().getValue());
    }

    public ExecutionToken getObject(){
        return object;
    }

    public ExecutionToken getMember(){
        return member;
    }
}
