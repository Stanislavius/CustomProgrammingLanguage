package Executing;

import Lexing.TokenType;
import Parsing.ParsedTokens;

import java.util.HashMap;
import java.util.LinkedList;

public class Executor {
    static Variables globalVariables = new Variables();
    static LinkedList<FunctionDefinitionToken> stack = new LinkedList<FunctionDefinitionToken>();
    static LinkedList<Variables> namespaces = new LinkedList<Variables>();
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

    public static void addToStack(FunctionDefinitionToken curFunc){
        stack.add(curFunc);
        namespaces.add(new Variables());
    }

    public static void removeFromStack(){
        stack.remove(stack.size()-1);
        namespaces.remove(namespaces.size()-1);
    }

    public static ExecutionToken getExecutionTree(ParsedTokens pt) {
        ExecutionToken result = null;
        if (pt.getType() == TokenType.BLOCKWORD){
            if (pt.getValue().equals("def")){
                LinkedList<ExecutionToken> toDo = new LinkedList<ExecutionToken>();
                for(int i = 1; i < pt.operandsCount(); ++i){
                    toDo.add(getExecutionTree(pt.getChildren().get(i)));
                }
                ParsedTokens declaration = pt.getChildren().get(0);
                String name = declaration.getValue();
                LinkedList<String> args = new LinkedList<String>();
                for(int i = 0; i < declaration.operandsCount(); ++i){
                    args.add(declaration.getChildren().get(i).getValue());
                }
                return new FunctionDefinitionToken(pt.getToken(), name, args, toDo);
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

    public static void clearVariables() {
        globalVariables.clear();
    }

    static void setVariable(String name, ReturnValue value) {
        if (namespaces.size() > 0){
            namespaces.get(namespaces.size() - 1).setVariable(name, value);
        }
        else {
            globalVariables.setVariable(name, value);
        }
    }

    static public ReturnValue getVariable(String name) {
        if (namespaces.size() > 0){
            if (namespaces.get(namespaces.size() - 1).in(name))
                return namespaces.get(namespaces.size() - 1).getVariable(name);
            else
                return globalVariables.getVariable(name);
        }
        return globalVariables.getVariable(name);
    }
}

