package expression;

public class Variable<T> implements TripleExpression<T> {
    private String name;
    public Variable (String x) {
        this.name = x;
    }
    @Override
    public String toString() {
        return name;
    }
    @Override
    public boolean equals(Object object) {
        if (object != null && object.getClass() == getClass())
            return this.name.equals(((Variable) object).name);
        return false;
    }
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public T evaluate(T x, T y, T z) {
        T ans = null;
        if (name.equals("x")) {
            ans = x;
        } else if (name.equals("y")) {
            ans = y;
        } else if (name.equals("z")) {
            ans = z;
        }
        return ans;
    }
}
