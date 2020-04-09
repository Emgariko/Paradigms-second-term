package expression;

import expression.exceptions.*;

public class ShortOperationExecutor implements OperationExecutor<Short> {
    private boolean overflowCheckerMode = true;
    public ShortOperationExecutor(boolean mode) {
        overflowCheckerMode = mode;
    }

    public ShortOperationExecutor() {

    }

    @Override
    public Short add(Short c, Short d) {
        return (short) (c + d);
    }

    @Override
    public Short mul(Short c, Short d) {
        if (overflowCheckerMode && c == Short.MIN_VALUE && d == -1|| c != 0 && d != 0 && (c * d) / d != c) {
            throw new OverflowMultiplyException(Short.toString(c) + " * " + Short.toString(d));
        }
        return (short) (c * d);
    }

    @Override
    public Short sub(Short c, Short d) {
        if (overflowCheckerMode && ((d < 0 && Short.MAX_VALUE + d < c) || (d > 0 && Short.MIN_VALUE + d > c))) {
            throw new OverflowSubstractException(Short.toString(c) + " - " + Short.toString(d));
        }
        return (short) (c - d);
    }

    @Override
    public Short div(Short c, Short d) {
        if (overflowCheckerMode && c == Short.MIN_VALUE && d == -1) {
            throw new OverflowDivideExcpetpion(Short.toString(c) + " / " + Short.toString(d));
        }
        if (d == 0) {
            String msg = new String(Short.toString(c) + " / " + Short.toString(d));
            throw new DivisionByZeroException(msg);
        }
        return (short) (c / d);
    }

    @Override
    public Short neg(Short c) {
        Short val = c;
        if (overflowCheckerMode && val == Short.MIN_VALUE) {
            throw new OverflowNegateExcpetpion("-(" + Short.toString(val) + ")");
        }
        return (short) (-val);
    }

    @Override
    public Short parseConst(String s) {
        return (short)Integer.parseInt(s);
    }

    @Override
    public Short min(Short c, Short d) {
        return (short) Math.min(c, d);
    }

    @Override
    public Short max(Short c, Short d) {
        return (short) Math.max(c, d);
    }

    @Override
    public Short count(Short c) {
        return (short) (Integer.bitCount(c & 0xffff));
    }
}
