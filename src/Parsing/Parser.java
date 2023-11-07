package Parsing;

import Lexing.Token;
import Lexing.TokenType;

import java.util.*;

public class Parser {
    public LinkedList<ParsedTokens> parse(LinkedList<Token> tokens){
        LinkedList<ParsedTokens> program= new LinkedList<ParsedTokens>();
        try {
            LinkedList<ParsedTokens> block = new LinkedList<ParsedTokens>();
            for (Token token : tokens) {
                if (token.getType().equals(TokenType.NEWLINE)) {
                    program.add(new ParsedTokens(block));
                    block = new LinkedList<ParsedTokens>();
                } else {
                    block.add(new ParsedTokens(token));
                }
            }
            program = processBlocks(program);

        }
        catch (ParsingException e){
            System.out.println(e);
        }
        return program;
    }

    public LinkedList<ParsedTokens> processBlocks(LinkedList<ParsedTokens> tokens){
        int lowerIndent = 0;
        for (ParsedTokens curLine: tokens){
            if (curLine.getIndentationLevel() > lowerIndent)
                lowerIndent = curLine.getIndentationLevel();
        }
        LinkedList<ParsedTokens> block = new LinkedList<ParsedTokens>();
        while (lowerIndent != 0) {
            ParsedTokens head = null;
            Iterator iter = tokens.iterator();
            while (iter.hasNext()) {
                ParsedTokens curLine = (ParsedTokens) iter.next();
                if (curLine.getType() == TokenType.KEYWORD && curLine.getIndentationLevel() == lowerIndent - 1) {
                    if (head == null)
                        head = curLine;
                    else {
                        Iterator iter2 = block.iterator();
                        while(iter2.hasNext()){
                            head.addChild((ParsedTokens) iter2.next());
                        }
                        head = curLine;
                        block.clear();
                    }
                }
                else {
                    if (head != null) {
                        if (curLine.getIndentationLevel() == lowerIndent) {
                            block.add(curLine);
                            iter.remove();
                        }
                        else{
                            Iterator iter2 = block.iterator();
                            while(iter2.hasNext()){
                                head.addChild((ParsedTokens) iter2.next());
                            }
                            head = null;
                            block.clear();
                        }
                    }
                }
            }
            lowerIndent--;
            if (block.size() > 0){
                Iterator iter2 = block.iterator();
                while(iter2.hasNext()){
                    head.addChild((ParsedTokens) iter2.next());
                }
                head = null;
                block.clear();
            }
        }
        System.out.println(lowerIndent);
        return tokens;
    }
}
