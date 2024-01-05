package Executing;

import Lexing.LexingFormatter;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExecutorLogger {
    public static Logger createExecutorLogger(){
        Logger logger = Logger.getLogger("Executing");
        FileHandler fh = null;
        try {
            fh = new FileHandler("ExecutingLogs.log");
            fh.setLevel(Level.ALL);
            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fh.setFormatter(new ExecutorFormatter());
        logger.addHandler(fh);
        return logger;
    }

}
