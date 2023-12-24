package Lexing;

import Parsing.ParsingFormatter;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LexingLogger {
    public static Logger createLexingLogger(){
        Logger logger = Logger.getLogger("Lexing");
        FileHandler fh = null;
        try {
            fh = new FileHandler("LexingLogs.log");
            fh.setLevel(Level.ALL);
            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fh.setFormatter(new LexingFormatter());
        logger.addHandler(fh);
        return logger;
    }

}
