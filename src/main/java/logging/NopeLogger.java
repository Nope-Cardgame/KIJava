package logging;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NopeLogger {

    public static Logger getLogger(String className) {
        Logger logger = Logger.getLogger(className);

        logger.setUseParentHandlers(false);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        consoleHandler.setFormatter(new ClientFormatter(className));
        logger.addHandler(consoleHandler);

        return logger;
    }
}
