package expression.generic;
import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;

public class IntegerMode implements Operator<Integer> {
    private final boolean check;

    public IntegerMode(final boolean check) {
        this.check = check;
    }
    @Override
    public Integer add(Integer lhs, Integer rhs) throws OverflowException {
        if (check) {
            final int checker = lhs > 0 ? Integer.MAX_VALUE - lhs : Integer.MIN_VALUE - lhs;
            if ((lhs > 0 && checker < rhs) || (lhs < 0 && checker > rhs)) {
                throw new OverflowException("add", "Integer");
            }
        }
        return lhs + rhs;
    }

    @Override
    public Integer subtract(Integer x, Integer y) throws OverflowException {
        final int res = x - y;
        if (check && ((x > 0 && y < 0 && res - Integer.MAX_VALUE > 0) || (x < 0 && y > 0 && Integer.MIN_VALUE - res > 0))) {
            throw new OverflowException("sub", "Integer");
        }
        return res;
    }

    @Override
    public Integer multiply(Integer x, Integer y) {
        final int res = x * y;
        if (check && x != 0 && y != 0 && (res / y != x || res / x != y)) {
            throw new OverflowException("mul", "Integer");
        }
        return res;
    }

    @Override
    public Integer divide(Integer x, Integer y) {
        if (y == 0) {
            throw new DivisionByZeroException("div", "Integer");
        }
        return x / y;
    }

    @Override
    public Integer abs(Integer x) {
        if (check && x == Integer.MIN_VALUE) {
            throw new OverflowException("abs", "Integer");
        }
        return Math.abs(x);
    }

    @Override
    public Integer negate(Integer x) {
        if (check && x == Integer.MIN_VALUE) {
            throw new OverflowException("negate", "Integer");
        }
        return -x;
    }

    @Override
    public Integer mod(Integer x, Integer y) {
        if (y == 0) {
            throw new DivisionByZeroException("mod", "Integer");
        }
        return x % y;
    }

    @Override
    public Integer parseValue(String x) {
        return Integer.parseInt(x);
    }

    @Override
    public Integer valueOf(Integer value) {
        return value;
    }
}
