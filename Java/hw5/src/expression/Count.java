package expression;

public class Count<T> extends UnaryOperation<T> {

    public Count(TripleExpression<T> expression) {
        super(expression);
    }

    public Count(TripleExpression<T> parseElement, OperationExecutor<T> operationExecutor) {
        super(parseElement, operationExecutor);
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return operationExecutor.count(exp.evaluate(x, y, z));
    }
}
