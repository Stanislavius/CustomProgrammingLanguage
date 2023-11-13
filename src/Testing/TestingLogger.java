package Testing;

import Lexing.Token;
import Parsing.ParsingFormatter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestingLogger {
        public static Logger createTestingLogger(){
            Logger logger = Logger.getLogger("Testing");
            FileHandler fh = null;
            try {
                fh = new FileHandler("TestingLogs.log");
                fh.setLevel(Level.ALL);
                logger.setLevel(Level.ALL);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            fh.setFormatter(new ParsingFormatter());
            logger.addHandler(fh);
            return logger;
        }

}
