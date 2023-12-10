package Parsing.ParsedTokens;


import Lexing.Token;

public class StringPT extends ParsedToken{
    public StringPT(Token t){
        super(t);
    }
    public ParsedTokenType getParsedType(){
        return ParsedTokenType.VALUE;
    }
    public String toString(){
        return "\"" + token.getValue() + "\"";
    }
}

