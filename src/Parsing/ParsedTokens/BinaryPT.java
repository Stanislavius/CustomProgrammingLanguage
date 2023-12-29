package Parsing.ParsedTokens;

import Lexing.Token;

public class BinaryPT extends ParsedToken{
    ParsedToken left;
    ParsedToken right;
    public BinaryPT(Token t){
        super(t);
    }

    public BinaryPT(Token t, ParsedToken left, ParsedToken right){

        super(t);
        this.left = left;
        this.right = right;
    }

    public void setLeft(ParsedToken left){
        this.left = left;
    }

    public void setRight(ParsedToken left){
        this.right = left;
    }

    public boolean hasLeft(){
        return this.left != null;
    }

    public boolean hasRight(){
        return this.right!= null;
    }

    public ParsedToken getLeft(){
        return this.left;
    }

    public ParsedToken getRight(){
        return this.right;
    }
    public ParsedTokenType getParsedType(){
        return ParsedTokenType.BINARY_OPERATION;
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        if (right.getClass() != FunctionCallPT.class)
            sb.append("(");
        sb.append(left.toString());
        if (right.getClass() != FunctionCallPT.class)
            sb.append(token.getValue());
        sb.append(right.toString());
        if (token.getValue().equals("["))
            sb.append("]");
        if (right.getClass() != FunctionCallPT.class)
            sb.append(")");
        return sb.toString();
    }
}
