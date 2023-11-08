package Executing;

import Lexing.TokenType;
import Parsing.ParsedTokens;

import java.util.HashMap;
import java.util.LinkedList;

public class Executor {
    public static String execute(LinkedList<ParsedTokens> program) {
        LinkedList<String> output = new LinkedList<String>();
        for (ParsedTokens line : program) {
            ExecutionToken current_line = getExecutionTree(line);
            //System.out.println(line.toString());
            try {
                ReturnValue result = current_line.execute();
                output.add(result.toString() + "");
            } catch (ExecutionException e) {
                System.out.println(e.toString());
            }
        }
        return output.get(output.size()-1);
    }

    public static ExecutionToken getExecutionTree(ParsedTokens pt) {
        ExecutionToken result = null;
        if (pt.getType() == TokenType.BLOCKWORD){
            if (pt.getValue().equals("else")){
                //
            }
            else {
                LinkedList<ParsedTokens> list = pt.getChildren();
                ExecutionToken condition = getExecutionTree(list.get(0));
                LinkedList<ExecutionToken> toDo = new LinkedList<ExecutionToken>();
                LinkedList<ExecutionToken> elseConditions = new LinkedList<ExecutionToken>();
                LinkedList<LinkedList<ExecutionToken>> elseToDos = new LinkedList<LinkedList<ExecutionToken>>();
                for (int i = 1; i < list.size(); ++i) {
                    ParsedTokens curToken = list.get(i);
                    if (curToken.getValue().equals("elif")) {
                        elseConditions.add(getExecutionTree(curToken.getChildren().get(0)));
                        LinkedList<ExecutionToken> elseToDo = new LinkedList<ExecutionToken>();
                        for (int j = 1; j < curToken.operandsCount(); ++j)
                            elseToDo.add(getExecutionTree(curToken.getChildren().get(j)));
                        elseToDos.add(elseToDo);
                    } else {
                        if (curToken.getValue().equals("else")) {
                            elseConditions.add(new ElseExecutionToken(curToken.getToken()));
                            LinkedList<ExecutionToken> elseToDo = new LinkedList<ExecutionToken>();
                            for (int j = 0; j < curToken.operandsCount(); ++j)
                                elseToDo.add(getExecutionTree(curToken.getChildren().get(j)));
                            elseToDos.add(elseToDo);
                        } else {
                            toDo.add(getExecutionTree(list.get(i)));
                        }
                    }
                }
                result = new BlockExecutionToken(pt.getToken(), condition, toDo, elseConditions, elseToDos);
            }
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

