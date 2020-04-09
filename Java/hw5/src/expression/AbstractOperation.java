package expression;

import java.util.Objects;

public abstract class AbstractOperation<T> implements TripleExpression<T> {
    protected OperationExecutor<T> operationExecutor;
    private TripleExpression<T> a, b;
    public AbstractOperation(TripleExpression<T> c, TripleExpression<T> d, OperationExecutor<T> e){
        a = c;
        b = d;
        operationExecutor = e;
    }
    protected abstract int getPriority();
    protected abstract T makeOperation(T c, T d);
    public T evaluate(T x, T y, T z) {
        return makeOperation(a.evaluate(x, y, z), b.evaluate(x,y, z));
    }

    protected abstract String operationSym();

    public String toString() {
        StringBuilder res = new StringBuilder("(");
        res.append(a.toString());
        res.append(" ");
        res.append(operationSym());
        res.append(" ");
        res.append(b.toString());
        res.append(")");
        return res.toString();
    }
    private boolean checkPrior(TripleExpression e) {
        return (e instanceof AbstractOperation && ((AbstractOperation) e).getPriority() < this.getPriority());
    }

    private boolean isImportant(TripleExpression e) { // special cases
        return  (e instanceof AbstractOperation &&
                ((this instanceof CheckedSubtract && ((AbstractOperation) e).getPriority() == 1)
                || this instanceof CheckedDivide)) ||
                (e instanceof CheckedDivide && this.getPriority() == 2);
    }

    @Override
    public boolean equals(Object object) {
        if (object != null && object.getClass() == getClass()) {
            return (((AbstractOperation) object).a.equals(this.a) &&
                    ((AbstractOperation) object).b.equals(this.b) &&
                    ((AbstractOperation) object).operationSym().equals(this.operationSym()));
        } else {
            return false;
        }
    }
    @Override
    public int hashCode() {
        return Objects.hash(a.hashCode(), b.hashCode(), operationSym().hashCode());
    }
}
