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
        if (indentationLevel != 0) {
            sb.append("\\t");
            sb.append(indentationLevel);
            sb.append(" ");
        }
        sb.append(expression.toString());
        sb.append("\n");
        return sb.toString();
    }
}
