package expression;

import expression.exceptions.DivisionByZeroException;

import java.math.BigInteger;

public class BigIntOperationExecutor implements OperationExecutor<BigInteger> {
    @Override
    public BigInteger add(BigInteger c, BigInteger d) {
        return c.add(d);
    }

    @Override
    public BigInteger mul(BigInteger c, BigInteger d) {
        return c.multiply(d);
    }

    @Override
    public BigInteger sub(BigInteger c, BigInteger d) {
        return c.subtract(d);
    }

    @Override
    public BigInteger div(BigInteger c, BigInteger d) {
        if (d.intValue() == 0) {
            throw new DivisionByZeroException(c.toString() + " / " + d.toString());
        }
        return c.divide(d);
    }

    @Override
    public BigInteger neg(BigInteger c) {
        return c.negate();
    }

    @Override
    public BigInteger parseConst(String s) {
        return new BigInteger(s);
    }

    @Override
    public BigInteger min(BigInteger c, BigInteger d) {
        return c.min(d);
    }

    @Override
    public BigInteger max(BigInteger c, BigInteger d) {
        return c.max(d);
    }

    @Override
    public BigInteger count(BigInteger c) {
        return new BigInteger(Integer.toString(c.bitCount()));
    }
}
