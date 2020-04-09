package expression;

import expression.exceptions.*;

public class LongOperationExecutor implements OperationExecutor<Long> {
    private boolean overflowCheckerMode = true;
    public LongOperationExecutor(boolean mode) {
        overflowCheckerMode = mode;
    }
    public LongOperationExecutor() {}
    @Override
    public Long add(Long c, Long d) {
        return c + d;
    }

    @Override
    public Long mul(Long c, Long d) {
        if (overflowCheckerMode && (c == Long.MIN_VALUE && d == -1|| c != 0 && d != 0 && (c * d) / d != c)) {
            throw new OverflowMultiplyException(Long.toString(c) + " * " + Long.toString(d));
        }
        return c * d;
    }

    @Override
    public Long sub(Long c, Long d) {
        if ((d < 0 && Long.MAX_VALUE + d < c) || (d > 0 && Long.MIN_VALUE + d > c)) {
            throw new OverflowSubstractException(Long.toString(c) + " - " + Long.toString(d));
        }
        return c - d;
    }

    @Override
    public Long div(Long c, Long d) {
        if (overflowCheckerMode && c == Long.MIN_VALUE && d == -1) {
            throw new OverflowDivideExcpetpion(Long.toString(c) + " / " + Long.toString(d));
        }
        if (d == 0) {
            String msg = new String(Long.toString(c) + " / " + Long.toString(d));
            throw new DivisionByZeroException(msg);
        }
        return c / d;
    }

    @Override
    public Long neg(Long c) {
        Long val = c;
        if (overflowCheckerMode && val == Long.MIN_VALUE) {
            throw new OverflowNegateExcpetpion("-(" + Long.toString(val) + ")");
        }
        return -val;
    }

    @Override
    public Long parseConst(String s) {
        return Long.parseLong(s);
    }

    @Override
    public Long min(Long c, Long d) {
        return Math.min(c, d);
    }

    @Override
    public Long max(Long c, Long d) {
        return Math.max(c, d);
    }

    @Override
    public Long count(Long c) {
        return (long) Long.bitCount(c);
    }
}
