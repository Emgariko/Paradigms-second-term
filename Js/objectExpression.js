"use strict";

function Expr(evaluate, diff, toString, prefix, postfix) {
    this.prototype.evaluate = evaluate;
    this.prototype.diff = diff;
    this.prototype.toString = toString;
    this.prototype.prefix = prefix;
    this.prototype.postfix = postfix;
}
function CreateExpr(object, evaluate, diff, toString, prefix, postfix) {
    object.prototype = Object.create(Expr.prototype);
    Expr.call(object, evaluate, diff, toString, prefix, postfix);
}

function Const(x) {
    this.x = x;
}
CreateExpr(Const,
    function() { return this.x },
    function() { return Const.ZERO },
    function() { return this.x.toString() },
    function () { return this.x.toString() },
    function () { return this.x.toString() }
)

Const.ZERO = new Const(0);
Const.ONE = new Const(1);

const vars = {"x" : 0, "y" : 1, "z" : 2};
function Variable(name) {
    this.name = name;
    this.argIndex = vars[name];
}
CreateExpr(Variable,
    function(...args) { return args[this.argIndex] },
    function(variable) { return this.name === variable ? Const.ONE : Const.ZERO},
    function() { return this.name.toString() },
    function() { return this.name.toString() },
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
    },
    function() {
        return '(' + (this.operationSymbol + ' ' + this.operands.map((x) => x.prefix() + ' ').reduce((x, res) => x + res, '')).slice(0, -1) + ')';
    },
    function() {
        return '(' + (this.operands.length === 0 ? ' ' : this.operands.map((x) => x.postfix() + ' ').reduce((x, res) => x + res, '')) + this.operationSymbol + ')';
    }
)

function CreateOperation(makeOperation, makeDiff, operationSymbol) {
    let operation = function (...args) {
        Operation.call(this, ...args);
    };
    operation.prototype = Object.create(Operation.prototype);
    operation.prototype.makeOperation = makeOperation;
    operation.prototype.makeDiff = makeDiff;
    operation.prototype.operationSymbol = operationSymbol;
    return operation;
}

const Add = CreateOperation(
    function(l, r) { return l + r },
    function(variable, l, r) { return (new Add(l.diff(variable), r.diff(variable)))} ,
    "+"
)

const Subtract = CreateOperation(
    function(l, r) { return l - r },
    function(variable, l, r) { return (new Subtract(l.diff(variable), r.diff(variable))) },
    "-"
)

const Divide = CreateOperation(
    function(l, r) { return l / r },
    function(variable, l, r) { return (new Divide(
        new Subtract(
            new Multiply(l.diff(variable), r), new Multiply(l, r.diff(variable))),
        new Multiply(r, r))) },
    "/"
)

const Multiply = CreateOperation(
    function(l, r) { return l * r },
    function(variable, l, r) { return (new Add(
        new Multiply(l.diff(variable), r), new Multiply(l, r.diff(variable)))) },
    "*"
)

const Negate = CreateOperation(
    function(l) { return -l; },
    function(variable, l) { return (new Negate(l.diff(variable))) },
    "negate"
)

const Power = CreateOperation(
    function (l, r) { return Math.pow(l, r) },
    function(variable, l, r) { return (new Multiply(
        new Power(l, new Subtract(r, new Const(+'1'))),
        new Add(
            new Multiply(r, l.diff(variable)), new Multiply(l, new Multiply(new Log(E, l), r.diff(variable)))))) },
    "pow"
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
    "log"
)


const buildSum = function(...args) {
    if (args.length === 1) {
        return args[0];
    }
    let res = new Add(args[0], args[1]);
    for (let i = 2; i < args.length; i++) {
        res = new Add(res, args[i]);
    }
    //console.log(...args);
    return res;
}

const Sumexp = CreateOperation(
    function(...args) { return args.reduce((acc, cur) => acc + Math.pow(Math.E, cur), 0) },
    function(variable, ...args) {
        return (args.length === 0) ? new Const(+'0') : buildSum(...args.map((element) =>
            (new Multiply(new Power(E, element), element.diff(variable)))));
    },
    "sumexp"
)

const Softmax = CreateOperation(
    function(...args) { return Math.pow(Math.E, args[0]) / Sumexp.prototype.makeOperation(...args)},
    function(variable, ...args) {
        return (args.length === 0 ? new Const(+'0') : (new Divide(new Power(E, args[0]),
                buildSum(...args.map((element) => new Power(E, element))))
        ).diff(variable));
    },
    "softmax"
)


