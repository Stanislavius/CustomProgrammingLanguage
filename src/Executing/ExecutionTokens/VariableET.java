package Executing.ExecutionTokens;

import Executing.Types.ErrorType;
import Executing.Types.ExecutionException;
import Executing.Types.ObjectType;
import Executing.Executor;
import Lexing.Token;

import java.util.LinkedList;

public class VariableET extends ExecutionToken {
    public VariableET(Token t) {
        super(t);
    }
    ObjectType replacement = null;

    public ObjectType execute() throws ExecutionException {
        if (replacement != null){
            Executor.logger.info("Get value of variable " + token.getValue() + " which is outer-defined and equals " + replacement);
            return replacement;
        }
        Executor.logger.info("Get value of variable " + token.getValue());
        ObjectType variable = Executor.getVariable(token.getValue());
        if (variable == null) {
            ErrorType error = new ErrorType("NoSuchVariable");
            error.setLine(token.getLineNum());
            error.setPosition(token.getPos());
            throw new ExecutionException(error);
        }
        return variable;
    }

    public String toString(){
        return token.getValue();
    }

    public void replaceOuterVariableIfHasAny(LinkedList<String> args) throws ExecutionException {
        boolean present = false;
        for(int i = 0; i < args.size(); ++i){
            if (args.get(i).equals(this.token.getValue())){
                present = true;
                break;
            }
        }
        if (!present){
            Executor.logger.info("Get value of outer variable " + token.getValue());
            ObjectType variable = Executor.getVariable(token.getValue());
            if (variable == null) {
                ErrorType error = new ErrorType("NoSuchVariable");
                error.setLine(token.getLineNum());
                error.setPosition(token.getPos());
                throw new ExecutionException(error);
            }
            Executor.logger.info("Get value of outer variable " + token.getValue() + " replacement = " + variable);
            this.replacement = variable;
        }
    }
}
