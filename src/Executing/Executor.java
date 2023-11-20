package Executing;

import Executing.ExecutionTokens.*;
import Executing.ExecutionTokens.Builtin.Types.*;
import Executing.ExecutionTokens.Builtin.Types.IntType;
import Parsing.ParsedTokens.*;

import java.util.LinkedList;

public class Executor {
    static Variables globalVariables = new Variables();
    static LinkedList<FunctionDefinitionToken> stack = new LinkedList<FunctionDefinitionToken>();
    static LinkedList<Variables> namespaces = new LinkedList<Variables>();
    public static String execute(LinkedList<ParsedAbstractStatement> program) {
        createTypes();
        LinkedList<String> output = new LinkedList<String>();
        for (ParsedAbstractStatement line : program) {
            ExecutionToken current_line = getExecutionTree(line);
            //System.out.println(line.toString());
            ObjectType result = current_line.execute();
            output.add(result.toString() + "");
            if (result.getType().toString().equals("str"))
                output.add("\"" + output.removeLast() + "\"");
        }
        return output.get(output.size()-1);
    }

    public static void createTypes(){
        ClassType.createType();
        StringType.createType();
        ClassType.createType2();
        FunctionType.createType();
        FloatType.createType();
        IntType.createType();
        ErrorType.createType();
        ListType.createType();
        VoidType.createType();
        BuiltinFunctions.createFunctions();
    }
    public static void addToStack(FunctionDefinitionToken curFunc){
        stack.add(curFunc);
        namespaces.add(new Variables());
    }

    public static void removeFromStack(){
        stack.remove(stack.size()-1);
        namespaces.remove(namespaces.size()-1);
    }

    public static ExecutionToken getExecutionTree(ParsedAbstractStatement pt) {
        if (pt.getParsedType() == ParsedTokenType.STATEMENT){
            return getExecutionTreeExpression(((ParsedStatement)(pt)).getExpression());
        }
        if (pt.getParsedType() == ParsedTokenType.CONDITIONAL){
            ParsedConditionalStatement pcs = (ParsedConditionalStatement) pt;
            LinkedList<LinkedList<ExecutionToken>> toDos = new  LinkedList<LinkedList<ExecutionToken>>();
            LinkedList<ExecutionToken> conditions = new  LinkedList<ExecutionToken>();
            LinkedList<ExecutionToken> toDo = getExecutionTreeForBlock(pcs.getToDo());
            ExecutionToken condition = getExecutionTreeExpression(pcs.getCondition());
            while (pcs.hasNext()){
                pcs = pcs.getNext();
                toDos.add(getExecutionTreeForBlock(pcs.getToDo()));
                if (pcs.getValue().equals("else"))
                    conditions.add(new ElseExecutionToken(pcs.getToken()));
                else
                    conditions.add(getExecutionTreeExpression(pcs.getCondition()));
            }
            return new BlockExecutionToken(pt.getToken(), condition, toDo, conditions, toDos);
        }
        if (pt.getParsedType() == ParsedTokenType.FUNCTION_DEFINITION){
            ParsedFunctionDefinition PFD = (ParsedFunctionDefinition) pt;
            String name = PFD.getFunctionName();
            LinkedList<ParsedVariable> args = PFD.getArgs();
            LinkedList<String> variableNames = new LinkedList<String>();
            for(int i = 0; i < args.size(); ++i){
                variableNames.add(args.get(i).getValue());
            }
            ParsedBlock toDos = PFD.getToDo();
            LinkedList<ExecutionToken> executionToDos = new LinkedList<ExecutionToken>();
            for (int i = 0; i < toDos.size(); ++i){
                executionToDos.add(getExecutionTree(toDos.get(i)));
            }
            return new FunctionDefinitionToken(pt.getToken(), name, variableNames, executionToDos);
        }

        if (pt.getParsedType() == ParsedTokenType.CLASS_DEFINITION){
            ParsedClassDefinition PCD = (ParsedClassDefinition) pt;
            String name = PCD.getClassName();
            ParsedBlock toDos = PCD.getToDo();
            LinkedList<ExecutionToken> executionToDos = new LinkedList<ExecutionToken>();
            for (int i = 0; i < toDos.size(); ++i){
                executionToDos.add(getExecutionTree(toDos.get(i)));
            }
            return new ClassDefinitionToken(pt.getToken(), name, executionToDos);
        }

        if (pt.getParsedType() == ParsedTokenType.ASSIGNMENT){
            ParsedAssigmentStatement pat = (ParsedAssigmentStatement) pt;
            if (pat.getVariable().getClass() == ParsedMembership.class) {
                ParsedMembership pm = (ParsedMembership) pat.getVariable();
                MemberExecutionToken met = new MemberExecutionToken(pm.getToken(),
                        getExecutionTreeExpression(pm.getObject()),
                        getExecutionTreeExpression(pm.getMember()));
                return new AssignmentToken(pt.getToken(), met, getExecutionTreeExpression(pat.getExpression()));
            }
            else
                return new AssignmentToken(pt.getToken(), new VariableExecutionToken(pat.getVariable().getToken()), getExecutionTreeExpression(pat.getExpression()));
        }
        return null;
    }

