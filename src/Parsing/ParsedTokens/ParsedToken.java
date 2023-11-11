package Parsing.ParsedTokens;

import Lexing.Token;
import Lexing.TokenType;

import java.util.Arrays;
import java.util.HashSet;

public class ParsedToken {
    final Token token;
    public ParsedToken(Token t){
        this.token = t;
    }
    public Token getToken() {
        return this.token;
    }
    public int getLine() {
        return this.token.getLine();
    }
    public int getPos() {
        return this.token.getPos();
    }
    public String getValue() {
        return this.token.getValue();
    }
    public TokenType getType() {
        return this.token.getType();
    }
    public String toString() {
        return this.token.toString();
    }
    public  ParsedTokenType getParsedType(){return ParsedTokenType.UNPARSED;}
}
