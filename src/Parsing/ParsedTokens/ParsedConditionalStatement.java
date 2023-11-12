package Parsing.ParsedTokens;

import Lexing.Token;

public class ParsedConditionalStatement extends ParsedStatementWithBlock{
    ParsedToken condition;
    ParsedConditionalStatement next;
    public ParsedConditionalStatement(Token t, int indent){
        super(t, indent);
    }

    public ParsedConditionalStatement(Token t, int indent, ParsedToken condition){
        super(t, indent);
        this.condition = condition;
    }

    public ParsedConditionalStatement(Token t, int indent, ParsedToken condition, ParsedBlock toDo){
        super(t, indent);
        this.condition = condition;
        this.toDo = toDo;
    }

    public ParsedConditionalStatement(Token t, int indent, ParsedToken condition, ParsedToken toDo){
        super(t, indent);
        this.condition = condition;
        this.toDo = (ParsedBlock) toDo;
    }

    public ParsedTokenType getParsedType(){
        return ParsedTokenType.CONDITIONAL;
    }

    public boolean hasNext(){
        return this.next != null;
    }

    public ParsedConditionalStatement getNext() {
        return this.next;
    }

    public void append(Token t, ParsedToken condition, ParsedBlock toDo){
        if (hasNext())
            next.append(t, condition, toDo);
        else
            next = new ParsedConditionalStatement(t, indentationLevel, condition, toDo);
    }

    public void append(ParsedConditionalStatement pCD){
        if (hasNext())
            next.append(pCD);
        else
            next = pCD;
    }

    public ParsedToken getCondition() {return condition;}

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(token.getValue());
        sb.append(" ");
        if (condition != null)
            sb.append(condition.toString());
        return sb.toString();
    }
}
