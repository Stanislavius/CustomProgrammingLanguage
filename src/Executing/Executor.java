package Executing;

import Lexing.TokenType;
import Parsing.ParsedTokens;

import java.util.HashMap;
import java.util.LinkedList;

public class Executor {
    public static LinkedList<String> execute(LinkedList<ParsedTokens> program) {
        LinkedList<String> output = new LinkedList<String>();
        for (ParsedTokens line : program) {
            ExecutionToken current_line = getExecutionTree(line);
            //System.out.println(line.toString());
            try {
                ReturnValue result = current_line.execute();
                boolean flag = true;
                if (result.getType() == ReturnType.EMPTY)
                    flag = false;
                if (result.getType() == ReturnType.PRINT) {
                    //System.out.println(result.execute());
                }
                if (flag)
                    output.add(result + "");
            } catch (ExecutionException e) {
                System.out.println(e.toString());
            }
        }
        return output;
    }

    public static ExecutionToken getExecutionTree(ParsedTokens pt) {
        ExecutionToken result = null;
        if (pt.getType() == TokenType.KEYWORD){
            LinkedList<ParsedTokens> list = pt.getChildren();
            ExecutionToken condition = getExecutionTree(list.get(0));
            LinkedList<ExecutionToken> toDo = new LinkedList<ExecutionToken>();
            for(int i = 1; i < list.size(); ++i){
                toDo.add(getExecutionTree(list.get(i)));
            }
            result = new BlockExecutionToken(pt.getToken(), condition, toDo);
        }
        if (pt.getType().equals(TokenType.ASSIGNMENT)) {
            ExecutionToken left = getExecutionTree(pt.getLeft());
            ExecutionToken right = getExecutionTree(pt.getRight());
            result = new AssignmentToken(pt.getToken(), left, right);
        }
        if (pt.getType().equals(TokenType.ARITHMETIC)) {
            if (pt.hasLeft()) {
                ExecutionToken left = getExecutionTree(pt.getLeft());
                ExecutionToken right = getExecutionTree(pt.getRight());
                result = new BinaryNumericalOperation(pt.getToken(), left, right);
            } else {
                ExecutionToken right = getExecutionTree(pt.getRight());
                result = new UnaryNumericalOperation(pt.getToken(), right);
            }
        }
        if (pt.getType().equals(TokenType.INT)) {
            result = new NumericType(pt.getToken());
        }

        if (pt.getType().equals(TokenType.FLOAT)) {
            result = new NumericType(pt.getToken());
        }

        if (pt.getType().equals(TokenType.FUNCTION)) {
            LinkedList<ExecutionToken> children = new LinkedList<ExecutionToken>();
            LinkedList<ParsedTokens> args = pt.getChildren();
            for (int i = 0; i < args.size(); ++i)
                children.add(getExecutionTree(args.get(i)));
            result = new FunctionToken(pt.getToken(), children);
        }

        if (pt.getType().equals(TokenType.VARIABLE)) {
            result = new VariableExecutionToken(pt.getToken());
        }

        if (pt.getType().equals(TokenType.COMPARISON)) {
            ExecutionToken left = getExecutionTree(pt.getLeft());
            ExecutionToken right = getExecutionTree(pt.getRight());
            result = new ComparisonExecutionToken(pt.getToken(), left, right);
        }

        return result;
    }

    static class Variables {
        static HashMap<String, ReturnValue> variables = new HashMap<String, ReturnValue>();

        public void addVariable(String name, ReturnValue value) {

        }

        static public void setVariable(String name, ReturnValue value) {
            variables.put(name, value);
        }

        static public ReturnValue getVariable(String name) {
            ReturnValue value = null;
            if (variables.containsKey(name)) {
                value = variables.get(name);
            } else {
                //throw error
            }
            return value;
        }

        static public void clear() {
            variables.clear();
        }
    }

    public static void clearVariables() {
        Variables.clear();
    }

    static void setVariable(String name, ReturnValue value) {
        Variables.setVariable(name, value);
    }

    static public ReturnValue getVariable(String name) {
        return Variables.getVariable(name);
    }
}

