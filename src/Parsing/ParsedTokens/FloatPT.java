package Parsing.ParsedTokens;

import Lexing.Token;

public class FloatPT extends ParsedToken{
    public FloatPT(Token t){
        super(t);
    }
    public ParsedTokenType getParsedType(){
        return ParsedTokenType.VALUE;
    }
    public String toString(){
        return token.getValue();
    }
}
