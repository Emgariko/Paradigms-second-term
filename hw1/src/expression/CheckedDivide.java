package expression;

import expression.exceptions.*;

public class CheckedDivide extends AbstractOperation {
    public CheckedDivide(CommonExpression a, CommonExpression b) {
        super(a, b);
    }

    @Override
    protected int getPriority() {
        return 2;
    }

    public int makeOperation(int c, int d) {
        ExceptionGenerator.checkOperation(c, d, "DIV");
        return c / d;
    }

    @Override
    public String operationSym() {
        return "/";
    }
    @Override
    protected double makeOperation(double c, double d) {
        return c / d;
    }
}
