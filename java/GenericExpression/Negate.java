package expression;

import expression.exceptions.OverflowException;
import expression.generic.Operator;

public final class Negate<T> extends AbstractUnaryOperation<T> {

    public Negate(TripleExpression<T> x, Operator<T> mode) {
        super(x, "-", mode);
    }

    @Override
    protected T compute(T x) throws OverflowException {
        return mode.negate(x);
    }
}
