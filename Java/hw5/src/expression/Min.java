package expression;

public class Min<T> extends AbstractOperation<T> {
    public Min(TripleExpression<T> c, TripleExpression<T> d, OperationExecutor<T> e) {
        super(c, d, e);
    }

    @Override
    protected int getPriority() {
        return 2;
    }

    @Override
    protected T makeOperation(T c, T d) {
        return operationExecutor.min(c, d);
    }

    @Override
    protected String operationSym() {
        return "min";
    }
}
