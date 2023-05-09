import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class ClientFormatter extends SimpleFormatter {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";

    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();
        if (record.getLevel() == Level.INFO) {
            builder.append(ANSI_GREEN);
        }
        builder.append(record.getLoggerName());
        builder.append(" -> ");
        builder.append(record.getMessage());
        builder.append(ANSI_RESET);
        builder.append("\n\n");
        return builder.toString();
    }
}
