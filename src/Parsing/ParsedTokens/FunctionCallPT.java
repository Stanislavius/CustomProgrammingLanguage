package Parsing.ParsedTokens;

import Lexing.Token;

import java.util.LinkedList;

public class FunctionCallPT extends ParsedToken{
    LinkedList<ParsedToken> args = new LinkedList<ParsedToken>();
    public FunctionCallPT(Token t){
        super(t);
    }
    public FunctionCallPT(Token t, LinkedList<ParsedToken> args){
        this(t);
        this.args = args;
    }
    public ParsedTokenType getParsedType(){
        return ParsedTokenType.FUNCTION_ARGS;
    }

    public LinkedList<ParsedToken> getArgs(){
        return this.args;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for(int i = 0; i < args.size(); ++i){
            sb.append(args.get(i).toString());
            if (i != args.size() - 1)
                sb.append(", ");
        }
        sb.append(")");
        return sb.toString();
    }
}
