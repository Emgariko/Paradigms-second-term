package expression;

public class Const<T> implements TripleExpression<T> {
    private T val;
    public Const(T i) {
        val = i;
    }
    @Override
    public boolean equals(Object object) {
        if (object != null && object.getClass() == getClass())
            return this.val == ((Const) object).val;
        return false;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return val;
    }
}
