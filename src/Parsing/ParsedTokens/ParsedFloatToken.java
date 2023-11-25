package Parsing.ParsedTokens;

import Lexing.Token;

public class ParsedFloatToken extends ParsedToken{
    public ParsedFloatToken(Token t){
        super(t);
    }
    public ParsedTokenType getParsedType(){
        return ParsedTokenType.VALUE;
    }
    public String toString(){
        return token.getValue();
    }
}
