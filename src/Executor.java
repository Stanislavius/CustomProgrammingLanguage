import java.util.HashMap;
import java.util.LinkedList;

public class Executor {
    public static LinkedList<String> execute(LinkedList<ParsedTokens> program){
        LinkedList<String> output = new LinkedList<String>();
        for (ParsedTokens line: program){
            ExecutionToken current_line = getExecutionTree(line);
            //System.out.println(line.toString());
            try {
                ReturnType result = current_line.execute();
                boolean flag = true;
                if (result.getType() == ReturnTypes.EMPTY)
                    flag = false;
                if (result.getType() == ReturnTypes.PRINT){
                    System.out.println(result.execute());
                }
                if (flag)
                    output.add(result+ "");
            }
            catch (ExecutionException e){
                System.out.println(e.toString());
            }
        }
        return output;
    }

    public static ExecutionToken getExecutionTree(ParsedTokens pt){
        ExecutionToken result = null;
        if (pt.getType().equals(token_type.assignment)){
            ExecutionToken left = getExecutionTree(pt.getLeft());
            ExecutionToken right = getExecutionTree(pt.getRight());
            result = new AssignmentToken(pt.getToken(), left, right);
        }
        if (pt.getType().equals(token_type.arithmetic)){
            if (pt.hasLeft()) {
                ExecutionToken left = getExecutionTree(pt.getLeft());
                ExecutionToken right = getExecutionTree(pt.getRight());
                result = new BinaryNumericalOperation(pt.getToken(), left, right);
            }
            else{
                ExecutionToken right = getExecutionTree(pt.getRight());
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
            LinkedList<ExecutionToken> children = new LinkedList<ExecutionToken>();
            LinkedList<ParsedTokens> args = pt.getChildren();
            for(int i = 0; i < args.size(); ++i)
                children.add(getExecutionTree(args.get(i)));
            result = new FunctionToken(pt.getToken(), children);
        }

        if(pt.getType().equals(token_type.variable)){
            result = new VariableExecutionToken(pt.getToken());
        }
        return result;
    }

    static class Variables{
        static HashMap<String, ReturnType> variables = new HashMap<String, ReturnType>();
        public void addVariable(String name, ReturnType value){

        }

        static public void setVariable(String name, ReturnType value){
            variables.put(name, value);
        }

       static public ReturnType getVariable(String name){
            ReturnType value = null;
            if (variables.containsKey(name)){
                value = variables.get(name);
            }
            else{
                //throw error
            }
            return value;
        }

        static public void clear(){
            variables.clear();
        }
    }

    static void clearVariables(){
        Variables.clear();
    }
    static void setVariable(String name, ReturnType value){
        Variables.setVariable(name, value);
    }

    static public ReturnType getVariable(String name){
        return Variables.getVariable(name);
    }
}


abstract class ExecutionToken{
    Token token;
    public ExecutionToken(Token token){ this.token = token;}

    public Token getToken() {return token;}
    public abstract ReturnType execute() throws ExecutionException;
}


class VariableExecutionToken extends ExecutionToken{
    public VariableExecutionToken(Token t){
        super(t);
    }
    public ReturnType execute() throws ExecutionException{
        return Executor.getVariable(token.getValue());
    }

}

class AssignmentToken extends ExecutionToken{
    ExecutionToken assignTo;
    ExecutionToken  value;
    public AssignmentToken(Token t, ExecutionToken assignTo,  ExecutionToken value){
        super(t);
        this.assignTo = assignTo;
        this.value = value;
    }
    public ReturnType execute() throws ExecutionException{
        String name = assignTo.getToken().getValue();
        ReturnType val = value.execute();
        Executor.setVariable(name, val);
        return new ReturnType(null, ReturnTypes.EMPTY);
    }

}


class NumericType extends ExecutionToken{
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


class FunctionToken extends  ExecutionToken {
    LinkedList< ExecutionToken> args;
    public FunctionToken(Token token, LinkedList< ExecutionToken> args){
        super(token);
        this.args = args;
    }

    public ReturnType execute() throws ExecutionException {
        switch (token.getValue()) {
            case "abs":
                if (args.size() != 1)
                    throw new WrongNumberOfArgumentsException(token, 1, args.size());
                return FunctionToken.abs(args.get(0).execute());
            case "neg":
                if (args.size() != 1)
                    throw new WrongNumberOfArgumentsException(token, 1, args.size());
                return FunctionToken.neg(args.get(0).execute());
            case "int":
                if (args.size() != 1)
                    throw new WrongNumberOfArgumentsException(token, 1, args.size());
                return FunctionToken.convertToInt(args.get(0).execute());
            case "float":
                if (args.size() != 1)
                    throw new WrongNumberOfArgumentsException(token, 1, args.size());
                return FunctionToken.convertToFloat(args.get(0).execute());
            case "print":
                if (args.size() != 1)
                    throw new WrongNumberOfArgumentsException(token, 1, args.size());
                return new ReturnType<>(args.get(0).execute(), ReturnTypes.PRINT);
            case "argmax":
                if (args.size() != 2)
                    throw new WrongNumberOfArgumentsException(token, 2, args.size());
                return FunctionToken.argMax(args.get(0).execute(), args.get(1).execute());
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
            float floatInt = (float) arg.getValue();
            return new ReturnType(Math.abs(floatInt), ReturnTypes.FLOAT);
        }
        return new ReturnType(null, ReturnTypes.ERROR);
    }

    static ReturnType convertToInt(ReturnType arg){
        if (arg.getType() == ReturnTypes.INT){
            return arg;
        }
        if (arg.getType() == ReturnTypes.FLOAT){
            float val = (float) arg.getValue();
            int floatInt = (int) val;
            return new ReturnType(floatInt, ReturnTypes.INT);
        }
        return new ReturnType(null, ReturnTypes.ERROR);
    }

    static ReturnType convertToFloat(ReturnType arg){
        if (arg.getType() == ReturnTypes.INT){
            int val = (int) arg.getValue();
            float floatInt = (float) val;
            return new ReturnType(floatInt, ReturnTypes.FLOAT);
        }
        if (arg.getType() == ReturnTypes.FLOAT){
            return arg;
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

    static ReturnType argMax(ReturnType arg1, ReturnType arg2){
        float f1, f2;
        if (arg1.getType() == ReturnTypes.INT){
            f1 = (float)((int) arg1.getValue());
        }
        else{
            f1 = (float) arg1.getValue();
        }
        if (arg2.getType() == ReturnTypes.INT){
            f2 = (float)((int) arg2.getValue());
        }
        else{
            f2 = (float) arg2.getValue();
        }
        int res = 0;
        if (f1 > f2){
            res = 1;
        }
        if (f1 < f2){
            res = -1;
        }
        if (f1 == f2){
            res = 0;
        }
        return new ReturnType(res, ReturnTypes.INT);
    }
}


class BinaryNumericalOperation extends  ExecutionToken {
    ExecutionToken left;
    ExecutionToken right;
    public BinaryNumericalOperation(Token token,  ExecutionToken left,  ExecutionToken right){
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


class UnaryNumericalOperation extends ExecutionToken {
    ExecutionToken right;
    public UnaryNumericalOperation(Token token,  ExecutionToken right){
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
            float argFloat = (float) arg.getValue();
            return new ReturnType<Float>(-argFloat, ReturnTypes.FLOAT);
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


