package expression.exceptions;

public class AbsenceOfLeftBracketException extends ParsingException {
    public AbsenceOfLeftBracketException(String message) {
        super("Left bracket is absent. " + message);
    }
}
