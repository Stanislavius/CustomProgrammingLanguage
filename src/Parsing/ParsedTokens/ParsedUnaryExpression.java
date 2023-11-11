package Parsing.ParsedTokens;

import Lexing.Token;

public class ParsedUnaryExpression extends ParsedToken{
    ParsedToken right;
    public ParsedUnaryExpression(Token t){
        super(t);
    }
    public ParsedUnaryExpression(Token t, ParsedToken right){

        super(t);
        this.right = right;
    }

    public void setRight(ParsedToken left){
        this.right = left;
    }

    public boolean hasRight(){
        return this.right!= null;
    }

    public ParsedToken getRight(){
        return this.right;
    }
    public ParsedTokenType getParsedType(){
        return ParsedTokenType.UNARY_OPERATION;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(token.getValue());
        sb.append(right.toString());
        sb.append(")");
        return sb.toString();
    }
}
