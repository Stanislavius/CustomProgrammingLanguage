package Parsing.ParsedTokens;

import Lexing.Token;

public class VariablePT extends ParsedToken{
    public VariablePT(Token t){
        super(t);
    }

    public ParsedTokenType getParsedType(){
        return ParsedTokenType.VALUE;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(token.getValue());
        return sb.toString();
    }
}
