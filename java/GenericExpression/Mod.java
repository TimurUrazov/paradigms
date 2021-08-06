package expression;
import expression.exceptions.DivisionByZeroException;
import expression.generic.Operator;

public class Mod<T> extends AbstractBinaryOperation<T> {
    public Mod(TripleExpression<T> lhs, TripleExpression<T> rhs, Operator<T> mode) {
        super(lhs, rhs, "mod", mode);
    }

    protected T compute(T lhs, T rhs) throws DivisionByZeroException {
        return mode.mod(lhs, rhs);
    }
}
