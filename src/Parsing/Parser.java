package Parsing;

import Lexing.Token;
import Lexing.TokenTypes;
import Parsing.ParsedTokens;
import Parsing.ParsingException;

import java.util.*;

public class Parser {
    public LinkedList<ParsedTokens> parse(LinkedList<Token> tokens){
        LinkedList<ParsedTokens> program= new LinkedList<ParsedTokens>();
        try {
            LinkedList<ParsedTokens> block = new LinkedList<ParsedTokens>();
            for (Token token : tokens) {
                if (token.getType().equals(TokenTypes.new_line)) {
                    program.add(new ParsedTokens(block));
                    block = new LinkedList<ParsedTokens>();
                } else {
                    block.add(new ParsedTokens(token));
                }
            }
        }
        catch (ParsingException e){
            System.out.println(e);
        }
        return program;
    }
}
