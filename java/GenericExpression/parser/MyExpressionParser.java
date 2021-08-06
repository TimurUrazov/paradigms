package expression.parser;

import expression.*;
import expression.exceptions.*;
import expression.exceptions.ParsingException;
import expression.exceptions.UnsupportedOperationException;
import expression.generic.Operator;
import java.util.Map;

public class MyExpressionParser<T> extends BaseParser {
    private final static int floor = 0;
    private final static int maxPriority = 2;
    private final Operator<T> mode;
    TripleExpression<T> expr = null;

    public static final Map<String, Integer> PRIORITIES = Map.of(
            "add", 2, "sub", 2,
            "mul", 1, "div", 1, "mod", 1,
            "square", 0, "abs", 0
    );

    public static final Map<String, String> CHAROPERANDS = Map.of(
            "+", "add", "-", "sub",
            "m","mod", "*", "mul", "/", "div"
    );

    public MyExpressionParser(String expression, Operator<T> mode) {
        super(new StringSource(expression));
        this.mode = mode;
    }

    public TripleExpression<T> parse() throws ParsingException {
        TripleExpression<T> result = parseTerm(maxPriority);
        if (ch != END) {
            if (ch == ')') {
                throw new AbsenceOfLeftBracketException(errorLogging());
            } else {
                throw new ExcessSymbolException("Unexpected symbol:" + ch + ". " + errorLogging());
            }
        }
        return result;
    }

    private TripleExpression<T> parseTerm(int priority) throws ParsingException {
        skipWhitespace();
        if (priority == floor) {
            return parseFloor();
        }
        TripleExpression<T> expr = parseTerm(priority - 1);
        skipWhitespace();
        do {
            final String curOperation = CHAROPERANDS.get(Character.toString(ch));
            if (curOperation != null && priority == PRIORITIES.get(curOperation)) {
                if (ch == 'm') {
                    test('m');
                    test('o');
                    test('d');
                    if (between('0', '9') || between('A', 'z')) {
                        throw new InvalidVariableException(errorLogging());
                    }
                } else if (!between('1', '9')) {
                    nextChar();
                }
                expr = buildOperation(curOperation, expr, parseTerm(priority - 1));
            } else {
                return expr;
            }
            skipWhitespace();
        } while (true);
    }

    private TripleExpression<T> parseNegate() throws ParsingException {
        skipWhitespace();
        if (between('0', '9')) {
            final StringBuilder sb = new StringBuilder();
            sb.append('-');
            return parseConst(sb);
        }
        return new Negate<>(parseFloor(), mode);
    }

    private TripleExpression<T> parseFloor() throws ParsingException {
        skipWhitespace();
        if (test('-')) {
            return parseNegate();
        } else if (between('0', '9')) {
            final StringBuilder sb = new StringBuilder();
            return parseConst(sb);
        } else if (test('(')) {
            expr = parseTerm(maxPriority);
            skipWhitespace();
            if (!expect(')')) {
                throw new AbsenceOfRightBracketException(errorLogging());
            }
            return expr;
        } else {
            return parseVariable();
        }
    }

    private TripleExpression<T> parseAbs() throws ParsingException {
        skipWhitespace();
        return new Abs<>(parseFloor(), mode);
    }

    private TripleExpression<T> parseSquare() throws ParsingException {
        skipWhitespace();
        return new Square<>(parseFloor(), mode);
    }

    private TripleExpression<T> parseVariable() throws InvalidVariableException {
        StringBuilder expr = new StringBuilder();
        while (between('0', '9') || between('A', 'z')) {
            expr.append(ch);
            nextChar();
        }
        final String token = expr.toString();
        if (token.equals("square"))
            return parseSquare();
        if (token.equals("abs"))
            return parseAbs();
        return parseVariable(token);
    }

    private TripleExpression<T> buildOperation(String oper, TripleExpression<T> lhs, TripleExpression<T> rhs) throws UnsupportedOperationException {
        return switch (oper) {
            case "add" -> new Add<>(lhs, rhs, mode);
            case "mod" -> new Mod<>(lhs, rhs, mode);
            case "sub" -> new Subtract<>(lhs, rhs, mode);
            case "div" -> new Divide<>(lhs, rhs, mode);
            case "mul" -> new Multiply<>(lhs, rhs, mode);
            default -> throw new UnsupportedOperationException(oper + ". " + errorLogging());
        };
    }

    private TripleExpression<T> parseVariable(String token) throws InvalidVariableException {
        if (token.equals("x") || token.equals("y") || token.equals("z")) {
            return new Variable<>(token);
        }
        throw new InvalidVariableException("Expected variable, found " + (token.equals("") ? "nothing" : token) + ". " + errorLogging());
    }

    private TripleExpression<T> parseConst(StringBuilder sb) throws IllegalConstException {
        while (between('0', '9')) {
            sb.append(ch);
            nextChar();
        }
        try {
            return new Const<>(sb.toString(), mode);
        } catch (IllegalArgumentException e) {
            throw new IllegalConstException(sb.toString());
        }
    }
}