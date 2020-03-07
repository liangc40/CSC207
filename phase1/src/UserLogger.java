import java.util.logging.Logger;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;

/** A UserLogger that generates logs to CardHolders or AdminUsers. */
class UserLogger {

    /**
     * The name of the output file, might be a cardholder's email address or the daily report of the
     * city of the admin user.
     */
    private String logId;

    /**
     * Set the name of the log file to logId.
     *
     * @param logId the name of the log to be set
     */
    void setLogId(String logId) {
        this.logId = logId;
    }

    /**
     * Return the Logger that will log the messages to a file with logId and to the console.
     *
     * @return the customized Logger
     */
    Logger getLogger() {
        // create a new Logger
        Logger userLogger = Logger.getLogger("");
        LogManager.getLogManager().reset();
        userLogger.setLevel(Level.ALL);
        try {
            // customize our Logger with our format and file name
            MyFormatter formatter = new MyFormatter();
            userLogger = Logger.getLogger("test");
            // The file handler of this UserLogger
            FileHandler fileHandler = new FileHandler(logId + " info.txt", true);
            // messages of all logging levels would be logged to the file
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(formatter);
            /* The console handler of this UserLogger. */
            ConsoleHandler consoleHandler = new ConsoleHandler();
            // only messages of logging level equal to or higher than WARNING would be
            // logged to the console
            consoleHandler.setLevel(Level.WARNING);
            consoleHandler.setFormatter(formatter);
            userLogger.addHandler(fileHandler);
            userLogger.addHandler(consoleHandler);
    } catch (IOException e) {
            e.printStackTrace();
        }
        return userLogger;
    }
}