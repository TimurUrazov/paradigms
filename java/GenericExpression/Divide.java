package expression;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;
import expression.generic.Operator;

public class Divide<T> extends AbstractBinaryOperation<T> {
    public Divide(TripleExpression<T> lhs, TripleExpression<T> rhs, Operator<T> mode) {
        super(lhs, rhs, "/", mode);
    }

    protected T compute(T lhs, T rhs) throws OverflowException, DivisionByZeroException {
        return mode.divide(lhs, rhs);
    }
}