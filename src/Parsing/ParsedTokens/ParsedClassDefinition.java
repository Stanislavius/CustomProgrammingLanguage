package Parsing.ParsedTokens;

import Lexing.Token;

import java.util.LinkedList;

public class ParsedClassDefinition extends ParsedStatementWithBlock{
    Token className;
    public ParsedClassDefinition(Token t){
        super(t, 0);
    }

    public ParsedClassDefinition(Token t, int indent, Token className){
        super(t, indent);
        this.className = className;
    }

    public ParsedClassDefinition(Token t, int indent, Token className, ParsedBlock toDo){
        super(t, indent);
        this.toDo = toDo;
        this.className = className;
    }
    public ParsedTokenType getParsedType(){
        return ParsedTokenType.CLASS_DEFINITION;
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("class ");
        sb.append(className.getValue());
        if (toDo != null) {
            sb.append("\n");
            for (int i = 0; i < toDo.size(); ++i) {
                sb.append(toDo.get(i).toString());
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public String getClassName(){
        return className.getValue();
    }
}
