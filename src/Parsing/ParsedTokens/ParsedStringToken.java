package Parsing.ParsedTokens;


import Lexing.Token;

public class ParsedStringToken extends ParsedToken{
    public ParsedStringToken(Token t){
        super(t);
    }
    public ParsedTokenType getParsedType(){
        return ParsedTokenType.STRING;
    }
    public String toString(){
        return token.getValue();
    }
}
