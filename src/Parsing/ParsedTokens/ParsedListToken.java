package Parsing.ParsedTokens;

import Lexing.Token;

import java.util.LinkedList;

public class ParsedListToken extends ParsedToken{
    LinkedList<ParsedToken> values;
    public ParsedListToken(Token t, LinkedList<ParsedToken> values){
        super(t);
        this.values = values;
    }
    public ParsedTokenType getParsedType(){
        return ParsedTokenType.VALUE;
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(int i = 0; i < values.size(); ++i){
            sb.append(values.get(i).toString());
            if (i != values.size() - 1)
                sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    public LinkedList<ParsedToken> getValues(){return values;}
}
