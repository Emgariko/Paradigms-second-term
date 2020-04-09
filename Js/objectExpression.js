"use strict";

function Expr(_evaluate, _diff, _toString) {
    this.prototype.evaluate = _evaluate;
    this.prototype.diff = _diff;
    this.prototype.toString = _toString;
}
function CreateExpr(object, _evaluate, _diff, _toString) {
    object.prototype = Object.create(Expr.prototype);
    Expr.call(object, _evaluate, _diff, _toString);
}

function Const(x) {
    this.x = x;
}
CreateExpr(Const,
    function() { return this.x },
    function() { return new Const(0) },
    function() { return this.x.toString() }
)

const vars = {"x" : 0, "y" : 1, "z" : 2};
function Variable(name) {
    this.name = name;
    this.argIndex = vars[name];
}
CreateExpr(Variable,
    function(...args) { return args[this.argIndex] },
    function(variable) { return new Const(this.name === variable ? 1 : 0) },
    function() { return this.name.toString() }
)


function Operation(...args) {
    this.operands = args;
}
CreateExpr(Operation,
    function (...args) {
        return this.makeOperation(...this.operands.map((element) => element.evaluate(...args)))
    },
    function (variable) {
        return this.makeDiff(variable, ...this.operands)
    },
    function () {
        return this.operands.map((x) => x.toString() + ' ').reduce((x, res) => x + res, '') + this.operationSymbol
    }
)

function CreateOperation(_makeOperation, _makeDiff, _operationSymbol) {
    let operation = function (...args) {
        Operation.call(this, ...args);
    };
    operation.prototype = Object.create(Operation.prototype);
    operation.prototype.makeOperation = _makeOperation;
    operation.prototype.makeDiff = _makeDiff;
    operation.prototype.operationSymbol = _operationSymbol
    return operation
}

const Add = CreateOperation(
    function(l, r) { return l + r },
    function(variable, l, r) { return (new Add(l.diff(variable), r.diff(variable)))} ,
    "+",
)

const Subtract = CreateOperation(
    function(l, r) { return l - r },
    function(variable, l, r) { return (new Subtract(l.diff(variable), r.diff(variable))) },
    "-",
)

const Divide = CreateOperation(
    function(l, r) { return l / r },
    function(variable, l, r) { return (new Divide(
        new Subtract(
            new Multiply(l.diff(variable), r), new Multiply(l, r.diff(variable))),
        new Multiply(r, r))) },
    "/",
)

const Multiply = CreateOperation(
    function(l, r) { return l * r },
    function(variable, l, r) { return (new Add(
        new Multiply(l.diff(variable), r), new Multiply(l, r.diff(variable)))) },
    "*",
)

const Negate = CreateOperation(
    function(l) { return -l; },
    function(variable, l) { return (new Negate(l.diff(variable))) },
    "negate",
)

const Power = CreateOperation(
    function (l, r) { return Math.pow(l, r) },
    function(variable, l, r) { return (new Multiply(
        new Power(l, new Subtract(r, new Const(1))),
        new Add(
            new Multiply(r, l.diff(variable)), new Multiply(l, new Multiply(new Log(E, l), r.diff(variable)))))) },
    "pow",
)

const E = new Const(Math.E);

const Log = CreateOperation(
    function(l, r) { return Math.log(Math.abs(r)) / Math.log(Math.abs(l)) },
    function(variable, l, r) { return (new Divide(
        new Subtract(
            new Divide(new Multiply(new Log(E, l), r.diff(variable)), r),
            new Divide(new Multiply(l.diff(variable), new Log(E, r)), l)
        ), new Multiply(new Log(E, l), new Log(E, l))
    )) },
    "log",
)


const tokenToOperation = {
    "+" : Add,
    "-" : Subtract,
    "*" : Multiply,
    "/" : Divide,
    "negate" : Negate,
    "pow" : Power,
    "log" : Log
};

function parse(s) {
    let exprs = []
    const parse_token = function(token) {
        if (token in tokenToOperation) {
            token = tokenToOperation[token];
            let l = token.prototype.makeOperation.length;
            exprs.splice(-l, l, new token(...exprs.slice(-l)));
        } else if (token in vars) {
            exprs.push(new Variable(token));
        } else if (token.split("").map((x) => ('0' <= x && x <= '9')).reduce((x, y) => (x & y)), true) { // a simple const
            exprs.push(new Const(1 * token));
        }
    }
    s.trim().split(/\s+/).forEach(parse_token);
    return exprs[0];
}
