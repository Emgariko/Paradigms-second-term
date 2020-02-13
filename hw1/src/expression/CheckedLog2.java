package expression;

import expression.exceptions.ExceptionGenerator;
import expression.exceptions.LogByNonPositiveException;

public class CheckedLog2 implements CommonExpression {
    CommonExpression exp;
    public CheckedLog2(CommonExpression expression) {
        exp = expression;
    }

    @Override
    public int evaluate(int x) {
        int val = exp.evaluate(x);
        if (val <= 0) {
            throw new LogByNonPositiveException("Log2(" + Integer.toString(val) + ")");
        }
        int res = calcLog2(val);
        return res;
    }

    private int calcLog2(int val) {
        int x = val, res = 0;
        /* while (x < val) {
            if (x >= Integer.MAX_VALUE / 2) {
                x = val;
            } else {
                x *= 2;
            }
            res++;
        } */
        while (x > 0) {
            x /= 2;
            res++;
        }
        return res - 1;
    }

    @Override
    public double evaluate(double x) {
        return 0;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int val = exp.evaluate(x);
        int res = calcLog2(val);
        ExceptionGenerator.checkOperation(val, 14, "LOG2");
        return res;
    }
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("log2");
        res.append("(");
        res.append(exp.toString());
        res.append(")");
        return res.toString();
    }
}
