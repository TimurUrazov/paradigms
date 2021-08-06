package expression.generic;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.UnsupportedOperationException;

import java.math.BigInteger;

public class BigIntegerMode implements Operator<BigInteger> {
    @Override
    public BigInteger add(BigInteger x, BigInteger y) {
        return x.add(y);
    }

    @Override
    public BigInteger subtract(BigInteger x, BigInteger y) {
        return x.subtract(y);
    }

    @Override
    public BigInteger divide(BigInteger x, BigInteger y) {
        if (y.equals(BigInteger.ZERO)) {
            throw new DivisionByZeroException("divide", "BigInteger");
        }
        return x.divide(y);
    }

    @Override
    public BigInteger abs(BigInteger x) {
        return x.abs();
    }

    @Override
    public BigInteger mod(BigInteger x, BigInteger y) {
        if (y.intValue() <= 0) {
            throw new UnsupportedOperationException(x.toString() + " mod " + y.toString());
        }
        return x.mod(y);
    }

    @Override
    public BigInteger multiply(BigInteger x, BigInteger y) {
        return x.multiply(y);
    }

    @Override
    public BigInteger negate(BigInteger x) {
        return x.negate();
    }

    @Override
    public BigInteger parseValue(String x) {
        return new BigInteger(x);
    }

    @Override
    public BigInteger valueOf(Integer value) {
        return BigInteger.valueOf(value);
    }
}