const tokenToOperation = {
    "+" : Add,
    "-" : Subtract,
    "*" : Multiply,
    "/" : Divide,
    "negate" : Negate,
    "pow" : Power,
    "log" : Log,
    "softmax" : Softmax,
    "sumexp" : Sumexp
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

function StringSource(data) {
    this._data = data;
    this._pos = 0;
}
StringSource.prototype.hasNext = function() { return this._pos < this._data.length; }
StringSource.prototype.next = function() { return this.hasNext() ? this._data[this._pos++] : '\0'; }
StringSource.prototype.cur = function() { return this.hasNext() ? this._data[this._pos] : '\0'; }
StringSource.prototype.inc = function() { this._pos++; }
StringSource.prototype.getPos = function() { return this._pos; }
StringSource.prototype.check = function(ch) { return this.cur() === ch; }

function ExpressionError(msg) {
    this.message = msg;
}
ExpressionError.prototype = Object.create(Error.prototype);
ExpressionError.prototype.name = "ExpressionError";
ExpressionError.prototype.constructor = ExpressionError;

function Parser(source) {
    this.source = source;
}
Parser.prototype.isWhiteSpace = function(value) {
    return /\s/.test(value);
}
Parser.prototype.isNumber = function(value) {
    return /^-{0,1}\d+$/.test(value);
}
Parser.prototype.isOperation = function(value) {
    return value in tokenToOperation;
}
Parser.prototype.isWord = function(value) {
    return /^[0-9a-zA-Z]+$/.test(value);
}
Parser.prototype.tokenType = function(token) {
    if (token === '(') {
        return "Lb";
    } else if (token === ')') {
        return "Rb";
    } else if (this.isNumber(token)) {
        return "Const";
    } else if (token in tokenToOperation) {
        return "Operation";
    } else if (token in vars) {
        return "Variable";
    } else if (token === '\0') {
        return "End";
    } else if (token === "") {
        return "Empty";
    } else {
        // try to replace throw out of here
        throw new ExpressionError("Unknown symbol");
        //throw new ExpressionError("Unknown or unexpected symbol");
    }
    /*if (token === '\0') {
        throw new ExpressionError("Unexpected end of source");
    } else {

    }*/
}
Parser.prototype.parseGlobal = "";
/*Parser.prototype.parse = function(mode) {
    return this.prototype.parseGlobal(mode);
}*/
Parser.prototype.parse = function(mode) {
    let token = this.parseToken();
    let res;
    if (this.tokenType(token) === "Lb") {
        res = this.parseExpression(mode);
    } else {
        if (this.tokenType(token) === 'Variable') {
            res = new Variable(token);
        } else if (this.tokenType(token) === 'Const') {
            res = new Const(+token);
        } else if (this.tokenType(token) === "Rb") {
            throw new ExpressionError("Unexpected ) bracket");
        } else if (this.tokenType(token) === "Operation") {
            throw new ExpressionError("Expected Bracket before " + token + " operation");
        } else if (this.tokenType(token) === "End") {
            throw new ExpressionError("Unexpected end of source");
        }
    }
    if (this.source.hasNext()) {
        token = this.parseToken();
        //throw new ExpressionError("Unexpected symbol: " + token[0]);
        throw new ExpressionError("Expected ( bracket");
    }
    return res;
}
Parser.prototype.parseExpression = function(mode) {
    let content = [];
    let token;
    let operationCounter = 0, operationId = -1;
    while (true) {
        token = this.parseToken();
        let type = this.tokenType(token);
        switch (type) {
            case 'Lb':
                content.push(this.parseExpression(mode));
                break;
            case 'Const':
                content.push(new Const(+token));
                break;
            case 'Variable':
                content.push(new Variable(token));
                break;
            case 'Operation':
                content.push(token);
                operationCounter++;
                operationId = content.length - 1;
                break;
            case 'End':
                throw new ExpressionError("Missing ) bracket");
            case 'Rb':
                break;
        }
        if (type === 'Rb') {
            break;
        }
    }
    if (operationCounter > 1) {
        throw new ExpressionError("Missing ( bracket");
        //throw new ExpressionError("Invalid operation format");
    }
    if (mode === "prefix" && operationId != 0) {
        throw new ExpressionError("Invalid operation format");
    }
    if (mode === "postfix" && operationId != content.length - 1) {
        throw new ExpressionError("Invalid operation format");
    }
    let x = tokenToOperation[content[operationId]].prototype.makeOperation.length;
    if (x !== 0 && content.length - 1 !== x) {
        throw new ExpressionError("Too many arguments for operation " + content[operationId]);
    }
    let l = 0, r = content.length - 1;
    if (mode === "prefix") {
        l++;
    } else if (mode === "postfix") {
        r--;
    }
    return new tokenToOperation[content[operationId]](...content.slice(l, r + 1));
}
Parser.prototype.skipWhiteSpaces = function() {
    while (this.isWhiteSpace(this.source.cur())) {
        this.source.next();
    }
}
Parser.prototype.parseToken = function() {
    this.skipWhiteSpaces();
    if (this.source.check('(') || this.source.check(')')) {
        return this.source.next();
    }
    let token = '';
    while (this.source.hasNext() && !this.isWhiteSpace(this.source.cur()) && !this.source.check('(') && !this.source.check(')')) {
        token = token + this.source.cur();
        this.source.next();
    }
    token = (token === '') ? this.source.next() : token;
    return token;
}

function ExpressionParser(source) {
    Parser.call(this, source);
}
ExpressionParser.prototype = Object.create(Parser.prototype);
function parsePrefix(s) {
    let parser = new ExpressionParser(new StringSource(s.trim()));
    let res = parser.parse("prefix");
    return res;
}

function parsePostfix(s) {
    let parser = new ExpressionParser(new StringSource(s.trim()));
    let res = parser.parse("postfix");
    return res;
}