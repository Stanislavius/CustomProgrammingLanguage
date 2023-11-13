package Parsing.ParsedTokens;

import Lexing.Token;

import java.util.LinkedList;

public class ParsedBlock extends ParsedAbstractStatement{
    LinkedList<ParsedAbstractStatement> toDo;
    public ParsedBlock(Token t){
        super(t);
        toDo = new LinkedList<ParsedAbstractStatement>();
    }
    public ParsedTokenType getParsedType(){
        return ParsedTokenType.BLOCK;
    }

    public void addStatement(ParsedAbstractStatement statement){
        toDo.add(statement);
    }

    public ParsedAbstractStatement get(int i){
        return toDo.get(i);
    }

    public int size(){
        return toDo.size();
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < toDo.size(); ++i) {
            sb.append(toDo.get(i).toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
