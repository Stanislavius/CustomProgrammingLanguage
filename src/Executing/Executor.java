package Executing;

import Executing.ExecutionTokens.*;
import Executing.Types.*;
import Executing.Types.IntType;
import Parsing.ParsedTokens.*;

import java.util.LinkedList;

public class Executor {
    static Variables globalVariables = new Variables();
    static LinkedList<FunctionDefinitionET> stack = new LinkedList<FunctionDefinitionET>();
    static LinkedList<Variables> namespaces = new LinkedList<Variables>();

    static LinkedList<TryET> tryBlocks = new LinkedList<TryET>();
    public static String execute(LinkedList<AbstractStatementPT> program) {
        createTypes();
        LinkedList<String> output = new LinkedList<String>();
        try {
            for (AbstractStatementPT line : program) {
                //System.out.println(line.toString());
                ObjectType result = new VoidType();
                try {
                    ExecutionToken current_line = getExecutionTree(line);
                    result = current_line.execute();
                    output.add(result.toString() + "");
                } catch (ExecutionException e) {
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
        catch (EndOfExecutionException e){
            return e.getError().toString();
        }
    }

    public static void enterTryBlock(TryET tryBlock){
        tryBlocks.add(tryBlock);
    }

    public static void exitTryBlock(){
        tryBlocks.removeLast();
    }

    public static ObjectType sendError(ErrorType error) throws EndOfExecutionException {
        ObjectType result = error;
        try {
            if (!tryBlocks.isEmpty())
                result = tryBlocks.removeLast().doExcept(error);
            else {
                System.out.println(error);
                throw new EndOfExecutionException(error);
            }
        }
        catch (ExecutionException e) {
            Executor.sendError(e.getError());
        }
        return result;
    }

    public static void createTypes(){
        ClassType.createType();
        StringType.createType();
        ClassType.createType2();
        FunctionType.createType();
        DictType.createType();
        FloatType.createType();
        IntType.createType();
        ErrorType.createType();
        ListType.createType();
        VoidType.createType();
        BuiltinFunctions.createFunctions();
    }
    public static void addToStack(FunctionDefinitionET curFunc){
        stack.add(curFunc);
        namespaces.add(new Variables());
    }

    public static void removeFromStack(){
        stack.remove(stack.size()-1);
        namespaces.remove(namespaces.size()-1);
    }

    public static ExecutionToken getExecutionTree(AbstractStatementPT pt) throws ExecutionException {
        if (pt.getParsedType() == ParsedTokenType.STATEMENT){
            return getExecutionTreeExpression(((StatementPT)(pt)).getExpression());
        }
        if (pt.getParsedType() == ParsedTokenType.CONDITIONAL){
            ConditionalStatementPT pcs = (ConditionalStatementPT) pt;
            LinkedList<LinkedList<ExecutionToken>> toDos = new  LinkedList<LinkedList<ExecutionToken>>();
            LinkedList<ExecutionToken> conditions = new  LinkedList<ExecutionToken>();
            LinkedList<ExecutionToken> toDo = getExecutionTreeForBlock(pcs.getToDo());
            ExecutionToken condition = getExecutionTreeExpression(pcs.getCondition());
            while (pcs.hasNext()){
                pcs = pcs.getNext();
                toDos.add(getExecutionTreeForBlock(pcs.getToDo()));
                if (pcs.getValue().equals("else"))
                    conditions.add(new ElseET(pcs.getToken()));
                else
                    conditions.add(getExecutionTreeExpression(pcs.getCondition()));
            }
            return new BlockET(pt.getToken(), condition, toDo, conditions, toDos);
        }
        if (pt.getParsedType() == ParsedTokenType.FUNCTION_DEFINITION){
            FunctionDefinitionPT PFD = (FunctionDefinitionPT) pt;
            String name = PFD.getFunctionName();
            LinkedList<VariablePT> args = PFD.getArgs();
            LinkedList<String> variableNames = new LinkedList<String>();
            for(int i = 0; i < args.size(); ++i){
                variableNames.add(args.get(i).getValue());
            }
            BlockPT toDos = PFD.getToDo();
            LinkedList<ExecutionToken> executionToDos = new LinkedList<ExecutionToken>();
            for (int i = 0; i < toDos.size(); ++i){
                executionToDos.add(getExecutionTree(toDos.get(i)));
            }
            return new FunctionDefinitionET(pt.getToken(), name, variableNames, executionToDos);
        }

        if (pt.getParsedType() == ParsedTokenType.CLASS_DEFINITION){
            ClassDefinitionPT PCD = (ClassDefinitionPT) pt;
            String name = PCD.getClassName();
            BlockPT toDos = PCD.getToDo();
            LinkedList<ExecutionToken> executionToDos = new LinkedList<ExecutionToken>();
            for (int i = 0; i < toDos.size(); ++i){
                executionToDos.add(getExecutionTree(toDos.get(i)));
            }
            return new ClassDefinitionET(pt.getToken(), name, executionToDos);
        }

        if (pt.getParsedType() == ParsedTokenType.ASSIGNMENT){
            AssigmentStatementPT pat = (AssigmentStatementPT) pt;
            if(pat.getVariable().getClass().equals(VariablePT.class))
                return new AssignmentET(pt.getToken(), new VariableET(pat.getVariable().getToken()), getExecutionTreeExpression(pat.getExpression()));
            else
                return new AssignmentET(pt.getToken(), getExecutionTreeExpression(pat.getVariable()), getExecutionTreeExpression(pat.getExpression()));
        }

        if (pt.getParsedType() == ParsedTokenType.TRY_STATEMENT){
            TryStatementPT pts = (TryStatementPT) pt;
            LinkedList<ExceptET> excepts = new LinkedList<ExceptET>();
            LinkedList<ExceptStatementPT> parsedExcepts = pts.getExcepts();
            for (int i = 0; i < parsedExcepts.size(); ++i){
                excepts.add(getExecutionTreeForExcept(parsedExcepts.get(i)));
            }

            return new TryET(pt.getToken(), getExecutionTreeForBlock(pts.getToDo()), excepts);
        }

        return null;
    }

    public static ExceptET getExecutionTreeForExcept(ExceptStatementPT exceptStatement) throws ExecutionException {
        LinkedList<ExecutionToken> parsedTypes = new LinkedList<ExecutionToken>();
        LinkedList<VariablePT> typesOfException = exceptStatement.getTypesOfException();
        for(int i = 0; i < typesOfException.size(); ++i)
            parsedTypes.add(new VariableET(typesOfException.get(i).getToken()));
        return new ExceptET(exceptStatement.getToken(), parsedTypes, getExecutionTreeForBlock(exceptStatement.getToDo()));
    }

    public static LinkedList<ExecutionToken> getExecutionTreeForBlock(BlockPT pb) throws ExecutionException {
        LinkedList<ExecutionToken> tree = new LinkedList<ExecutionToken>();
        for (int i = 0; i < pb.size(); ++i){
            tree.add(getExecutionTree(pb.get(i)));
        }
        return tree;
    }

    public static ExecutionToken getExecutionTreeExpression(ParsedToken pt) throws ExecutionException {
        if (pt.getClass() == ListPT.class){
            ListPT PLT = (ListPT)pt;
            LinkedList<ParsedToken> parsedValues = PLT.getValues();
            LinkedList<ExecutionToken> values = new LinkedList<ExecutionToken>();
            for (int i = 0; i < parsedValues.size(); ++i)
                values.add(getExecutionTreeExpression(parsedValues.get(i)));
            LinkedList<ObjectType> objectValues = new LinkedList<ObjectType>();
            for(int i = 0; i < values.size(); ++i)
                objectValues.add(values.get(i).execute());
            return new ValueET(pt.getToken(), new ListType(objectValues));
        }

        if (pt.getClass() == DictPT.class){
            DictPT PDT = (DictPT)pt;
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

            return new ValueET(pt.getToken(), new DictType(objectKeys, objectValues));
        }

        if (pt.getParsedType() == ParsedTokenType.BINARY_OPERATION) {
            ExecutionToken left = getExecutionTreeExpression(((BinaryPT)pt).getLeft());
            ExecutionToken right = getExecutionTreeExpression(((BinaryPT)pt).getRight());
            switch (pt.getToken().getValue()){
                case ".":
                    return new MemberET(pt.getToken(), left, right);
                case "[":
                    return new ItemET(pt.getToken(), left, right);
                default:
                    return new BinaryOperationET(pt.getToken(), left, right);
            }
        }

        if (pt.getParsedType() == ParsedTokenType.UNARY_OPERATION) {
            ExecutionToken right = getExecutionTreeExpression(((UnaryPT)pt).getRight());
            return new UnaryOperationET(pt.getToken(), right);
        }

        if (pt.getClass() == IntPT.class) {
            return new ValueET(pt.getToken(),
                    new IntType(Integer.parseInt(pt.getToken().getValue())));
        }

        if (pt.getClass() == StringPT.class) {
            return new ValueET(pt.getToken(), new StringType(pt.getToken().getValue()));
        }

        if (pt.getClass() == FloatPT.class) {
            return new ValueET(pt.getToken(),
                    new FloatType(Float.parseFloat(pt.getToken().getValue())));
        }

        if (pt.getParsedType() == ParsedTokenType.FUNCTION_ARGS) {
            FunctionCallPT PFC = (FunctionCallPT)  pt;
            LinkedList<ExecutionToken> children = new LinkedList<ExecutionToken>();
            LinkedList<ParsedToken> args = PFC.getArgs();
            for (int i = 0; i < args.size(); ++i)
                children.add(getExecutionTreeExpression(args.get(i)));
            return new FunctionCallET(pt.getToken(), children);
        }

        if (pt.getClass() == VariablePT.class) {
            return new VariableET(pt.getToken());
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

