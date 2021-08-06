package expression.generic;

import expression.exceptions.DivisionByZeroException;

public class ByteMode implements Operator<Byte>{

    @Override
    public Byte add(Byte x, Byte y) {
        return (byte) (x + y);
    }

    @Override
    public Byte subtract(Byte x, Byte y) {
        return (byte) (x - y);
    }

    @Override
    public Byte multiply(Byte x, Byte y) {
        return (byte) (x * y);
    }

    @Override
    public Byte divide(Byte x, Byte y) {
        if (y == 0) {
            throw new DivisionByZeroException("divide", "Byte");
        }
        return (byte) (x / y);
    }

    @Override
    public Byte negate(Byte x) {
        return (byte) -x;
    }

    @Override
    public Byte abs(Byte x) {
        return (byte) Math.abs(x);
    }

    @Override
    public Byte mod(Byte x, Byte y) {
        return (byte) (x % y);
    }

    @Override
    public Byte parseValue(String x) {
        return (byte) Integer.parseInt(x);
    }

    @Override
    public Byte valueOf(Integer value) {
        return value.byteValue();
    }
}
