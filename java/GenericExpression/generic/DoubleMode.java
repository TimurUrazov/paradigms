package expression.generic;

public class DoubleMode implements Operator<Double> {
    @Override
    public Double add(Double x, Double y) {
        return x + y;
    }

    @Override
    public Double subtract(Double x, Double y) {
        return x - y;
    }

    @Override
    public Double multiply(Double x, Double y) {
        return x * y;
    }

    @Override
    public Double divide(Double x, Double y) {
        return x / y;
    }

    @Override
    public Double negate(Double x) {
        return -x;
    }

    @Override
    public Double abs(Double x) {
        return Math.abs(x);
    }

    @Override
    public Double mod(Double x, Double y) {
        return x % y;
    }

    @Override
    public Double parseValue(String value) {
        return Double.parseDouble(value);
    }

    @Override
    public Double valueOf(Integer value) {
        return value.doubleValue();
    }
}
