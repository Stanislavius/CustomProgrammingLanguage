package Parsing.ParsedTokens;

import Lexing.Token;

import java.util.LinkedList;

public class ParsedExceptStatement extends ParsedStatementWithBlock{
    LinkedList<ParsedVariable> typesOfException = new LinkedList<ParsedVariable>();
    public ParsedExceptStatement(Token t, int indent){
        super(t, indent);
    }

    public ParsedExceptStatement(Token t, int indent, LinkedList<ParsedVariable> typesOfException){
        super(t, indent);
        if (!(typesOfException == null))
            this.typesOfException = typesOfException;
    }

    public ParsedExceptStatement(Token t, int indent, ParsedBlock toDo){
        super(t, indent);
        this.toDo = toDo;
    }

    public ParsedExceptStatement(Token t, int indent, ParsedToken toDo){
        super(t, indent);
        this.toDo = (ParsedBlock) toDo;
    }

    public ParsedTokenType getParsedType(){
        return ParsedTokenType.EXCEPT_STATEMENT;
    }

    public String toString(){
        return "except";
    }

    public LinkedList<ParsedVariable> getTypesOfException(){
        return typesOfException;
    }
}
