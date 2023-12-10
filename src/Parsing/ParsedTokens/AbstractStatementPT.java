package Parsing.ParsedTokens;

import Lexing.Token;

public class AbstractStatementPT extends ParsedToken{
    int indentationLevel;
    public AbstractStatementPT(Token t, int indent){
        super(t);
        indentationLevel = indent;
    }

    public AbstractStatementPT(Token t){
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
