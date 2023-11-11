package Parsing;

import Lexing.Token;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class ParsingLogger {
    Logger logger;
    public ParsingLogger(){
        logger = Logger.getLogger("Parser");
        FileHandler fh = null;
        try {
            fh = new FileHandler("ParserLogs.log");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.addHandler(fh);
    }

    public String tokensToString(LinkedList<Token> line){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < line.size(); ++i){
            sb.append(line.get(i).getValue());
            sb.append(" ");
        }
        return sb.toString();
    }

    public void info(String s){
        logger.info(s);
    }
}
