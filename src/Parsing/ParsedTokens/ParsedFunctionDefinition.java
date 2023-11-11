package Parsing.ParsedTokens;

import Lexing.Token;

import java.util.LinkedList;

public class ParsedFunctionDefinition extends ParsedStatementWithBlock{
    Token functionName;
    LinkedList<ParsedVariable> args = new LinkedList<ParsedVariable>(); //in fact ParsedVariables
    public ParsedFunctionDefinition(Token t){
        super(t, 0);
    }

    public ParsedFunctionDefinition(Token t, int indent, Token functionName){
        super(t, indent);
        this.functionName = functionName;
    }
    public ParsedFunctionDefinition(Token t, int indent, Token functionName, LinkedList<ParsedVariable> args, ParsedBlock toDo){
        super(t, indent);
        this.args = args;
        this.toDo = toDo;
        this.functionName = functionName;
    }

    public ParsedFunctionDefinition(Token t, int indent, Token functionName, LinkedList<ParsedVariable> args){
        super(t, indent);
        this.args = args;
        this.functionName = functionName;
    }
    public ParsedTokenType getParsedType(){
        return ParsedTokenType.FUNCTION_DEFINITION;
    }
    public LinkedList<ParsedVariable> getArgs(){
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
}
