package expression;

import expression.exceptions.*;

public class CheckedSubtract<T> extends AbstractOperation<T> {
    public CheckedSubtract(TripleExpression a, TripleExpression b, OperationExecutor<T> c) {
        super(a, b, c);
    }

    @Override
    protected int getPriority() {
        return 1;
    }

    @Override
    protected T makeOperation(T c, T d) {
        return super.operationExecutor.sub(c, d);
    }

    /* public int makeOperation(int c, int d) {
        int res = c - d;
        if ((d < 0 && Integer.MAX_VALUE + d < c) || (d > 0 && Integer.MIN_VALUE + d > c)) {
            throw new OverflowSubstractException(Integer.toString(c) + " - " + Integer.toString(d));
        }
        return c - d;
    } */

    @Override
    public String operationSym() {
        return "-";
    }

}
