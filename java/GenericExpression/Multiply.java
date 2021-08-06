package expression;

import expression.exceptions.OverflowException;
import expression.generic.Operator;

public class Multiply<T> extends AbstractBinaryOperation<T> {
    public Multiply(TripleExpression<T> lhs, TripleExpression<T> rhs, Operator<T> mode) {
        super(lhs, rhs, "*", mode);
    }

    protected T compute(T lhs, T rhs) throws OverflowException {
        return mode.multiply(lhs, rhs);
    }
}

