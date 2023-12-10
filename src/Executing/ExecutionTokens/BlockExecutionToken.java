package Executing.ExecutionTokens;

import Executing.Types.ExecutionException;
import Executing.Types.IntType;
import Executing.Types.ObjectType;
import Executing.Types.VoidType;
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
    public ObjectType execute() throws ExecutionException {
        ObjectType result = new VoidType();
        boolean flag = true;
        if (token.getValue().equals("if")){
            ObjectType conditionEvaluation = condition.execute();
            if ((((IntType) conditionEvaluation).getInt()) != 0){
                for(int i = 0; i < toDo.size(); ++i){
                    result = toDo.get(i).execute();
                }
            }
            else
                flag = false;
        }
        if (token.getValue().equals("while")){
            while ((((IntType) condition.execute()).getInt()) != 0){
                for(int i = 0; i < toDo.size(); ++i){
                    result = toDo.get(i).execute();
                }
            }
            flag = false;
        }
        if (flag == false) {
            for (int i = 0; i < elseConditions.size(); ++i) {
                if ((((IntType) elseConditions.get(i).execute()).getInt() != 0)) {
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
