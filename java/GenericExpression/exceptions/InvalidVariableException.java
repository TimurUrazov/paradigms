package expression.exceptions;

public class InvalidVariableException extends ParsingException {
    public InvalidVariableException(String message) {
        super("Invalid variable: " + message);
    }
}