package expression;

import expression.exceptions.ComputingException;
import expression.generic.Operator;

import java.util.Objects;

public abstract class AbstractUnaryOperation<T> implements TripleExpression<T> {
    protected final TripleExpression<T> expression;
    protected Operator<T> mode;
    private final String opString;

    public AbstractUnaryOperation(TripleExpression<T> expression, String operationString, Operator<T> mode) {
        this.mode = mode;
        this.expression = expression;
        opString = operationString;
    }

    protected abstract T compute(T expression) throws ComputingException;

    public T evaluate(T x, T y, T z) throws ComputingException {
        return compute(expression.evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return "(" + opString + expression + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o.getClass().equals(this.getClass())) {
            return expression.equals(((AbstractUnaryOperation<?>) o).expression);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression, opString);
    }
}
