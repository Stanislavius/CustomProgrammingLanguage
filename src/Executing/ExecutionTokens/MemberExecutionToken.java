package Executing.ExecutionTokens;

import Executing.ExecutionExceptions.ExecutionException;
import Executing.ReturnType;
import Executing.ReturnValue;
import Lexing.Token;

public class MemberExecutionToken extends ExecutionToken {
    ExecutionToken object;
    ExecutionToken member;
    public MemberExecutionToken(Token token, ExecutionToken object, ExecutionToken member) {
        super(token);
        this.object = object;
        this.member = member;
    }

    public ReturnValue execute() throws ExecutionException {
        ReturnValue objectValue = object.execute();
        if (objectValue.getType() == ReturnType.LIST)
            return ListType.accessMember(objectValue, member);
        return null;
    }
}
