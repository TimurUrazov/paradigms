package expression;

import expression.exceptions.ComputingException;
import expression.generic.Operator;

import java.util.Objects;

abstract class AbstractBinaryOperation<T> implements TripleExpression<T> {
    protected final TripleExpression<T> lhs, rhs;
    protected Operator<T> mode;
    private final String opString;

    protected AbstractBinaryOperation(TripleExpression<T> lhs, TripleExpression<T> rhs, String operationType, Operator<T> mode) {
        this.mode = mode;
        this.lhs = lhs;
        this.rhs = rhs;
        opString = operationType;
    }

    protected abstract T compute(T lhs, T rhs) throws ComputingException;

    @Override
    public T evaluate(T x, T y, T z) throws ComputingException {
        return compute(lhs.evaluate(x, y, z), rhs.evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return "(" + lhs.toString() + " " + opString + " " + rhs.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o.getClass().equals(this.getClass())) {
            return ((AbstractBinaryOperation<?>) o).lhs.equals(lhs) && ((AbstractBinaryOperation<?>) o).rhs.equals(rhs);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lhs, rhs, opString);
    }
}