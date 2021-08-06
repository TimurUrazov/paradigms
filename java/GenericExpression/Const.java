package expression;

import expression.generic.Operator;

import java.util.Objects;

public class Const<T> implements TripleExpression<T> {
    private final String value;
    private final Operator<T> mode;

    public Const(String value, Operator<T> mode) {
        this.mode = mode;
        this.value = value;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return mode.parseValue(value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Const<?>) {
            return ((Const<?>) o).value.equals(value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}