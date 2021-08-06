package expression.exceptions;

public class DivisionByZeroException extends ComputingException {
    public DivisionByZeroException() {
        super("Division by zero exception");
    }

    public DivisionByZeroException(String oper, String mode) {
        super("Division by zero exception in " + oper + " operation " + " in " + mode + " mode");
    }
}