package Executing;

import Lexing.Token;
import java.util.LinkedList;

public class BlockExecutionToken extends ExecutionToken{
    ExecutionToken condition;
    LinkedList<ExecutionToken> elseConditions;
    LinkedList<LinkedList<ExecutionToken>> elseToDos;
    LinkedList<ExecutionToken> toDo;
    public BlockExecutionToken(Token t, ExecutionToken condition, LinkedList<ExecutionToken> toDo,
                               LinkedList<ExecutionToken> elseConditions,
                               LinkedList<LinkedList<ExecutionToken>> elseToDos){
        super(t);
        this.condition = condition;
        this.toDo = toDo;
        this.elseToDos = elseToDos;
        this.elseConditions = elseConditions;
    }
    public ReturnValue execute() throws ExecutionException {
        ReturnValue result = new ReturnValue(null, ReturnType.VOID);
        boolean flag = true;
        if (token.getValue().equals("if")){
            ReturnValue conditionEvaluation = condition.execute();
            if (((int) conditionEvaluation.getValue()) != 0){
                for(int i = 0; i < toDo.size(); ++i){
                    result = toDo.get(i).execute();
                }
            }
            else
                flag = false;
        }
        if (token.getValue().equals("while")){
            while (((int) condition.execute().getValue()) != 0){
                for(int i = 0; i < toDo.size(); ++i){
                    result = toDo.get(i).execute();
                }
            }
            flag = false;
        }
        if (flag == false) {
            for (int i = 0; i < elseConditions.size(); ++i) {
                if ((((int) elseConditions.get(i).execute().getValue()) != 0)) {
                    for (int j = 0; j < elseToDos.get(i).size(); ++j) {
                        result = elseToDos.get(i).get(j).execute();
                    }
                    break;
                }
            }
        }
        return result;
    }
}
