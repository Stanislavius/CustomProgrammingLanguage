package Parsing.ParsedTokens;

import Lexing.Token;

public class ParsedAssigmentStatement extends ParsedAbstractStatement{
    ParsedToken expression; // in fact ParsedExpression
    ParsedVariable variable;

    public ParsedAssigmentStatement(Token t, int indent){
        super(t);
        this.indentationLevel = indent;
    }

    public ParsedAssigmentStatement(int indent, ParsedToken expression){
        this(expression.getToken(), indent);
        this.expression = expression;
    }

    public ParsedAssigmentStatement(Token t, int indent, ParsedVariable variable, ParsedToken expression){
        this(t, indent);
        this.variable = variable;
        this.expression = expression;
    }

    public ParsedAssigmentStatement(Token t, int indent, ParsedToken variable, ParsedToken expression){
        this(t, indent, (ParsedVariable) variable, expression);
    }

    public void setExpression(ParsedToken expression){
        this.expression = expression;
    }

    public ParsedTokenType getParsedType(){
        return ParsedTokenType.ASSIGNMENT;
    }

    public ParsedToken getExpression(){
        return this.expression;
    }

    public ParsedVariable getVariable(){
        return this.variable;
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(variable.toString());
        sb.append("=");
        sb.append(expression.toString());
        return sb.toString();
    }
}