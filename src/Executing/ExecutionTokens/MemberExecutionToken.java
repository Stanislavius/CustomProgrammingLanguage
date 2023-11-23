package Executing.ExecutionTokens;

import Executing.ExecutionTokens.Builtin.Types.ObjectType;
import Executing.ExecutionTokens.Builtin.Types.VoidType;
import Lexing.Token;

import java.util.LinkedList;

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
        if (member.getClass() == VariableExecutionToken.class)
            return ObjectType.getMember(objectValue, ((VariableExecutionToken)member).getToken().getValue());
        else{
            LinkedList<ObjectType> args = ((FunctionCallToken) member).executeArgs();
            args.add(0, objectValue);
            return ObjectType.getMember(objectValue, ((FunctionCallToken)member).getToken().getValue()).call(args);

        }
    }

    public ExecutionToken getObject(){
        return object;
    }

    public ExecutionToken getMember(){
        return member;
    }
}
