package Executing;

import Executing.ExecutionTokens.*;
import Executing.Types.*;
import Executing.Types.IntType;
import Parsing.ParsedTokens.*;

import java.util.LinkedList;

public class Executor {
    static Variables globalVariables = new Variables();
    static LinkedList<FunctionDefinitionToken> stack = new LinkedList<FunctionDefinitionToken>();
    static LinkedList<Variables> namespaces = new LinkedList<Variables>();

    static LinkedList<TryExecutionToken> tryBlocks = new LinkedList<TryExecutionToken>();
    public static String execute(LinkedList<ParsedAbstractStatement> program) {
        createTypes();
        LinkedList<String> output = new LinkedList<String>();
        try {
            for (ParsedAbstractStatement line : program) {
                //System.out.println(line.toString());
                ObjectType result = new VoidType();
                try {
                    ExecutionToken current_line = getExecutionTree(line);
                    result = current_line.execute();
                    output.add(result.toString() + "");
                } catch (ExecutionError e) {
                    output.add(Executor.sendError(e.getError()).toString());
                }
                if (result.getType().toString().equals("str"))
                    output.add("\"" + output.removeLast() + "\"");
            }
            if (output.isEmpty())
                return new VoidType().toString();
            else
                return output.get(output.size() - 1);
        }
        catch (EndOfExecutionError e){
            return e.getError().toString();
        }
    }

    public static void enterTryBlock(TryExecutionToken tryBlock){
        tryBlocks.add(tryBlock);
    }

    public static void exitTryBlock(){
        tryBlocks.removeLast();
    }

    public static ObjectType sendError(ErrorType error) throws EndOfExecutionError {
        ObjectType result = error;
        try {
            if (!tryBlocks.isEmpty())
                result = tryBlocks.removeLast().doExcept(error);
            else {
                System.out.println(error);
                throw new EndOfExecutionError(error);
            }
        }
        catch (ExecutionError e) {
            Executor.sendError(e.getError());
        }
        return result;
    }

    public static void createTypes(){
        ClassType.createType();
        StringType.createType();
        ClassType.createType2();
        FunctionType.createType();
        ClassDict.createType();
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

    public static ExecutionToken getExecutionTree(ParsedAbstractStatement pt) throws ExecutionError {
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
            if(pat.getVariable().getClass().equals(ParsedVariable.class))
                return new AssignmentToken(pt.getToken(), new VariableExecutionToken(pat.getVariable().getToken()), getExecutionTreeExpression(pat.getExpression()));
            else
                return new AssignmentToken(pt.getToken(), getExecutionTreeExpression(pat.getVariable()), getExecutionTreeExpression(pat.getExpression()));
        }

        if (pt.getParsedType() == ParsedTokenType.TRY_STATEMENT){
            ParsedTryStatement pts = (ParsedTryStatement) pt;
            LinkedList<ExceptExecutionToken> excepts = new LinkedList<ExceptExecutionToken>();
            LinkedList<ParsedExceptStatement> parsedExcepts = pts.getExcepts();
            for (int i = 0; i < parsedExcepts.size(); ++i){
                excepts.add(getExecutionTreeForExcept(parsedExcepts.get(i)));
            }

            return new TryExecutionToken(pt.getToken(), getExecutionTreeForBlock(pts.getToDo()), excepts);
        }

        return null;
    }

    public static ExceptExecutionToken getExecutionTreeForExcept(ParsedExceptStatement exceptStatement) throws ExecutionError {
        LinkedList<ExecutionToken> parsedTypes = new LinkedList<ExecutionToken>();
        LinkedList<ParsedVariable> typesOfException = exceptStatement.getTypesOfException();
        for(int i = 0; i < typesOfException.size(); ++i)
            parsedTypes.add(new VariableExecutionToken(typesOfException.get(i).getToken()));
        return new ExceptExecutionToken(exceptStatement.getToken(), parsedTypes, getExecutionTreeForBlock(exceptStatement.getToDo()));
    }

    public static LinkedList<ExecutionToken> getExecutionTreeForBlock(ParsedBlock pb) throws ExecutionError {
        LinkedList<ExecutionToken> tree = new LinkedList<ExecutionToken>();
        for (int i = 0; i < pb.size(); ++i){
            tree.add(getExecutionTree(pb.get(i)));
        }
        return tree;
    }

    public static ExecutionToken getExecutionTreeExpression(ParsedToken pt) throws ExecutionError {
        if (pt.getClass() == ParsedListToken.class){
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

        if (pt.getClass() == ParsedDictToken.class){
            ParsedDictToken PDT = (ParsedDictToken)pt;
            LinkedList<ParsedToken> parsedValues = PDT.getValues();
            LinkedList<ParsedToken> parsedKeys = PDT.getKeys();
            LinkedList<ExecutionToken> values = new LinkedList<ExecutionToken>();
            LinkedList<ExecutionToken> keys = new LinkedList<ExecutionToken>();
            for (int i = 0; i < parsedValues.size(); ++i) {
                values.add(getExecutionTreeExpression(parsedValues.get(i)));
                keys.add(getExecutionTreeExpression(parsedKeys.get(i)));
            }
            LinkedList<ObjectType> objectValues = new LinkedList<ObjectType>();
            LinkedList<ObjectType> objectKeys = new LinkedList<ObjectType>(); //TODO, VALUES CREATED WHEN FORMING EXECUTION TREE
            for(int i = 0; i < values.size(); ++i) {
                objectValues.add(values.get(i).execute());
                objectKeys.add(keys.get(i).execute());
            }

            return new ValueExecutionToken(pt.getToken(), new ClassDict(objectKeys, objectValues));
        }

        if (pt.getParsedType() == ParsedTokenType.BINARY_OPERATION) {
            ExecutionToken left = getExecutionTreeExpression(((ParsedBinaryExpression)pt).getLeft());
            ExecutionToken right = getExecutionTreeExpression(((ParsedBinaryExpression)pt).getRight());
            switch (pt.getToken().getValue()){
                case ".":
                    return new MemberExecutionToken(pt.getToken(), left, right);
                case "[":
                    return new ItemExecutionToken(pt.getToken(), left, right);
                default:
                    return new BinaryOperation(pt.getToken(), left, right);
            }
        }

        if (pt.getParsedType() == ParsedTokenType.UNARY_OPERATION) {
            ExecutionToken right = getExecutionTreeExpression(((ParsedUnaryExpression)pt).getRight());
            return new UnaryOperation(pt.getToken(), right);
        }

        if (pt.getClass() == ParsedIntToken.class) {
            return new ValueExecutionToken(pt.getToken(),
                    new IntType(Integer.parseInt(pt.getToken().getValue())));
        }

        if (pt.getClass() == ParsedStringToken.class) {
            return new ValueExecutionToken(pt.getToken(), new StringType(pt.getToken().getValue()));
        }

        if (pt.getClass() == ParsedFloatToken.class) {
            return new ValueExecutionToken(pt.getToken(),
                    new FloatType(Float.parseFloat(pt.getToken().getValue())));
        }

        if (pt.getParsedType() == ParsedTokenType.FUNCTION_ARGS) {
            ParsedFunctionCall PFC = (ParsedFunctionCall)  pt;
            LinkedList<ExecutionToken> children = new LinkedList<ExecutionToken>();
            LinkedList<ParsedToken> args = PFC.getArgs();
            for (int i = 0; i < args.size(); ++i)
                children.add(getExecutionTreeExpression(args.get(i)));
            return new FunctionCallToken(pt.getToken(), children);
        }

        if (pt.getClass() == ParsedVariable.class) {
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

