package expression.parser;

import expression.*;
import expression.exceptions.*;

import java.util.Map;
import java.util.Set;

public class ExpressionParser<T extends Number> extends BaseParser implements Parser {
    private TokenType curToken;
    private T tokenValue;
    private String tokenName;
    private static int highestLevel = 3; // levelcount + 1
    private final OperationExecutor<T> operationExecutor;

    private static final Map<Integer, Set<TokenType>> ops = Map.of(
            0, Set.of(TokenType.MAX, TokenType.MIN),
            1, Set.of(TokenType.ADD, TokenType.SUB),
            2, Set.of(TokenType.MUL, TokenType.DIV)
    );
    private static final Map<Integer, TokenType> levelType = Map.of(
            0, TokenType.MIN,
            1, TokenType.ADD,
            2, TokenType.MUL,
            3, TokenType.UNKNOWN // for parseElement debug
    );

    public ExpressionParser(OperationExecutor<T> executor) {
        operationExecutor = executor;
    }

    @Override
    public TripleExpression<T> parse(String expression) throws ParsingException {
        this.source = new StringSource(expression);
        nextChar();
        parseToken(true);
        return parseLevel(0, false, false);
    }

    private TripleExpression<T> parseLevel(int level, boolean get, boolean expect) throws ParsingException {
        TripleExpression<T> left = null;
        if (level == highestLevel) {
            return parseElement(get, expect);
        }
        left = parseLevel(level + 1, get, expect);
        while (true) {
            if (ops.get(level).contains(curToken)) {
                TokenType oper = curToken;
                /*left = createOperation((TripleExpression) left,
                        (TripleExpression) parseLevel(level + 1, true, expect), oper);*/
                left = createOperation(left, parseLevel(level + 1, true, expect), oper);
            } else {
                if (levelType.get(level) == TokenType.MUL) {
                    if (curToken == TokenType.NUMBER) {
                        throw new InvalidOrMissedExpressionException("Unexpected number", this.source.getPos());
                        //throw new ParsinggException("Unexpected number", this.source.getPos());
                    }
                }
                if (levelType.get(level) == TokenType.ADD) {
                    if (expect && curToken != TokenType.RB) {
                        throw new MissingBracketException(")", this.source.getPos());
                        //throw new ParsinggException("Expected symbol ')'", this.source.getPos());
                    }
                    if (!expect && curToken == TokenType.RB) {
                        throw new InvalidOrMissedExpressionException("Unexpected symbol ')'", this.source.getPos());
                    }
                    if (curToken == TokenType.LB) {
                        throw new InvalidOrMissedExpressionException("Unexpected symbol '('", this.source.getPos());
                    }
                }
                if (levelType.get(level) == TokenType.MIN) {

                }
                return left;
            }
        }
    }

    private TripleExpression<T> createOperation(TripleExpression<T> left, TripleExpression<T> right, TokenType type) {
        switch (type) {
            case ADD:
                return new CheckedAdd<T>(left, right, operationExecutor);
            case SUB:
                return new CheckedSubtract<T>(left, right, operationExecutor);
            case MUL:
                return new CheckedMultiply<T>(left, right, operationExecutor);
            case DIV:
                return new CheckedDivide<T>(left, right, operationExecutor);
            case MIN:
                return new Min<T>(left, right, operationExecutor);
            case MAX:
                return new Max<T>(left, right, operationExecutor);
            default:
                return null; // for debug
        }
    }

    private TripleExpression<T> parseElement(boolean get, boolean expect) throws ParsingException {
        if (get) {
            parseToken(get);
        }
        TripleExpression<T> res;
        TokenType token = curToken;
        switch (token) {
            case NUMBER:
                res = new Const<T>(tokenValue);
                parseToken(false);
                return res;
            case VAR:
                res = new Variable<T>(tokenName);
                parseToken(false);
                return res;
            case COUNT:
                res = new Count<T>(parseElement(true, expect), operationExecutor);
                return res;
            case SUB:
                res = new CheckedNegate<T>(parseElement(true, expect), operationExecutor);
                return res;
            case LB:
                res = parseLevel(0, true, true);
                if (curToken != TokenType.RB) {
                    throw new MissingBracketException(")", this.source.getPos());
                }
                parseToken(false);
                return res;
            default:
                switch (curToken){
                    case RB:
                        throw new InvalidOrMissedExpressionException("Missing argument", this.source.getPos());
                    case ADD:
                        throw new InvalidOrMissedExpressionException("Missing argument", this.source.getPos());
                    case SUB:
                        throw new InvalidOrMissedExpressionException("Missing argument", this.source.getPos());
                    /*case END:
                        throw new ParsingException("Ex", this.source.getPos());*/

                }
                throw new InvalidOrMissedExpressionException("Missing argument or unexpected symbol", this.source.getPos());
        }
        //return null;
    }

    private void parseToken(boolean get) throws InvalidOrMissedExpressionException {
        skipWhitespace();
        if (test('-')) {
            if (get && between('0', '9')) {
                curToken = TokenType.NUMBER;
                tokenValue = parseValue(true);
            } else {
                curToken = TokenType.SUB;
            }
        } else if (between('0', '9')) {
            curToken = TokenType.NUMBER;
            tokenValue = parseValue(false);
        } else if (between('x', 'z')) {
            tokenName = parseName();
            curToken = TokenType.VAR;
        } else if (test('+')) {
            curToken = TokenType.ADD;
        } else if (test('-')) {
            curToken = TokenType.SUB;
        } else if (test('*')) {
            if (test('*')) {
                curToken = TokenType.POW;
                return;
            }
            curToken = TokenType.MUL;
        } else if (test('/')) {
            if (test('/')) {
                curToken = TokenType.LOG;
                return;
            }
            curToken = TokenType.DIV;
        } else if (test('m')) {
            if (expect("in")) {
                curToken = TokenType.MIN;
            } else if (expect("ax")) {
                curToken = TokenType.MAX;
            } else {
                throw new InvalidOrMissedExpressionException("Expected 'min' or 'max'", this.source.getPos());
            }
        } else if (test('c')) {
            if (expect("ount")) {
                curToken = TokenType.COUNT;
            } else {
                throw new InvalidOrMissedExpressionException("Expected 'count'", this.source.getPos());
            }
        } else if (test('(')) {
            curToken = TokenType.LB;
        } else if (test(')')) {
            curToken = TokenType.RB;
        } else if (ch == '\0') {
            curToken = TokenType.END;
        } else {
            throw new InvalidOrMissedExpressionException("Unknown symbol", this.source.getPos());
        }
    }

    private String parseName() {
        StringBuilder res = new StringBuilder();
        while (between('a', 'z')) {
            res.append(ch);
            nextChar();
        }
        return res.toString();
    }

    private T parseValue(boolean minus) {
        StringBuilder res = new StringBuilder();
        if (minus) {
            res.append('-');
        }
        while (between('0', '9')) {
            res.append(ch);
            nextChar();
        }
        T result;
        try {
            result = operationExecutor.parseConst(res.toString());
        } catch (NumberFormatException e) {
            throw new OverflowPowException(res.toString());
        }
        return result;
    }

    private void skipWhitespace() {
        while (test(' ') || test('\r') || test('\n') || test('\t')) {
            // skip
        }
    }
}
