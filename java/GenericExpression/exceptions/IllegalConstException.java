package expression.exceptions;

public class IllegalConstException extends ParsingException {
    public IllegalConstException(String message) {
        super("Illegal const: " + message);
    }
}
