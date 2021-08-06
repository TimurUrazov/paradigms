package expression.generic;
import expression.exceptions.DivisionByZeroException;

public class ModuleMode implements Operator<Integer> {
    private final Integer MOD = 1009;
    @Override
    public Integer add(Integer x, Integer y) {
        return ((x + y) % MOD + MOD) % MOD;
    }

    @Override
    public Integer subtract(Integer x, Integer y) {
        return ((x - y) % MOD + MOD) % MOD;
    }

    @Override
    public Integer multiply(Integer x, Integer y) {
        return (((x % MOD) * (y % MOD)) % MOD + MOD) % MOD;
    }

    private Integer pow(Integer a) {
        int c = 1;
        int b = MOD - 2;
        while(b != 0) {
            if (b % 2 == 1) {
                c = multiply(c, a);
            }
            b /= 2;
            a = multiply(a, a);
        }
        return c % MOD;
    }

    @Override
    public Integer divide(Integer x, Integer y) {
        if (y == 0) {
            throw new DivisionByZeroException("divide", "Module");
        }
        return multiply(x, pow(y));
    }

    @Override
    public Integer negate(Integer x) {
        return (((-x) % MOD) + MOD) % MOD;
    }

    @Override
    public Integer abs(Integer x) {
        return x >= 0 ? x % MOD : x % MOD + MOD;
    }

    @Override
    public Integer mod(Integer x, Integer y) {
        if (y == 0) {
            throw new DivisionByZeroException("mod", "Module");
        }
        return x % (MOD < y ? MOD : y);
    }

    @Override
    public Integer parseValue(String x) {
        return (Integer.parseInt(x) % MOD + MOD) % MOD;
    }

    @Override
    public Integer valueOf(Integer x) {
        return (x % MOD + MOD) % MOD;
    }
}
