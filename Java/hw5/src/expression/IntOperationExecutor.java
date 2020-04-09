package expression;

import expression.exceptions.*;

public class IntOperationExecutor implements OperationExecutor<Integer> {
    private boolean overflowCheckerMode = true;
    public IntOperationExecutor(boolean mode) {
        overflowCheckerMode = mode;
    }
    public IntOperationExecutor() {}
    @Override
    public Integer add(Integer c, Integer d) {
        if (overflowCheckerMode && ((d > 0 && Integer.MAX_VALUE - d < c) || (d < 0 && Integer.MIN_VALUE - d > c))) {
            throw new OverflowAddException(Integer.toString(c) + " + " + Integer.toString(d));
        }
        return c + d;
    }

    @Override
    public Integer mul(Integer c, Integer d) {
        if (overflowCheckerMode && (c == Integer.MIN_VALUE && d == -1|| c != 0 && d != 0 && (c * d) / d != c)) {
            throw new OverflowMultiplyException(Integer.toString(c) + " * " + Integer.toString(d));
        }
        return c * d;
    }

    @Override
    public Integer sub(Integer c, Integer d) {
        if (overflowCheckerMode && ((d < 0 && Integer.MAX_VALUE + d < c) || (d > 0 && Integer.MIN_VALUE + d > c))) {
            throw new OverflowSubstractException(Integer.toString(c) + " - " + Integer.toString(d));
        }
        return c - d;
    }

    @Override
    public Integer div(Integer c, Integer d) {
        if (overflowCheckerMode && c == Integer.MIN_VALUE && d == -1) {
            throw new OverflowDivideExcpetpion(Integer.toString(c) + " / " + Integer.toString(d));
        }
        if (d == 0) {
            String msg = new String(Integer.toString(c) + " / " + Integer.toString(d));
            throw new DivisionByZeroException(msg);
        }
        return c / d;
    }

    @Override
    public Integer neg(Integer c) {
        int val = c;
        if (overflowCheckerMode && val == Integer.MIN_VALUE) {
            throw new OverflowNegateExcpetpion("-(" + Integer.toString(val) + ")");
        }
        return -val;
    }

    @Override
    public Integer parseConst(String s) {
        return Integer.parseInt(s);
    }

    @Override
    public Integer min(Integer c, Integer d) {
        return Math.min(c, d);
    }

    @Override
    public Integer max(Integer c, Integer d) {
        return Math.max(c, d);
    }

    @Override
    public Integer count(Integer c) {
        return Integer.bitCount(c);
    }
}
