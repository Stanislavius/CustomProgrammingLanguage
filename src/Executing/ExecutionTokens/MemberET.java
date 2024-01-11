package Executing.ExecutionTokens;

import Executing.Executor;
import Executing.Types.ExecutionException;
import Executing.Types.ObjectType;
import Executing.Types.ErrorType;
import Lexing.Token;

import java.util.LinkedList;

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
                if (!et.hasLineNum()) {
                    et.setLine(token.getLineNum());
                    et.setPosition(token.getPos());
                }
                throw e;
            }
        }
        else {
            try {
                result = result.getMember(member.getToken().getValue());
            }
            catch (ExecutionException e){
                ErrorType et = e.getError();
                if (!et.hasLineNum()) {
                    et.setLine(token.getLineNum());
                    et.setPosition(token.getPos());
                }
                throw e;
            }

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

    public void replaceOuterVariableIfHasAny(LinkedList<String> args) throws ExecutionException {
        object.replaceOuterVariableIfHasAny(args);
        member.replaceOuterVariableIfHasAny(args);
    }
}
