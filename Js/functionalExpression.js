"use strict";

let cnst = (x) => () => x;
const e = cnst(Math.E);
const pi = cnst(Math.PI);
const vars = {"x" : 0, "y" : 1, "z" : 2};
let variable = (name) => (...args) => {
    return args[vars[name]];
};

let operation = (func, ...exprs) => (...args) => (func(...exprs.map(val => val(...args))));

let negate = (x) => operation(y => -y, x);

let add = (a, b) => operation((x, y) => x + y, a, b);
let subtract = (a, b) => operation((x, y) => x - y, a, b);
let multiply = (a, b) => operation((x, y) => x * y, a, b);
let divide = (a, b) => operation((x, y) => x / y, a, b);
let avg5 = (a, b, c, d, e) => operation((...args) => args.reduce((x, y) => x + y, 0) / args.length,
    a, b, c, d, e
);
let med3 = (a, b, c) => operation(
    (...args) => (args.sort((x, y) => x - y)[Math.floor(args.length / 2)]),
    a, b, c
);

const tokenToConst = {
    "e" : e,
    "pi": pi
}

const tokenToOperation= {
    "+" : add,
    "-" : subtract,
    "*" : multiply,
    "/" : divide,
    "negate" : negate,
    "med3" : med3,
    "avg5" : avg5
}

function parse(s) {
    let exprs = []
    const parse_token = function(token) {
        if (token in tokenToOperation) {
            token = tokenToOperation[token];
            exprs.splice(-token.length, token.length, token(...exprs.slice(-token.length)));
        } else if (token in vars) {
            exprs.push(variable(token));
        } else if (token in tokenToConst) {
            exprs.push(tokenToConst[token]);
        } else if (token.split("").map((x) => ('0' <= x && x <= '9')).reduce((x, y) => (x & y)), true) { // a simple const
            exprs.push(cnst(1 * token));
        }
    }
    s.trim().split(/\s+/).forEach(parse_token);
    return exprs[0];
}
