package expression;

public class CheckedAdd<T> extends AbstractOperation<T> {
    public CheckedAdd(TripleExpression a, TripleExpression b, OperationExecutor<T> executor) {
        super(a, b, executor);
    }


    @Override
    protected int getPriority() {
        return 1;
    }

    @Override
    protected T makeOperation(T c, T d) {
        return super.operationExecutor.add(c, d);
    }

    @Override
    public String operationSym() {
        return "+";
    }
}
