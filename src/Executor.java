import java.util.LinkedList;

public class Executor {
    public static LinkedList<String> execute(LinkedList<ParsedTokens> program){
        LinkedList<String> result = new LinkedList<String>();
        for (ParsedTokens line: program){
            NumericExecutionToken current_line = getExecutionTree(line);
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

    public static NumericExecutionToken getExecutionTree(ParsedTokens pt){
        NumericExecutionToken result = null;
        if (pt.getType().equals(token_type.arithmetic)){
            if (pt.hasLeft()) {
                NumericExecutionToken left = getExecutionTree(pt.getLeft());
                NumericExecutionToken right = getExecutionTree(pt.getRight());
                result = new BinaryNumericalOperation(pt.getToken(), left, right);
            }
            else{
                NumericExecutionToken right = getExecutionTree(pt.getRight());
                result = new UnaryNumericalOperation(pt.getToken(), right);
            }
        }
        if (pt.getType().equals(token_type.INT)){
            result = new NumericType(pt.getToken());
        }

        if (pt.getType().equals(token_type.FLOAT)){
            result = new NumericType(pt.getToken());
        }

        if (pt.getType().equals(token_type.function)){
            LinkedList<NumericExecutionToken> children = new LinkedList<NumericExecutionToken>();
            LinkedList<ParsedTokens> args = pt.getChildren();
            for(int i = 0; i < args.size(); ++i)
                children.add(getExecutionTree(args.get(i)));
            result = new NumericFunction(pt.getToken(), children);
        }
        return result;
    }
}


abstract class ExecutionToken{
    Token token;
    public ExecutionToken(Token token){ this.token = token;}

    public Token getToken() {return token;}
}


abstract class NumericExecutionToken extends ExecutionToken{
    public NumericExecutionToken(Token token){super(token);}
    public abstract ReturnType execute() throws ExecutionException;
}


class NumericType extends NumericExecutionToken {
    ReturnType value;
    public NumericType(Token token){
        super(token);
    }

    public ReturnType execute(){
        if (this.token.getType() == token_type.INT)
        {
            return new ReturnType<Integer>(Integer.parseInt(this.token.getValue()), ReturnTypes.INT);
        }
        if (this.token.getType() == token_type.FLOAT)
        {
            return new ReturnType<Float>(Float.parseFloat(this.token.getValue()), ReturnTypes.FLOAT);
        }
        return new ReturnType(null, ReturnTypes.ERROR);
    }

}

enum ReturnTypes{
    INT,
    FLOAT,
    EMPTY,
    PRINT,
    RETURN,
    ERROR
}

class ReturnType<T>{
    T value;
    ReturnTypes type;

    public ReturnType(T value, ReturnTypes type){
        this.value = value;
        this.type = type;
    }

    public T execute(){
        return value;
    }

    public ReturnTypes getType(){
        return type;
    }

    public T getValue(){
        return value;
    }

    public String toString(){
        return value.toString();
    }
}


class NumericFunction extends NumericExecutionToken {
    LinkedList<NumericExecutionToken> args;
    public NumericFunction(Token token, LinkedList<NumericExecutionToken> args){
        super(token);
        this.args = args;
    }

    public ReturnType execute() throws ExecutionException {
        switch (token.getValue()) {
            case "abs":
                if (args.size() != 1)
                    throw new WrongNumberOfArgumentsException(token, 1, args.size());
                return NumericFunction.abs(args.get(0).execute());
            case "neg":
                if (args.size() != 1)
                    throw new WrongNumberOfArgumentsException(token, 1, args.size());
                return NumericFunction.neg(args.get(0).execute());
            default:
                throw new NoSuchFunctionException(token);
        }

    }

    static ReturnType abs(ReturnType arg){
        if (arg.getType() == ReturnTypes.INT){
            int argInt = (int) arg.getValue();
            return new ReturnType(Math.abs(argInt), ReturnTypes.INT);
        }
        if (arg.getType() == ReturnTypes.FLOAT){
            float floatInt = (int) arg.getValue();
            return new ReturnType(Math.abs(floatInt), ReturnTypes.FLOAT);
        }
        return new ReturnType(null, ReturnTypes.ERROR);
    }

    static ReturnType neg(ReturnType arg){
        if (arg.getType() == ReturnTypes.INT){
            int argInt = (int) arg.getValue();
            return new ReturnType(-Math.abs(argInt), ReturnTypes.INT);
        }
        if (arg.getType() == ReturnTypes.FLOAT){
            float floatInt = (int) arg.getValue();
            return new ReturnType(-Math.abs(floatInt), ReturnTypes.FLOAT);
        }
        return new ReturnType(null, ReturnTypes.ERROR);
    }
}


class BinaryNumericalOperation extends NumericExecutionToken {
    NumericExecutionToken left;
    NumericExecutionToken right;
    public BinaryNumericalOperation(Token token, NumericExecutionToken left, NumericExecutionToken right){
        super(token);
        this.left = left;
        this.right = right;
    }

    public ReturnType execute() throws ExecutionException{
        ReturnType result;
        ReturnType lRes;
        ReturnType rRes;
        switch (token.getValue()) {
            case "+":
                lRes = left.execute();
                rRes = right.execute();
                result = BinaryNumericalOperation.sum(lRes, rRes);
                break;
            case "-":
                lRes = left.execute();
                rRes = right.execute();
                result = BinaryNumericalOperation.sub(rRes, lRes);
                break;
            case "*":
                lRes = left.execute();
                rRes = right.execute();
                result = BinaryNumericalOperation.mul(lRes, rRes);
                break;
            case "/":
                lRes = left.execute();
                rRes = right.execute();
                result = BinaryNumericalOperation.div(rRes, lRes, this.token);
                break;
            default:
                result = new ReturnType<String>(null, ReturnTypes.ERROR);
        }
        return result;
    }

    static ReturnType mul(ReturnType lRes, ReturnType rRes) throws ExecutionException{
        if (lRes.getType() == ReturnTypes.INT && rRes.getType() == ReturnTypes.INT){
            int lInt = (int) lRes.getValue();
            int rInt = (int) rRes.getValue();
            return new ReturnType<Integer>(rInt * lInt, ReturnTypes.INT);
        }
        if (lRes.getType() == ReturnTypes.FLOAT && rRes.getType() == ReturnTypes.INT){
            float lInt = (float) lRes.getValue();
            int rInt = (int) rRes.getValue();
            return new ReturnType<Float>(rInt * lInt, ReturnTypes.FLOAT);
        }
        if (lRes.getType() == ReturnTypes.INT && rRes.getType() == ReturnTypes.FLOAT){
            int lInt = (int) lRes.getValue();
            float rInt = (float) rRes.getValue();
            return new ReturnType<Float>(rInt * lInt, ReturnTypes.FLOAT);
        }
        if (lRes.getType() == ReturnTypes.FLOAT && rRes.getType() == ReturnTypes.FLOAT){
            float lInt = (float) lRes.getValue();
            float rInt = (float) rRes.getValue();
            return new ReturnType<Float>(rInt * lInt, ReturnTypes.FLOAT);
        }
        return new ReturnType(null, ReturnTypes.ERROR);
    }

    static ReturnType div(ReturnType lRes, ReturnType rRes, Token t) throws ExecutionException{
        if (lRes.getType() == ReturnTypes.INT && rRes.getType() == ReturnTypes.INT){
            int lInt = (int) lRes.getValue();
            int rInt = (int) rRes.getValue();
            if(rInt == 0){
                throw new ZeroDivisionException(t);
            }
            return new ReturnType<Integer>(rInt / lInt, ReturnTypes.INT);
        }
        if (lRes.getType() == ReturnTypes.FLOAT && rRes.getType() == ReturnTypes.INT){
            float lInt = (float) lRes.getValue();
            int rInt = (int) rRes.getValue();
            if(rInt == 0){
                throw new ZeroDivisionException(t);
            }
            return new ReturnType<Float>(rInt / lInt, ReturnTypes.FLOAT);
        }
        if (lRes.getType() == ReturnTypes.INT && rRes.getType() == ReturnTypes.FLOAT){
            int lInt = (int) lRes.getValue();
            float rInt = (float) rRes.getValue();
            if(rInt == 0.0){
                throw new ZeroDivisionException(t);
            }
            return new ReturnType<Float>(rInt / lInt, ReturnTypes.FLOAT);
        }
        if (lRes.getType() == ReturnTypes.FLOAT && rRes.getType() == ReturnTypes.FLOAT){
            float lInt = (float) lRes.getValue();
            float rInt = (float) rRes.getValue();
            if(rInt == 0.0){
                throw new ZeroDivisionException(t);
            }
            return new ReturnType<Float>(rInt / lInt, ReturnTypes.FLOAT);
        }
        return new ReturnType(null, ReturnTypes.ERROR);
    }

    static ReturnType sum(ReturnType lRes, ReturnType rRes){
        if (lRes.getType() == ReturnTypes.INT && rRes.getType() == ReturnTypes.INT){
            int lInt = (int) lRes.getValue();
            int rInt = (int) rRes.getValue();
            return new ReturnType<Integer>(rInt + lInt, ReturnTypes.INT);
        }
        if (lRes.getType() == ReturnTypes.FLOAT && rRes.getType() == ReturnTypes.INT){
            float lInt = (float) lRes.getValue();
            int rInt = (int) rRes.getValue();
            return new ReturnType<Float>(rInt + lInt, ReturnTypes.FLOAT);
        }
        if (lRes.getType() == ReturnTypes.INT && rRes.getType() == ReturnTypes.FLOAT){
            int lInt = (int) lRes.getValue();
            float rInt = (float) rRes.getValue();
            return new ReturnType<Float>(rInt + lInt, ReturnTypes.FLOAT);
        }
        if (lRes.getType() == ReturnTypes.FLOAT && rRes.getType() == ReturnTypes.FLOAT){
            float lInt = (float) lRes.getValue();
            float rInt = (float) rRes.getValue();
            return new ReturnType<Float>(rInt + lInt, ReturnTypes.FLOAT);
        }
        return new ReturnType(null, ReturnTypes.ERROR);
    }

    static ReturnType sub(ReturnType lRes, ReturnType rRes){
        if (lRes.getType() == ReturnTypes.INT && rRes.getType() == ReturnTypes.INT){
            int lInt = (int) lRes.getValue();
            int rInt = (int) rRes.getValue();
            return new ReturnType<Integer>(rInt - lInt, ReturnTypes.INT);
        }
        if (lRes.getType() == ReturnTypes.FLOAT && rRes.getType() == ReturnTypes.INT){
            float lInt = (float) lRes.getValue();
            int rInt = (int) rRes.getValue();
            return new ReturnType<Float>(rInt - lInt, ReturnTypes.FLOAT);
        }
        if (lRes.getType() == ReturnTypes.INT && rRes.getType() == ReturnTypes.FLOAT){
            int lInt = (int) lRes.getValue();
            float rInt = (float) rRes.getValue();
            return new ReturnType<Float>(rInt - lInt, ReturnTypes.FLOAT);
        }
        if (lRes.getType() == ReturnTypes.FLOAT && rRes.getType() == ReturnTypes.FLOAT){
            float lInt = (float) lRes.getValue();
            float rInt = (float) rRes.getValue();
            return new ReturnType<Float>(rInt - lInt, ReturnTypes.FLOAT);
        }
        return new ReturnType(null, ReturnTypes.ERROR);
    }
}


class UnaryNumericalOperation extends NumericExecutionToken {
    NumericExecutionToken right;
    public UnaryNumericalOperation(Token token, NumericExecutionToken right){
        super(token);
        this.right = right;
    }

    public ReturnType execute() throws ExecutionException{
        switch (token.getValue()) {
            case "+":
                return right.execute();
            case "-":
                return UnaryNumericalOperation.sub(right.execute());
        }
        return new ReturnType<>(null, ReturnTypes.ERROR);
    }

    public static ReturnType sub(ReturnType arg){
        if (arg.getType() == ReturnTypes.INT){
            int argInt = (int) arg.getValue();
            return new ReturnType<Integer>(-argInt, ReturnTypes.INT);
        }
        if (arg.getType() == ReturnTypes.FLOAT){
            int argFloat = (int) arg.getValue();
            return new ReturnType<Integer>(-argFloat, ReturnTypes.FLOAT);
        }
        return new ReturnType(null, ReturnTypes.ERROR);
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


