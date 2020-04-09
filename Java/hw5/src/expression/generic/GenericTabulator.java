package expression.generic;

import expression.*;
import expression.exceptions.ExpressionException;
import expression.exceptions.ParsingException;
import expression.parser.ExpressionParser;

import java.util.Map;

public class GenericTabulator implements Tabulator {
    private final Map<String, OperationExecutor<? extends Number>> operationExecutorMap = Map.of(
            "i", new IntOperationExecutor(),
            "d", new DoubleOperationExecutor(),
            "bi", new BigIntOperationExecutor(),
            "l", new LongOperationExecutor(false),
            "s", new ShortOperationExecutor(false),
            "u", new IntOperationExecutor(false)
    );
    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        OperationExecutor<? extends Number> operationExecutor = operationExecutorMap.get(mode);
        return calc(operationExecutor, expression, x1, x2, y1, y2, z1, z2);
    }

    public <T extends Number> Object[][][] calc(OperationExecutor<T> operationExecutor, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws ParsingException {
        ExpressionParser<T> parser = new ExpressionParser<>(operationExecutor);
        TripleExpression<T> exp;
        exp = parser.parse(expression);
        Object[][][] res = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                for (int k = z1; k <= z2; k++) {
                    T x = operationExecutor.parseConst(Integer.toString(i));
                    T y = operationExecutor.parseConst(Integer.toString(j));
                    T z = operationExecutor.parseConst(Integer.toString(k));
                    try {
                        res[i - x1][j - y1][k - z1] = exp.evaluate(x, y, z);
                    } catch (ExpressionException e) {
                        res[i - x1][j - y1][k - z1] = null;
                    }
                    // докрутить Exception при  офервлоу и делении на 0
                }
            }
        }
        return res;
    }
}
