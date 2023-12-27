package Parsing.ParsedTokens;

import Lexing.Token;

public class ConditionalStatementPT extends StatementWithBlockPT {
    ParsedToken condition;
    ConditionalStatementPT next;
    public ConditionalStatementPT(Token t, int indent){
        super(t, indent);
    }

    public ConditionalStatementPT(Token t, int indent, ParsedToken condition){
        super(t, indent);
        this.condition = condition;
    }

    public ConditionalStatementPT(Token t, int indent, ParsedToken condition, BlockPT toDo){
        super(t, indent);
        this.condition = condition;
        this.toDo = toDo;
    }

    public ConditionalStatementPT(Token t, int indent, ParsedToken condition, ParsedToken toDo){
        super(t, indent);
        this.condition = condition;
        this.toDo = (BlockPT) toDo;
    }

    public ParsedTokenType getParsedType(){
        return ParsedTokenType.CONDITIONAL;
    }

    public boolean hasNext(){
        return this.next != null;
    }

    public ConditionalStatementPT getNext() {
        return this.next;
    }

    public void append(Token t, ParsedToken condition, BlockPT toDo){
        if (hasNext())
            next.append(t, condition, toDo);
        else
            next = new ConditionalStatementPT(t, indentationLevel, condition, toDo);
    }

    public void append(ConditionalStatementPT pCD){
        if (hasNext())
            next.append(pCD);
        else
            next = pCD;
    }

    public ParsedToken getCondition() {return condition;}

    public String toString(){
        StringBuilder sb = new StringBuilder();
        if (indentationLevel != 0) {
            sb.append("\\t");
            sb.append(indentationLevel);
            sb.append(" ");
        }
        sb.append(token.getValue());
        if (condition != null) {
            sb.append(" ");
            sb.append(condition.toString());
        }
        if (toDo!=null) {
            sb.append("\n");
            sb.append(toDo.toString());
        }
        if (next != null)
            sb.append(next.toString());
        return sb.toString();
    }
}
