package Parsing.ParsedTokens;

import Lexing.Token;

public class AssigmentStatementPT extends AbstractStatementPT {
    ParsedToken expression; // in fact ParsedExpression
    ParsedToken variable;

    public AssigmentStatementPT(Token t, int indent){
        super(t);
        this.indentationLevel = indent;
    }

    public AssigmentStatementPT(int indent, ParsedToken expression){
        this(expression.getToken(), indent);
        this.expression = expression;
    }

    public AssigmentStatementPT(Token t, int indent, ParsedToken variable, ParsedToken expression){
        this(t, indent);
        this.variable = variable;
        this.expression = expression;
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

    public ParsedToken getVariable(){
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