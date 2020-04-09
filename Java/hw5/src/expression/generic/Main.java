package expression.generic;

import expression.Const;
import expression.TripleExpression;

public class Main {
    public static void main(String args[]) {
//        mode=i, x=[-1, 12] y=[-6, 14] z=[-11, 3], expression=10;
        GenericTabulator tabulator = new GenericTabulator();
        try {
            tabulator.tabulate("i", "10", -1, 12, -6, 14, -11, 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
