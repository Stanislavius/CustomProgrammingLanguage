package Executing.ExecutionTokens;

import Executing.Types.ExecutionError;
import Executing.Types.ObjectType;
import Lexing.Token;

public class MemberExecutionToken extends ExecutionToken {
    ExecutionToken object;
    ExecutionToken member;
    public MemberExecutionToken(Token token, ExecutionToken object, ExecutionToken member) {
        super(token);
        this.object = object;
        this.member = member;
    }

    public ObjectType execute() throws ExecutionError {
        ObjectType result = object.execute();
        return result.getMember(member.getToken().getValue());
    }

    public String getNameMember(){
        return member.getToken().getValue();
    }
    public ObjectType executeObject() throws ExecutionError {
        return object.execute();
    }

    public ExecutionToken getObject(){
        return object;
    }

    public ExecutionToken getMember(){
        return member;
    }
}
