package expression;

import expression.exceptions.OverflowException;
import expression.generic.Operator;

public class Subtract<T> extends AbstractBinaryOperation<T> {
    public Subtract(TripleExpression<T> lhs, TripleExpression<T> rhs, Operator<T> mode) {
      super(lhs, rhs, "-", mode);
    }

    @Override
    protected T compute(T lhs, T rhs) throws OverflowException {
        return mode.subtract(lhs, rhs);
    }
}

