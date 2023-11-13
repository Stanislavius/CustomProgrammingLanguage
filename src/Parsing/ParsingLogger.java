package Parsing;

import Lexing.Token;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParsingLogger {
    public static Logger createParsingLogger(){
        Logger logger = Logger.getLogger("Parser");
        FileHandler fh = null;
        try {
            fh = new FileHandler("ParserLogs.log");
            fh.setLevel(Level.ALL);
            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fh.setFormatter(new ParsingFormatter());
        logger.addHandler(fh);
        return logger;
    }

    public static String tokensToString(LinkedList<Token> line){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < line.size(); ++i){
            sb.append(line.get(i).getValue());
            sb.append(" ");
        }
        return sb.toString();
    }
}
