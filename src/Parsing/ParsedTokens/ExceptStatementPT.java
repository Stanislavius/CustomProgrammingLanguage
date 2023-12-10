package Parsing.ParsedTokens;

import Lexing.Token;

import java.util.LinkedList;

public class ExceptStatementPT extends StatementWithBlockPT {
    LinkedList<VariablePT> typesOfException = new LinkedList<VariablePT>();
    public ExceptStatementPT(Token t, int indent){
        super(t, indent);
    }

    public ExceptStatementPT(Token t, int indent, LinkedList<VariablePT> typesOfException){
        super(t, indent);
        if (!(typesOfException == null))
            this.typesOfException = typesOfException;
    }

    public ExceptStatementPT(Token t, int indent, BlockPT toDo){
        super(t, indent);
        this.toDo = toDo;
    }

    public ExceptStatementPT(Token t, int indent, ParsedToken toDo){
        super(t, indent);
        this.toDo = (BlockPT) toDo;
    }

    public ParsedTokenType getParsedType(){
        return ParsedTokenType.EXCEPT_STATEMENT;
    }

    public String toString(){
        return "except";
    }

    public LinkedList<VariablePT> getTypesOfException(){
        return typesOfException;
    }
}
