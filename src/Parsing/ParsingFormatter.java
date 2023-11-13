package Parsing;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class ParsingFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            StringBuilder sb = new StringBuilder();
            sb.append(record.getLevel()).append(':');
            sb.append(record.getMessage()).append('\n');
            sb.append("Method: ");
            sb.append(record.getSourceMethodName()).append('\n');
            return sb.toString();
        }
}
