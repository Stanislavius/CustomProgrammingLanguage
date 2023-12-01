package Parsing.ParsedTokens;

import Lexing.Token;

import java.util.LinkedList;

public class ParsedTryStatement extends ParsedStatementWithBlock{
    LinkedList<ParsedExceptStatement> excepts = new LinkedList<ParsedExceptStatement>();
    ParsedFinallyStatement finallyStatement;

    public ParsedTryStatement(Token t, int indent){
        super(t, indent);
    }


    public ParsedTryStatement(Token t, int indent, ParsedBlock toDo){
        super(t, indent);
        this.toDo = toDo;
    }

    public ParsedTryStatement(Token t, int indent, ParsedToken toDo){
        super(t, indent);
        this.toDo = (ParsedBlock) toDo;
    }

    public ParsedTokenType getParsedType(){
        return ParsedTokenType.TRY_STATEMENT;
    }

    public String toString(){
        return "try";
    }

    public void addExcept(ParsedExceptStatement pet){
        this.excepts.add(pet);
    }

    public void setFinallyStatement(ParsedFinallyStatement pfs){
        this.finallyStatement = pfs;
    }

    public LinkedList<ParsedExceptStatement> getExcepts(){
        return this.excepts;
    }
}
