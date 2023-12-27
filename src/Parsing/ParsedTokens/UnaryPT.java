package Parsing.ParsedTokens;

import Lexing.Token;

public class UnaryPT extends ParsedToken{
    ParsedToken right;
    public UnaryPT(Token t){
        super(t);
    }
    public UnaryPT(Token t, ParsedToken right){

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
        sb.append(token.getValue());
        sb.append("(");
        sb.append(right.toString());
        sb.append(")");
        return sb.toString();
    }
}
