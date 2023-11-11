package Parsing.ParsedTokens;

import Lexing.Token;

public class ParsedAbstractStatement extends ParsedToken{
    int indentationLevel;
    public ParsedAbstractStatement(Token t, int indent){
        super(t);
        indentationLevel = indent;
    }

    public ParsedAbstractStatement(Token t){
        super(t);
        indentationLevel = 0;
    }

    public int getIndentationLevel(){
        return indentationLevel;
    }

    public void raiseIndentationLevel(){
        indentationLevel += 1;
    }
    public ParsedTokenType getParsedType(){
        return ParsedTokenType.STATEMENT;
    }
}
