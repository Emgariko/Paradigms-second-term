package expression;

public abstract class UnaryOperation<T> implements TripleExpression<T> {
    protected TripleExpression<T> exp;
    protected OperationExecutor<T> operationExecutor;
    public UnaryOperation (TripleExpression<T> expression) {
        exp = expression;
    }

    public UnaryOperation(TripleExpression<T> expression, OperationExecutor<T> executor) {
        exp = expression;
        operationExecutor = executor;
    }
}
