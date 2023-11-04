import java.util.LinkedList;

public class Executor {
    public static LinkedList<String> execute(LinkedList<ParsedTokens> program){
        LinkedList<String> result = new LinkedList<String>();
        for (ParsedTokens line: program){
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

    public static IntExecutionToken getExecutionTree(ParsedTokens pt){
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

        if (pt.getType().equals(token_type.function)){
            LinkedList<IntExecutionToken> children = new LinkedList<IntExecutionToken>();
            LinkedList<ParsedTokens> args = pt.getChildren();
            for(int i = 0; i < args.size(); ++i)
                children.add(getExecutionTree(args.get(i)));
            result = new IntFunction(pt.getToken(), children);
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

class PrintFunc<T>{
    T value;
    public PrintFunc(T value){
        this.value = value;
    }

    public String execute(){
        return value.toString();
    }
}


class IntFunction extends IntExecutionToken{
    LinkedList<IntExecutionToken> args;
    public IntFunction(Token token, LinkedList<IntExecutionToken> args){
        super(token);
        this.args = args;
    }
    public int execute() throws ExecutionException {
        if (token.getValue().equals("abs")){
            if (args.size() != 1)
                throw new WrongNumberOfArgumentsException(token, 1, args.size());
            return Math.abs(args.get(0).execute());
        }
        else{
            throw new NoSuchFunctionException(token);
        }
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

class NoSuchFunctionException extends ExecutionException{
    public NoSuchFunctionException(Token t){
        super(t);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("No such function ");
        sb.append(error_token.getValue());
        sb.append(" ");
        sb.append(error_token.getLine());
        sb.append(", position is ");
        sb.append(error_token.getPos());
        return sb.toString();
    }
}

class WrongNumberOfArgumentsException extends ExecutionException{
    int needed;
    int provided;
    public WrongNumberOfArgumentsException(Token t, int needed, int provided){
        super(t);
        this.needed = needed;
        this.provided = provided;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Function ");
        sb.append(error_token.getValue());
        sb.append(" expects ");
        sb.append(needed);
        sb.append(" arguments, ");
        sb.append(provided);
        sb.append(" is given in line");
        sb.append(error_token.getLine());
        sb.append(", position is ");
        sb.append(error_token.getPos());
        return sb.toString();
    }
}


