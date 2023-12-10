package Parsing.ParsedTokens;

import Lexing.Token;

public class StatementPT extends AbstractStatementPT {
    ParsedToken expression; // in fact ParsedExpression

    public StatementPT(Token t, int indent){
        super(t);
        this.indentationLevel = indent;
    }

    public StatementPT(int indent, ParsedToken expression){
        this(expression.getToken(), indent);
        this.expression = expression;
    }

    public StatementPT(Token t, int indent, ParsedToken expression){
        this(t, indent);
        this.expression = expression;
    }

    public void setExpression(ParsedToken expression){
        this.expression = expression;
    }
    public ParsedToken getExpression(){
        return this.expression;
    }
    public ParsedTokenType getParsedType(){
        return ParsedTokenType.STATEMENT;
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < indentationLevel; ++i)
            sb.append("\t");
        sb.append(expression.toString());
        return sb.toString();
    }
}
