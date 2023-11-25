package Parsing.ParsedTokens;

import Lexing.Token;

public class ParsedStatementWithBlock extends ParsedAbstractStatement{
    ParsedBlock toDo;
    public ParsedStatementWithBlock(Token t, int indent){
        super(t, indent);
    }


    public ParsedStatementWithBlock(Token t, int indent, ParsedBlock toDo){
        super(t, indent);
        this.toDo = toDo;
    }

    public ParsedStatementWithBlock(Token t, int indent, ParsedToken toDo){
        super(t, indent);
        this.toDo = (ParsedBlock) toDo;
    }

    public ParsedTokenType getParsedType(){
        return ParsedTokenType.UNKNOWN_OPERATION;
    }

    public void setToDo(ParsedBlock toDo){this.toDo = toDo;}

    public ParsedBlock getToDo(){
        return toDo;
    }
}

