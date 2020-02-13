package expression;

import expression.exceptions.*;

public class CheckedSubtract extends AbstractOperation {
    public CheckedSubtract(CommonExpression a, CommonExpression b) {
        super(a, b);
    }

    @Override
    protected int getPriority() {
        return 1;
    }

    public int makeOperation(int c, int d) {
        ExceptionGenerator.checkOperation(c, d, "SUB");
        return c - d;
    }

    @Override
    public String operationSym() {
        return "-";
    }

    @Override
    protected double makeOperation(double c, double d) {
        return c - d;
    }
}
