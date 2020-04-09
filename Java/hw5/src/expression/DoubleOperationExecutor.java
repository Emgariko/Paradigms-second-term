package expression;

import expression.exceptions.OverflowAddException;

public class DoubleOperationExecutor implements OperationExecutor<Double> {
    @Override
    public Double add(Double c, Double d) {
        return c + d;
    }

    @Override
    public Double mul(Double c, Double d) {
        return c * d;
    }

    @Override
    public Double sub(Double c, Double d) {
        return c - d;
    }

    @Override
    public Double div(Double c, Double d) {
        return c / d;
    }

    @Override
    public Double neg(Double c) {
        return -c;
    }

    @Override
    public Double parseConst(String s) {
        return Double.parseDouble(s);
    }

    @Override
    public Double min(Double c, Double d) {
        return Math.min(c, d);
    }

    @Override
    public Double max(Double c, Double d) {
        return Math.max(c, d);
    }

    @Override
    public Double count(Double c) {
        return (double) Long.bitCount(Double.doubleToLongBits(c));
    }
}
