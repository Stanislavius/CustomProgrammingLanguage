package Executing.ExecutionTokens;

import Executing.Executor;
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
        result = result.getMember(member.getToken().getValue());
        Executor.logger.info("Get member " + member.toString()+ " from " + object.toString()+ ", result is " + result.toString());
        return result;
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
    public String toString(){
        return object.toString() + "." + member.toString();
    }
}
