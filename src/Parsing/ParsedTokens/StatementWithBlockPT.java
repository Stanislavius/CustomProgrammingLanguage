package Parsing.ParsedTokens;

import Lexing.Token;

public class StatementWithBlockPT extends AbstractStatementPT {
    BlockPT toDo;
    public StatementWithBlockPT(Token t, int indent){
        super(t, indent);
    }


    public StatementWithBlockPT(Token t, int indent, BlockPT toDo){
        super(t, indent);
        this.toDo = toDo;
    }

    public StatementWithBlockPT(Token t, int indent, ParsedToken toDo){
        super(t, indent);
        this.toDo = (BlockPT) toDo;
    }

    public ParsedTokenType getParsedType(){
        return ParsedTokenType.UNKNOWN_OPERATION;
    }

    public void setToDo(BlockPT toDo){this.toDo = toDo;}

    public BlockPT getToDo(){
        return toDo;
    }
}

