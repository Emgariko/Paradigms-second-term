package expression.exceptions;

import expression.parser.ExpressionParser;

public class ParsingException extends ExpressionException {
    public ParsingException(String msg, int pos) {
        super("Parsing error at " + Integer.toString(pos) + ", " + msg + ".");
    }
}
