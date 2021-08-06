package expression.exceptions;


public class OverflowException extends ComputingException {
    public OverflowException() {
        super("Overflow exception");
    }

    public OverflowException(String oper, String mode) {
        super("Overflow in " + oper + " operation " + " in " + mode + " mode");
    }
}
