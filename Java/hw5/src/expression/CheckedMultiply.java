package expression;

import expression.exceptions.*;

public class CheckedMultiply<T> extends AbstractOperation<T> {
    public CheckedMultiply(TripleExpression a, TripleExpression b, OperationExecutor<T> c) {
        super(a, b, c);
    }

    @Override
    protected int getPriority() {
        return 2;
    }

    @Override
    protected T makeOperation(T c, T d) {
        return super.operationExecutor.mul(c, d);
    }

    @Override
    public String operationSym() {
        return "*";
    }
}
