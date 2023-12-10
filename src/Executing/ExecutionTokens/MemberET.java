package Executing.ExecutionTokens;

import Executing.Types.ExecutionException;
import Executing.Types.ObjectType;
import Lexing.Token;

public class MemberET extends ExecutionToken {
    ExecutionToken object;
    ExecutionToken member;
    public MemberET(Token token, ExecutionToken object, ExecutionToken member) {
        super(token);
        this.object = object;
        this.member = member;
    }

    public ObjectType execute() throws ExecutionException {
        ObjectType result = object.execute();
        return result.getMember(member.getToken().getValue());
    }

    public String getNameMember(){
        return member.getToken().getValue();
    }
    public ObjectType executeObject() throws ExecutionException {
        return object.execute();
    }

    public ExecutionToken getObject(){
        return object;
    }

    public ExecutionToken getMember(){
        return member;
    }
}
