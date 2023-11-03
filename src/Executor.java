import java.util.LinkedList;

public class Executor {
    public static LinkedList<String> execute(LinkedList<ParsedTree> program){
        LinkedList<String> result = new LinkedList<String>();
        for (ParsedTree line: program){
            IntExecutionToken current_line = getExecutionTree(line);
            //System.out.println(line.toString());
            try {
                result.add(current_line.execute() + "");
            }
            catch (ExecutionException e){
                System.out.println(e.toString());
            }
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

    public Token getToken() {return token;}
}


abstract class IntExecutionToken extends ExecutionToken{
    public IntExecutionToken(Token token){super(token);}
    public abstract int execute() throws ExecutionException;
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

    public int execute() throws ExecutionException{
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
                int right_int = right.execute();
                if(right_int == 0){
                    throw new ZeroDivisionException(this.getToken());
                }
                result = left.execute() / right_int;
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

    public int execute() throws ExecutionException{
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

class ExecutionException extends Exception{
    protected Token error_token;

    public ExecutionException(Token t){
        error_token = t;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Runtime error in line ");
        sb.append(error_token.getLine());
        sb.append(", position is ");
        sb.append(error_token.getPos());
        return sb.toString();
    }

}

class ZeroDivisionException extends ExecutionException {
    public ZeroDivisionException(Token t){
        super(t);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Division by zero in line ");
        sb.append(error_token.getLine());
        sb.append(", position is ");
        sb.append(error_token.getPos());
        return sb.toString();
    }

}
