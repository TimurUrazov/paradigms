package expression.generic;
import expression.TripleExpression;
import expression.exceptions.ComputingException;
import expression.exceptions.ParsingException;
import expression.exceptions.UnsupportedModeException;
import expression.parser.ExpressionParser;

public class GenericTabulator implements Tabulator {
    private enum MODES {
        CHECKEDINTEGER("i", new IntegerMode(true)),
        DOUBLE("d", new DoubleMode()),
        BIGINTEGER("bi", new BigIntegerMode()),
        INTEGER("u", new IntegerMode(false)),
        BYTE("b", new ByteMode()),
        MODULE("p", new ModuleMode());

        private final String symbol;
        private final Operator<?> mode;

        MODES (String symbol, Operator<?> mode) {
            this.symbol = symbol;
            this.mode = mode;
        }
        public String getSymbol() {
            return symbol;
        }

        public Operator<?> getMode() {
            return mode;
        }

        public static Operator<?> getMode(String symbol) throws UnsupportedModeException {
            for (MODES mode : MODES.values()) {
                if (mode.getSymbol().equals(symbol)) {
                    return mode.getMode();
                }
            }
            throw new UnsupportedModeException(symbol);
        }
    }

    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws UnsupportedModeException {
        return makeTable(MODES.getMode(mode), expression, x1, x2, y1, y2, z1, z2);
    }

    private <T> Object[][][] makeTable(Operator<T> mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) {
        final TripleExpression<T> expr = new ExpressionParser<T>().parse(expression, mode);
        final Object[][][] table = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int i = x1; i < x2 + 1; i++) {
            for (int j = y1; j < y2 + 1; j++) {
                for (int k = z1; k < z2 + 1; k++) {
                    try {
                        table[i - x1][j - y1][k - z1] = expr.evaluate(mode.valueOf(i), mode.valueOf(j), mode.valueOf(k));
                    } catch (ParsingException | ComputingException e) {
                        table[i - x1][j - y1][k - z1] = null;
                    }
                }
            }
        }
        return table;
    }
}
