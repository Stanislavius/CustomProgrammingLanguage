package Executing.ExecutionTokens;

import Executing.Executor;
import Executing.Types.ExecutionException;
import Executing.Types.ObjectType;
import Executing.Types.ErrorType;
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
        Executor.logger.info("Get member " + member.toString()+ " from " + object.toString());
        ObjectType result = object.execute();
        if (member.getClass() == BinaryOperationET.class){
            try {
                BinaryOperationET memberBO = (BinaryOperationET) member;
                FunctionCallET functionCallET = (FunctionCallET) memberBO.right;
                result = result.getMember(memberBO.left.getToken().getValue()).call(functionCallET.executeArgs());
            }
            catch (ExecutionException e){
                ErrorType et = e.getError();
                et.setLine(token.getLineNum());
                et.setPosition(token.getPos());
                throw e;
            }
        }
        else {
            result = result.getMember(member.getToken().getValue());
        }
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
