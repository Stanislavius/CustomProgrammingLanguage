import java.util.LinkedList;
import java.util.function.IntBinaryOperator;

public class Executor {
    public static LinkedList<String> execute(LinkedList<ParsedTree> program){
        LinkedList<String> result = new LinkedList<String>();
        for (ParsedTree line: program){
            IntExecutionToken current_line = getExecutionTree(line);
            //System.out.println(line.toString());
            result.add(current_line.execute() + "");
        }
        return result;
    }

    public static IntExecutionToken getExecutionTree(ParsedTree pt){
        IntExecutionToken result = null;
        if (pt.getType().equals(token_type.arithmetic)){
            if (pt.hasLeft()) {
                IntExecutionToken left = getExecutionTree(pt.getLeft());
                IntExecutionToken right = getExecutionTree(pt.getRight());
                result = new BinaryNumericalOperation(pt.getToken(), left, right);
            }
            else{
                IntExecutionToken right = getExecutionTree(pt.getRight());
                result = new UnaryNumericalOperation(pt.getToken(), right);
            }
        }
        if (pt.getType().equals(token_type.INT)){
            result = new IntType(pt.getToken());
        }
        return result;
    }
}


abstract class ExecutionToken{
    Token token;
    public ExecutionToken(Token token){ this.token = token;}

}


abstract class IntExecutionToken extends ExecutionToken{
    public IntExecutionToken(Token token){super(token);}
    public abstract int execute();
}

class IntType extends IntExecutionToken{
    int value;
    public IntType(Token token){
        super(token);
    }

    public int execute(){
        return Integer.parseInt(this.token.getValue());
    }

}


class BinaryNumericalOperation extends IntExecutionToken{
    IntExecutionToken left;
    IntExecutionToken right;
    public BinaryNumericalOperation(Token token, IntExecutionToken left, IntExecutionToken right){
        super(token);
        this.left = left;
        this.right = right;
    }

    public int execute(){
        int result = 0;
        switch (token.getValue()) {
            case "+":
                result = left.execute() + right.execute();
                break;
            case "-":
                result = left.execute() - right.execute();
                break;
            case "*":
                result = left.execute() * right.execute();
                break;
            case "/":
                result = left.execute() / right.execute();
                break;
        }
        return result;
    }
}


class UnaryNumericalOperation extends IntExecutionToken{
    IntExecutionToken right;
    public UnaryNumericalOperation(Token token, IntExecutionToken right){
        super(token);
        this.right = right;
    }

    public int execute(){
        int result = 0;
        switch (token.getValue()) {
            case "+":
                result = right.execute();
                break;
            case "-":
                result =  - right.execute();
                break;
        }
        return result;
    }
}
