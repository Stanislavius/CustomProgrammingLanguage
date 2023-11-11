package Parsing.ParsedTokens;

import Lexing.Token;

public class ParsedIntToken extends ParsedToken{
    public ParsedIntToken(Token t){
        super(t);
    }
    public ParsedTokenType getParsedType(){
        return ParsedTokenType.INT;
    }
    public String toString(){
        return token.getValue();
    }
}
