package Parsing.ParsedTokens;

import Lexing.Token;

public class FinallyStatementPT extends StatementWithBlockPT {

    public FinallyStatementPT(Token t, int indent) {
        super(t, indent);
    }

    public FinallyStatementPT(Token t, int indent, BlockPT toDo) {
        super(t, indent, toDo);
    }

    public FinallyStatementPT(Token t, int indent, ParsedToken toDo) {
        super(t, indent, toDo);
    }

    public ParsedTokenType getParsedType(){
        return ParsedTokenType.FINALLY_STATEMENT;
    }
}
