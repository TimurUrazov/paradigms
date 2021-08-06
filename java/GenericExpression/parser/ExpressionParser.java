package expression.parser;

import expression.*;
import expression.exceptions.ParsingException;
import expression.generic.Operator;

public class ExpressionParser<T> implements Parser<T> {
    @Override
    public TripleExpression<T> parse(String expression, Operator<T> mode) throws ParsingException {
        return new MyExpressionParser<>(expression, mode).parse();
    }
}