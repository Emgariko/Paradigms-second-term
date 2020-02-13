package expression;

import expression.exceptions.ExceptionGenerator;
import expression.exceptions.LogByNonPositiveException;

public class CheckedPow2 implements CommonExpression {
    CommonExpression exp;
    public CheckedPow2(CommonExpression expression) {
        exp = expression;
    }

    @Override
    public int evaluate(int x) {
        int val = exp.evaluate(x);
        ExceptionGenerator.checkOperation(val, 14, "POW2");
        int res = calcPow2(val);
        return res;
    }

    private int calcPow2(int val) {
        if (val == 0) {
            return 1;
        }
        /* int c = val;
        int a = 2;
        int res = 1;
        while (c > 0) {
            if (c % 2 == 0) {
                ExceptionGenerator.checkOperation(a, a, "MUL");
                a *= a;
                c /= 2;
            } else {
                ExceptionGenerator.checkOperation(res, a, "MUL");
                res *= a;
                c--;
            }
        } */
        int res = 1;
        for (int i = 0; i < val; i++) {
            ExceptionGenerator.checkOperation(res, 2, "MUL");
            res *= 2;
        }
        return res;
    }

    @Override
    public double evaluate(double x) {
        return 0;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int val = exp.evaluate(x, y, z);
        ExceptionGenerator.checkOperation(val, 14, "POW2");
        int res = calcPow2(val);
        return res;
    }
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("pow2");
        res.append("(");
        res.append(exp.toString());
        res.append(")");
        return res.toString();
    }
}
