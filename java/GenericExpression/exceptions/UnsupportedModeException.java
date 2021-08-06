package expression.exceptions;

public class UnsupportedModeException extends Exception {
    public UnsupportedModeException(String message) {
        super("Unsupported mode: " + message);
    }
}
