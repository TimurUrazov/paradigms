package expression;
import expression.exceptions.OverflowException;
import expression.generic.Operator;

public class Abs<T> extends AbstractUnaryOperation<T> {
    public Abs(TripleExpression<T> expression, Operator<T> mode) {
        super(expression, "abs", mode);
    }

    @Override
    protected T compute(T number) throws OverflowException {
        return mode.abs(number);
    }
}
