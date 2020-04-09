package expression;

import expression.exceptions.*;

public class CheckedDivide<T> extends AbstractOperation<T> {
    public CheckedDivide(TripleExpression a, TripleExpression b, OperationExecutor c) {
        super(a, b, c);
    }

    @Override
    protected int getPriority() {
        return 2;
    }

    @Override
    protected T makeOperation(T c, T d) {
        return super.operationExecutor.div(c, d);
    }

    @Override
    public String operationSym() {
        return "/";
    }
}
