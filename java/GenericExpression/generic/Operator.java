package expression.generic;

public interface Operator<T> {
    T abs(final T x);
    T add(final T x, final T y);
    T subtract(final T x, final T y);
    T multiply(final T x, final T y);
    T divide(final T x, final T y);
    T negate(final T x);
    T parseValue(final String x);
    T valueOf(final Integer value);
    T mod(final T x, final T y);
}
