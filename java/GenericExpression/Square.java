package expression;

import expression.exceptions.OverflowException;
import expression.generic.Operator;

public class Square<T> extends AbstractUnaryOperation<T> {
    public Square(TripleExpression<T> expression, Operator<T> mode) {
        super(expression, "square", mode);
    }

    @Override
    protected T compute(T expression) throws OverflowException {
        return mode.multiply(expression, expression);
    }
}
