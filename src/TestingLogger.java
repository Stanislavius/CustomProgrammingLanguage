import Lexing.Token;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class TestingLogger {
    Logger logger;
    public TestingLogger(){
        logger = Logger.getLogger("Testing");
        FileHandler fh = null;
        try {
            fh = new FileHandler("TestingLogs.log");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.addHandler(fh);
    }

    public void info(String s){
        logger.info(s);
    }
}


