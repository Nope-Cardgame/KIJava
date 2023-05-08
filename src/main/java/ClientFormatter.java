import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class ClientFormatter extends SimpleFormatter {
    @Override
    public String format(LogRecord record) {
        return record.getLoggerName() + " -> " + record.getMessage() + "\n\n";
    }
}
