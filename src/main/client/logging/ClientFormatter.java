package logging;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class ClientFormatter extends SimpleFormatter {

    // stores the colors in ansi escape code as string
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_MAGENTA = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_RED = "\u001B[31m";

    private final String classname; // stores the name of the class

    public ClientFormatter(String classname){
        this.classname = classname;
    }

    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();
        if (record.getLevel() == Level.INFO) {
            switch (classname) {
                case "ConnectionHandler" -> builder.append(ANSI_GREEN);
                case "Rest" -> builder.append(ANSI_BLUE);
                case "ServerEventHandler" -> builder.append(ANSI_MAGENTA);
                case "UserdataFileReader" -> builder.append(ANSI_CYAN);
                case "WebTokenReceiver" -> builder.append(ANSI_YELLOW);
                case "Gui" -> builder.append(ANSI_RED);
            }
        }
        builder.append(record.getLevel().getName() + "\n");
        builder.append(record.getLoggerName());
        builder.append(" -> ");
        builder.append(record.getMessage());
        builder.append(ANSI_RESET);
        builder.append("\n\n");
        return builder.toString();
    }
}
