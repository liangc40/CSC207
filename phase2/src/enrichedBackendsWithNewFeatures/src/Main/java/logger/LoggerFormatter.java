package logger;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * MyFormatter class provides support for formatting a customized LogRecords.
 * It takes a LogRecord and converts it to a string.
 *
 */

class LoggerFormatter extends Formatter {
    @Override
    /**
     * Returns a formatted string using the specified format string and arguments for LogRecords.
     *
     * @param record LogRecord used to pass logging requests between the logging framework and
     *     individual log Handlers
     * @return String representing formatted string using the specified format string and arguments
     *     for LogRecords.
     */
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();
        builder.append(record.getLevel() + ": ");
        builder.append(formatMessage(record));
        builder.append(System.lineSeparator());
        return builder.toString();
    }
}