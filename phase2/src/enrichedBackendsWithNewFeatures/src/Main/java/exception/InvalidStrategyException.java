package exception;

/** An InvalidStrategyException that is thrown when the strategy is invalid. */
public class InvalidStrategyException extends Exception {
    /**
     * Construct a new InvalidStrategyException.
     *
     * @param s the String error message to be printed
     */
    public InvalidStrategyException(String s) {
        super(s);
    }
}

