package expression.parser;
import expression.TripleExpression;
import expression.exceptions.ParsingException;
import expression.generic.Operator;

public interface Parser<T> {
    TripleExpression<T> parse(String expression, Operator<T> mode) throws ParsingException;
}