    public static LinkedList<ExecutionToken> getExecutionTreeForBlock(ParsedBlock pb){
        LinkedList<ExecutionToken> tree = new LinkedList<ExecutionToken>();
        for (int i = 0; i < pb.size(); ++i){
            tree.add(getExecutionTree(pb.get(i)));
        }
        return tree;
    }

    public static ExecutionToken getExecutionTreeExpression(ParsedToken pt){
        if (pt.getParsedType() == ParsedTokenType.LIST){
            ParsedListToken PLT = (ParsedListToken)pt;
            LinkedList<ParsedToken> parsedValues = PLT.getValues();
            LinkedList<ExecutionToken> values = new LinkedList<ExecutionToken>();
            for (int i = 0; i < parsedValues.size(); ++i)
                values.add(getExecutionTreeExpression(parsedValues.get(i)));
            LinkedList<ObjectType> objectValues = new LinkedList<ObjectType>();
            for(int i = 0; i < values.size(); ++i)
                objectValues.add(values.get(i).execute());
            return new ValueExecutionToken(pt.getToken(), new ListType(objectValues));
        }
        if (pt.getParsedType() == ParsedTokenType.BINARY_OPERATION) {
            ExecutionToken left = getExecutionTreeExpression(((ParsedBinaryExpression)pt).getLeft());
            ExecutionToken right = getExecutionTreeExpression(((ParsedBinaryExpression)pt).getRight());
            return new BinaryOperation(pt.getToken(), left, right);
        }

        if (pt.getParsedType() == ParsedTokenType.UNARY_OPERATION) {
            ExecutionToken right = getExecutionTreeExpression(((ParsedUnaryExpression)pt).getRight());
            return new UnaryOperation(pt.getToken(), right);
        }

        if (pt.getParsedType() == ParsedTokenType.INT) {
            return new ValueExecutionToken(pt.getToken(),
                    new IntType(Integer.parseInt(pt.getToken().getValue())));
        }

        if (pt.getParsedType() == ParsedTokenType.MEMBERSHIP_FUNCTION_CALL) {
            ParsedMembership pm = (ParsedMembership) pt;
            return new MemberExecutionToken(pm.getToken(),
                    getExecutionTreeExpression(pm.getObject()),
                    getExecutionTreeExpression(pm.getMember()));
        }

        if (pt.getParsedType() == ParsedTokenType.MEMBERSHIP_VARIABLE) {
            ParsedMembership pm = (ParsedMembership) pt;
            return new MemberExecutionToken(pm.getToken(),
                    getExecutionTreeExpression(pm.getObject()),
                    getExecutionTreeExpression(pm.getMember()));
        }

        if (pt.getParsedType() == ParsedTokenType.STRING) {
            return new ValueExecutionToken(pt.getToken(), new StringType(pt.getToken().getValue()));
        }

        if (pt.getParsedType() == ParsedTokenType.FLOAT) {
            return new ValueExecutionToken(pt.getToken(),
                    new FloatType(Float.parseFloat(pt.getToken().getValue())));
        }

        if (pt.getParsedType() == ParsedTokenType.FUNCTION_CALL) {
            ParsedFunctionCall PFC = (ParsedFunctionCall)  pt;
            LinkedList<ExecutionToken> children = new LinkedList<ExecutionToken>();
            LinkedList<ParsedToken> args = PFC.getArgs();
            for (int i = 0; i < args.size(); ++i)
                children.add(getExecutionTreeExpression(args.get(i)));
            return new FunctionCallToken(pt.getToken(), children);
        }

        if (pt.getParsedType() == ParsedTokenType.VARIABLE) {
            return new VariableExecutionToken(pt.getToken());
        }

        return null;
    }

    public static void clearVariables() {
        globalVariables.clear();
    }

    public static void setVariable(String name, ObjectType value) {
        if (namespaces.size() > 0){
            namespaces.get(namespaces.size() - 1).setVariable(name, value);
        }
        else {
            globalVariables.setVariable(name, value);
        }
    }

    public static ObjectType getVariable(String name) {
        int i = namespaces.size()-1;
        while (i!= -1 && !namespaces.get(i).in(name)) {
            i--;
        }
        if (i == - 1){
            return globalVariables.getVariable(name);
        }
        else{
            return namespaces.get(i).getVariable(name);
        }
    }
}

