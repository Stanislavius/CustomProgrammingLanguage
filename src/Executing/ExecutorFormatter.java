package Executing;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class ExecutorFormatter extends Formatter {
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();
        sb.append(record.getLevel()).append(':');
        sb.append(record.getMessage()).append('\n');
        sb.append("Class and method: ").append(record.getSourceClassName()).append(".").append(record.getSourceMethodName());
        sb.append("\n");
        return sb.toString();
    }
}
