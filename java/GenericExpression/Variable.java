package expression;
import expression.exceptions.InvalidVariableException;

public class Variable<T> implements TripleExpression<T> {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public T evaluate(T x, T y, T z) throws InvalidVariableException {
        return switch (name) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new InvalidVariableException(name);
        };
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Variable) {
            return name.equals(((Variable<?>) o).name);
        }
        return false;
    }

    @Override
    public int hashCode() {
      return name.hashCode();
    }
}