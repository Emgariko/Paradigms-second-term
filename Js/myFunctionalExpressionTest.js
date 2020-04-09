include("functionalExpression.js");
correct = true;
for (i = 0; i < 11; i++) {
    value = i * i - 2 * i + 1;
    let exp = add(
        subtract(
            multiply(variable("x"), variable("x")),
            multiply(cnst(2), variable("x"))
        ),
        cnst(1)
    );
    res = exp(i);
    if (res != value) {
        println("Wrong value, expected:", value, " actual: ", res);
        correct = false;
        break;
    }
}
if (correct === true) {
    println("OK");
}