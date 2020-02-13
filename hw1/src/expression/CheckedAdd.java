package expression;

import expression.exceptions.*;

public class CheckedAdd extends AbstractOperation {
    public CheckedAdd(CommonExpression a, CommonExpression b) {
        super(a, b);
    }

    @Override
    protected int getPriority() {
        return 1;
    }
    @Override
    public int makeOperation(int c, int d) {
        ExceptionGenerator.checkOperation(c, d, "ADD");
        return c + d;
    }

    @Override
    public String operationSym() {
        return "+";
    }
    @Override
    protected double makeOperation(double c, double d) {
        return c + d;
    }
}
