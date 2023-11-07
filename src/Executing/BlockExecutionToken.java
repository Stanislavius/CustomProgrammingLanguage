package Executing;

import Lexing.Token;
import java.util.LinkedList;

public class BlockExecutionToken extends ExecutionToken{
    ExecutionToken condition;
    LinkedList<ExecutionToken> toDo;
    public BlockExecutionToken(Token t, ExecutionToken condition, LinkedList<ExecutionToken> toDo){
        super(t);
        this.condition = condition;
        this.toDo = toDo;
    }
    public ReturnValue execute() throws ExecutionException {
        ReturnValue result = null;
        if (token.getValue().equals("if")){
            ReturnValue conditionEvaluation = condition.execute();
            if (((int) conditionEvaluation.getValue()) != 0){
                for(int i = 0; i < toDo.size(); ++i){
                    result = toDo.get(i).execute();
                }
            }
        }
        if (token.getValue().equals("while")){
            while (((int) condition.execute().getValue()) != 0){
                for(int i = 0; i < toDo.size(); ++i){
                    result = toDo.get(i).execute();
                }
            }
        }
        return new ReturnValue(null, ReturnType.EMPTY);
    }
}
