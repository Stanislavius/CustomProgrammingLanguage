package Parsing.ParsedTokens;

import Lexing.Token;

public class ParsedFinallyStatement extends ParsedStatementWithBlock{

    public ParsedFinallyStatement(Token t, int indent) {
        super(t, indent);
    }

    public ParsedFinallyStatement(Token t, int indent, ParsedBlock toDo) {
        super(t, indent, toDo);
    }

    public ParsedFinallyStatement(Token t, int indent, ParsedToken toDo) {
        super(t, indent, toDo);
    }

    public ParsedTokenType getParsedType(){
        return ParsedTokenType.FINALLY_STATEMENT;
    }
}
