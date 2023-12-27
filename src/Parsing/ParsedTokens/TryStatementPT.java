package Parsing.ParsedTokens;

import Lexing.Token;

import java.util.LinkedList;

public class TryStatementPT extends StatementWithBlockPT {
    LinkedList<ExceptStatementPT> excepts = new LinkedList<ExceptStatementPT>();
    FinallyStatementPT finallyStatement;

    public TryStatementPT(Token t, int indent){
        super(t, indent);
    }


    public TryStatementPT(Token t, int indent, BlockPT toDo){
        super(t, indent);
        this.toDo = toDo;
    }

    public TryStatementPT(Token t, int indent, ParsedToken toDo){
        super(t, indent);
        this.toDo = (BlockPT) toDo;
    }

    public ParsedTokenType getParsedType(){
        return ParsedTokenType.TRY_STATEMENT;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        if (indentationLevel != 0) {
            sb.append("\\t");
            sb.append(indentationLevel);
            sb.append(" ");
        }
        sb.append("try");
        return sb.toString();
    }

    public void addExcept(ExceptStatementPT pet){
        this.excepts.add(pet);
    }

    public void setFinallyStatement(FinallyStatementPT pfs){
        this.finallyStatement = pfs;
    }

    public LinkedList<ExceptStatementPT> getExcepts(){
        return this.excepts;
    }
}
