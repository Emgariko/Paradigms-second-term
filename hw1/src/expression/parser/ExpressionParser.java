package expression.parser;

import expression.*;
import expression.exceptions.Parser;
import expression.exceptions.ParsingException;

import java.util.Map;
import java.util.Set;

public class ExpressionParser extends BaseParser implements Parser {
    private TokenType curToken;
    private int tokenValue;
    private String tokenName;
    private static int highestLevel = 3; // levelcount + 1
    private static final Map<Integer, Set<TokenType>> ops = Map.of(
            0, Set.of(TokenType.ADD, TokenType.SUB),
            1, Set.of(TokenType.MUL, TokenType.DIV),
            2, Set.of(TokenType.POW, TokenType.LOG)
    );
    private static final Map<Integer, TokenType> levelType = Map.of(
            0, TokenType.ADD,
            1, TokenType.MUL,
            2, TokenType.POW,
            3, TokenType.UNKNOWN // for parseElement debug
    );
    @Override
    public CommonExpression parse(String expression) {
        this.source = new StringSource(expression);
        nextChar();
        parseToken(true);
    return parseLevel(0, false, false);
    }

    private CommonExpression parseLevel(int level, boolean get, boolean expect) {
        CommonExpression left = null;
        if (level == highestLevel) {
            return parseElement(get, expect);
        }
        left = parseLevel(level + 1, get, expect);
        while (true) {
            if (ops.get(level).contains(curToken)) {
                TokenType oper = curToken;
                left = createOperation((CommonExpression) left,
                        (CommonExpression) parseLevel(level + 1, true, expect), oper);
            } else {
                if (levelType.get(level) == TokenType.MUL) {
                    if (curToken == TokenType.NUMBER) {
                        throw new ParsingException("Unexpected number", this.source.getPos());
                    }
                }
                if (levelType.get(level) == TokenType.ADD) {
                    if (expect && curToken != TokenType.RB) {
                        throw new ParsingException("Expected symbol ')'", this.source.getPos());
                    }
                    if (!expect && curToken == TokenType.RB) {
                        throw new ParsingException("Unexpected symbol ')'", this.source.getPos());
                    }
                    if (curToken == TokenType.LB) {
                        throw new ParsingException("Unexpected symbol '('", this.source.getPos());
                    }
                }
                return left;
            }
        }
    }

    private CommonExpression createOperation(CommonExpression left, CommonExpression right, TokenType type) {
        switch (type) {
            case ADD:
                return new CheckedAdd(left, right);
            case SUB:
                return new CheckedSubtract(left, right);
            case MUL:
                return new CheckedMultiply(left, right);
            case DIV:
                return new CheckedDivide(left, right);
            case LOG:
                return new CheckedLog(left, right);
            case POW:
                return new CheckedPow(left, right);
            default:
                return null; // for debug
        }
    }

    private CommonExpression parseElement(boolean get, boolean expect) {
        if (get) {
            parseToken(get);
        }
        CommonExpression res;
        TokenType token = curToken;
        switch (token) {
            case NUMBER:
                res = new Const(tokenValue);
                parseToken(false);
                return res;
            case VAR:
                res = new Variable(tokenName);
                parseToken(false);
                return res;
            case SUB:
                res = new CheckedNegate((CommonExpression) parseElement(true, expect));
                return res;
            case LB:
                res = parseLevel(0, true, true);
                if (curToken != TokenType.RB) {
                    throw new ParsingException("Expected symbol ')'", this.source.getPos());
                }
                parseToken(false);
                return res;
            case LOG2:
                res = new CheckedLog2(parseElement(true, false));
                return res;
            case POW2:
                res = new CheckedPow2(parseElement(true, false));
                return res;
            default:
                throw new ParsingException("Unexpected symbol", this.source.getPos());
        }
    }

    private TokenType parseToken(boolean get) {
        skipWhitespace();
        if (test('-')) {
            if (get && between('0', '9')) {
                curToken = TokenType.NUMBER;
                tokenValue = parseValue(true);
                return curToken;
            } else {
                return curToken = TokenType.SUB;
            }
        } else if (between('0', '9')) {
            curToken = TokenType.NUMBER;
            tokenValue = parseValue(false);
            return curToken;
        } else if (between('x', 'z')) {
            tokenName = parseName();
            return curToken = TokenType.VAR;
        } else if (test('l')) {
            if (expect("og2")) {
                if (test('x') || test('y') || test('z')) {
                    throw new ParsingException("Unexpected variable", this.source.getPos());
                }
                return curToken = TokenType.LOG2;
            } else {
                throw new ParsingException("Expected 'log2'", this.source.getPos());
            }
        } else if (test('p')) {
            if (expect("ow2")) {
                return curToken = TokenType.POW2;
            } else {
                throw new ParsingException("Expected 'pow2'", this.source.getPos());
            }
        } else if (test('+')) {
            return curToken = TokenType.ADD;
        } else if (test('-')) {
            return curToken = TokenType.SUB;
        } else if (test('*')) {
            if (test('*')) {
                return curToken = TokenType.POW;
            }
            return curToken = TokenType.MUL;
        } else if (test('/')) {
            if (test('/')) {
                return curToken = TokenType.LOG;
            }
            return curToken = TokenType.DIV;
        } else if (test('(')) {
            return curToken = TokenType.LB;
        } else if (test(')')) {
            return curToken = TokenType.RB;
        } else if (ch == '\0') {
            return curToken = TokenType.END;
        } else {
            throw new ParsingException("Unknown symbol", this.source.getPos());
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

    private int parseValue(boolean minus) {
        StringBuilder res = new StringBuilder();
        if (minus) {
            res.append('-');
        }
        while (between('0', '9')) {
            res.append(ch);
            nextChar();
        }
        return Integer.valueOf(res.toString());
    }

    private void skipWhitespace() {
        while (test(' ') || test('\r') || test('\n') || test('\t')) {
            // skip
        }
    }
}
