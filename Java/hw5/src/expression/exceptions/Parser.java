package expression.exceptions;

import expression.TripleExpression;
import expression.exceptions.ExpressionException;
import expression.exceptions.ParsingException;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface Parser{
    TripleExpression parse(String expression) throws /* Change me */ ExpressionException, ParsingException;
}