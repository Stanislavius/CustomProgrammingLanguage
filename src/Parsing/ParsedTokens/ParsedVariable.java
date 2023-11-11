package Parsing.ParsedTokens;

import Lexing.Token;

public class ParsedVariable extends ParsedToken{
    public ParsedVariable(Token t){
        super(t);
    }

    public ParsedTokenType getParsedType(){
        return ParsedTokenType.VARIABLE;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(token.getValue());
        return sb.toString();
    }
}
