package expression;

import expression.exceptions.*;

public class CheckedMultiply extends AbstractOperation {
    public CheckedMultiply(CommonExpression a, CommonExpression b) {
        super(a, b);
    }

    @Override
    protected int getPriority() {
        return 2;
    }
    @Override
    public int makeOperation(int c, int d) {
        ExceptionGenerator.checkOperation(c, d, "MUL");
        return c * d;
    }

    @Override
    public String operationSym() {
        return "*";
    }

    @Override
    protected double makeOperation(double c, double d) {
        return c * d;
    }

}
