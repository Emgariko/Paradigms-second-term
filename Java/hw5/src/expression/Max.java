package expression;

public class Max<T> extends AbstractOperation<T> {
    public Max(TripleExpression<T> c, TripleExpression<T> d, OperationExecutor<T> e) {
        super(c, d, e);
    }

    @Override
    protected int getPriority() {
        return 2;
    }

    @Override
    protected T makeOperation(T c, T d) {
        return operationExecutor.max(c, d);
    }

    @Override
    protected String operationSym() {
        return "max";
    }
}
