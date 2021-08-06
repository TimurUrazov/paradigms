package expression.exceptions;

public class UnsupportedOperationException extends ParsingException {
    public UnsupportedOperationException(String message) {
        super("Unsupported operation: " + message);
    }
}
