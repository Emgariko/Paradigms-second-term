package expression;

import expression.exceptions.ExceptionGenerator;
import expression.exceptions.OverflowAddException;
import expression.exceptions.OverflowNegateExcpetpion;

public class CheckedNegate implements CommonExpression {
    CommonExpression expression;

    public CheckedNegate(CommonExpression exp) {
        expression = exp;
    }

    @Override
    public int evaluate(int x) {
        int val = expression.evaluate(x);
        if (val == Integer.MIN_VALUE) {
            throw new OverflowNegateExcpetpion("-(" + Integer.toString(val) + ")");
        }
        return -val;
    }

    @Override
    public double evaluate(double x) {
        return -expression.evaluate(x);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int val = expression.evaluate(x, y, z);
        ExceptionGenerator.checkOperation(val, 14, "NEG");
        if (val == Integer.MIN_VALUE) {
            throw new OverflowNegateExcpetpion("-(" + Integer.toString(val) + ")");
        }
        return -val;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("-");
        res.append("(");
        res.append(expression.toString());
        res.append(")");
        return res.toString();
    }
}
