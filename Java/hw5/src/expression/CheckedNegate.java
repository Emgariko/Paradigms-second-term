package expression;

import expression.exceptions.OverflowNegateExcpetpion;

public class CheckedNegate<T> extends UnaryOperation<T> {

    public CheckedNegate(TripleExpression<T> exp, OperationExecutor<T> executor) {
        super(exp, executor);
    }

    /* @Override
    public int evaluate(int x) {
        int val = exp.evaluate(x);
        if (val == Integer.MIN_VALUE) {
            throw new OverflowNegateExcpetpion("-(" + Integer.toString(val) + ")");
        }
        return -val;
    } */

    /* public int evaluate(int x, int y, int z) {
        int val = exp.evaluate(x, y, z);
        if (val == Integer.MIN_VALUE) {
            throw new OverflowNegateExcpetpion("-(" + Integer.toString(val) + ")");
        }
        if (val == Integer.MIN_VALUE) {
            throw new OverflowNegateExcpetpion("-(" + Integer.toString(val) + ")");
        }
        return -val;
    } */

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("-");
        res.append("(");
        res.append(exp.toString());
        res.append(")");
        return res.toString();
    }

    @Override
    public T evaluate(T x, T y, T z) {
        T val = exp.evaluate(x, y, z);
        return operationExecutor.neg(val);
    }
}
