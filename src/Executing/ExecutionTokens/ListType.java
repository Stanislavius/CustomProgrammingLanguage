package Executing.ExecutionTokens;

import Executing.ExecutionExceptions.ExecutionException;
import Executing.ReturnType;
import Executing.ReturnValue;
import Lexing.Token;

import java.util.LinkedList;

public class ListType extends ExecutionToken{
    LinkedList<ExecutionToken> values;

    public ListType(Token t, LinkedList<ExecutionToken> values){
        super(t);
        this.values = values;

    }
    public ReturnValue execute() throws ExecutionException {
        return new ReturnValue(values, ReturnType.LIST);
    }

    public static ReturnValue accessMember(ReturnValue list, ExecutionToken member){
        if (member.getClass() == FunctionToken.class){
            FunctionToken ft = (FunctionToken)member;
            String functionName = ft.getToken().getValue();
            if (functionName.equals("append")){
                return ListType.append(list, ft.args.get(0));
            }
        }
        else{

        }
        return null;
    }
    public static ReturnValue append(ReturnValue list, ExecutionToken newValue){
        ((LinkedList<ExecutionToken>)list.getValue()).add(newValue);
        return new ReturnValue(null, ReturnType.VOID);
    }

}
