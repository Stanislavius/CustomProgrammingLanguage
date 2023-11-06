package Executing;

import Lexing.Token;
import Lexing.TokenTypes;
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
                ReturnType result = current_line.execute();
                boolean flag = true;
                if (result.getType() == ReturnTypes.EMPTY)
                    flag = false;
                if (result.getType() == ReturnTypes.PRINT) {
                    System.out.println(result.execute());
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
        if (pt.getType().equals(TokenTypes.assignment)) {
            ExecutionToken left = getExecutionTree(pt.getLeft());
            ExecutionToken right = getExecutionTree(pt.getRight());
            result = new AssignmentToken(pt.getToken(), left, right);
        }
        if (pt.getType().equals(TokenTypes.arithmetic)) {
            if (pt.hasLeft()) {
                ExecutionToken left = getExecutionTree(pt.getLeft());
                ExecutionToken right = getExecutionTree(pt.getRight());
                result = new BinaryNumericalOperation(pt.getToken(), left, right);
            } else {
                ExecutionToken right = getExecutionTree(pt.getRight());
                result = new UnaryNumericalOperation(pt.getToken(), right);
            }
        }
        if (pt.getType().equals(TokenTypes.INT)) {
            result = new NumericType(pt.getToken());
        }

        if (pt.getType().equals(TokenTypes.FLOAT)) {
            result = new NumericType(pt.getToken());
        }

        if (pt.getType().equals(TokenTypes.function)) {
            LinkedList<ExecutionToken> children = new LinkedList<ExecutionToken>();
            LinkedList<ParsedTokens> args = pt.getChildren();
            for (int i = 0; i < args.size(); ++i)
                children.add(getExecutionTree(args.get(i)));
            result = new FunctionToken(pt.getToken(), children);
        }

        if (pt.getType().equals(TokenTypes.variable)) {
            result = new VariableExecutionToken(pt.getToken());
        }
        return result;
    }

    static class Variables {
        static HashMap<String, ReturnType> variables = new HashMap<String, ReturnType>();

        public void addVariable(String name, ReturnType value) {

        }

        static public void setVariable(String name, ReturnType value) {
            variables.put(name, value);
        }

        static public ReturnType getVariable(String name) {
            ReturnType value = null;
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

    static void setVariable(String name, ReturnType value) {
        Variables.setVariable(name, value);
    }

    static public ReturnType getVariable(String name) {
        return Variables.getVariable(name);
    }
}

