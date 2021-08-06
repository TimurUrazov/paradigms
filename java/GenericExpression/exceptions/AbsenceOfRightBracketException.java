package expression.exceptions;

public class AbsenceOfRightBracketException extends ParsingException {
    public AbsenceOfRightBracketException(String message) {
        super("Right bracket is absent. " + message);
    }
}
