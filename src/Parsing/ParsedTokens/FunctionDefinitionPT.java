package Parsing.ParsedTokens;

import Lexing.Token;

import java.util.LinkedList;

public class FunctionDefinitionPT extends StatementWithBlockPT {
    Token functionName;
    LinkedList<VariablePT> args = new LinkedList<VariablePT>(); //in fact ParsedVariables
    public FunctionDefinitionPT(Token t){
        super(t, 0);
    }

    public FunctionDefinitionPT(Token t, int indent, Token functionName){
        super(t, indent);
        this.functionName = functionName;
    }
    public FunctionDefinitionPT(Token t, int indent, Token functionName, LinkedList<VariablePT> args, BlockPT toDo){
        super(t, indent);
        this.args = args;
        this.toDo = toDo;
        this.functionName = functionName;
    }

    public FunctionDefinitionPT(Token t, int indent, Token functionName, LinkedList<VariablePT> args){
        super(t, indent);
        this.args = args;
        this.functionName = functionName;
    }
    public ParsedTokenType getParsedType(){
        return ParsedTokenType.FUNCTION_DEFINITION;
    }
    public LinkedList<VariablePT> getArgs(){
        return args;
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("def ");
        sb.append(functionName.getValue());
        sb.append("(");
        for(int i = 0; i < args.size(); ++i){
            sb.append(args.get(i).toString());
            sb.append(" ");
        }
        sb.append(")");
        return sb.toString();
    }

    public String getFunctionName(){
        return functionName.getValue();
    }

    public Token getLastTokenInLine(){
        if (args.size() > 0)
            return args.getLast().getToken();
        else
            return functionName;
    }
}
