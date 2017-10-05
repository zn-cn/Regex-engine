package regex_engine.parse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import regex_engine.parse.astnode.*;

public class Parse {


    private String input;
    private Queue<Token> tokens;
    private Stack<ASTNode> stack;

    private Parse(String in) {
        input = in;
        tokens = null;
        stack = new Stack<>();
    }

    // 外部引用
    public static ArrayList<ASTNode> parse(String input) throws SyntaxError {
        return new Parse(input).parse();
    }

    // 解析
    private ArrayList<ASTNode> parse() throws SyntaxError {
        tokens = tokenInit(input);
        if (parseConcat() && tokens.isEmpty()) {
            ArrayList<ASTNode> temp = new ArrayList<>(stack);
            stack.clear();
            return temp;
        } else
            throw new SyntaxError("Invalid syntax");
    }

    // 初始化 tokens
    private Queue<Token> tokenInit(String in) throws SyntaxError {
        Stack<Character> symmetry = new Stack<>();  // 对称
        LinkedList<Token> tokens = new LinkedList<>();
        boolean afterBackslash = false;
        for (int i = 0; i < in.length(); i++) {
            String next = String.valueOf(in.charAt(i));
            switch (next) {
                case "\\":
                    if (afterBackslash) {
                        tokens.add(new Token(next, TokenType.CHAR, i));
                        afterBackslash = false;
                    } else
                        afterBackslash = true;
                    break;
                case "(":
                    if (afterBackslash) {
                        tokens.add(new Token(next, TokenType.CHAR, i));
                        afterBackslash = false;
                    } else {
                        tokens.add(new Token(next, TokenType.SYMBOL, i));
                        symmetry.push('(');
                    }
                    break;
                case ")":
                    if (afterBackslash) {
                        tokens.add(new Token(next, TokenType.CHAR, i));
                        afterBackslash = false;
                    } else {
                        if (symmetry.peek() == '(') {
                            symmetry.pop();
                            tokens.add(new Token(next, TokenType.SYMBOL, i));
                        } else {
                            throw new SyntaxError("You donn't use ) to close.");
                        }
                    }
                    break;
                case "[":
                    if (afterBackslash) {
                        tokens.add(new Token(next, TokenType.CHAR, i));
                        afterBackslash = false;
                    } else {
                        tokens.add(new Token(next, TokenType.SYMBOL, i));
                        symmetry.push('[');
                    }
                    break;
                case "]":
                    if (afterBackslash) {
                        tokens.add(new Token(next, TokenType.CHAR, i));
                        afterBackslash = false;
                    } else {
                        if (symmetry.peek() == '[') {
                            symmetry.pop();
                            tokens.add(new Token(next, TokenType.SYMBOL, i));
                        } else {
                            throw new SyntaxError("You donn't use ] to close.");
                        }
                    }
                    break;
                case "-":
                    if (symmetry.peek() == '[') {
                        tokens.add(new Token(next, TokenType.SYMBOL, i));
                    } else {
                        tokens.add(new Token(next, TokenType.CHAR, i));
                    }
                    break;
                case "{":
                    if (afterBackslash) {
                        tokens.add(new Token(next, TokenType.CHAR, i));
                        afterBackslash = false;
                    } else {
                        tokens.add(new Token(next, TokenType.SYMBOL, i));
                        symmetry.push('{');
                    }
                    break;
                case "}":
                    if (afterBackslash) {
                        tokens.add(new Token(next, TokenType.CHAR, i));
                        afterBackslash = false;
                    } else {
                        if (symmetry.peek() == '{') {
                            symmetry.pop();
                            tokens.add(new Token(next, TokenType.SYMBOL, i));
                        } else {
                            throw new SyntaxError("You donn't use } to close.");
                        }
                    }
                    break;
                case "|":
                    if (afterBackslash) {
                        tokens.add(new Token(next, TokenType.CHAR, i));
                        afterBackslash = false;
                    } else {
                        tokens.add(new Token(next, TokenType.SYMBOL, i));
                    }
                    break;
                case "*":
                    if (afterBackslash) {
                        tokens.add(new Token(next, TokenType.CHAR, i));
                        afterBackslash = false;
                    } else {
                        tokens.add(new Token(next, TokenType.SYMBOL, i));
                    }
                    break;
                case "+":
                    if (afterBackslash) {
                        tokens.add(new Token(next, TokenType.CHAR, i));
                        afterBackslash = false;
                    } else {
                        tokens.add(new Token(next, TokenType.SYMBOL, i));
                    }
                    break;
                case ".":
                    if (afterBackslash) {
                        tokens.add(new Token(next, TokenType.CHAR, i));
                        afterBackslash = false;
                    } else {
                        tokens.add(new Token(next, TokenType.SYMBOL, i));
                    }
                    break;
                case "?":
                    if (afterBackslash) {
                        tokens.add(new Token(next, TokenType.CHAR, i));
                        afterBackslash = false;
                    } else {
                        tokens.add(new Token(next, TokenType.SYMBOL, i));
                    }
                    break;
                case "^":
                    // 暂时不开发 [^a-z]类,此处^仅表示匹配开头位置
                    if (afterBackslash) {
                        tokens.add(new Token(next, TokenType.CHAR, i));
                        afterBackslash = false;
                    } else {
                        tokens.add(new Token(next, TokenType.SYMBOL, i));
                    }
                    break;
                case "$":
                    if (afterBackslash) {
                        tokens.add(new Token(next, TokenType.CHAR, i));
                        afterBackslash = false;
                    } else {
                        tokens.add(new Token(next, TokenType.SYMBOL, i));
                    }
                    break;
                case "w":
                    if (afterBackslash) {
                        tokens.add(new Token("\\w", TokenType.SYMBOL, i));
                        afterBackslash = false;
                    } else
                        tokens.add(new Token(next, TokenType.CHAR, i));
                    break;
                case "s":
                    if (afterBackslash) {
                        tokens.add(new Token("\\s", TokenType.SYMBOL, i));
                        afterBackslash = false;
                    } else
                        tokens.add(new Token(next, TokenType.CHAR, i));
                    break;
                case "d":
                    if (afterBackslash) {
                        tokens.add(new Token("\\d", TokenType.SYMBOL, i));
                        afterBackslash = false;
                    } else
                        tokens.add(new Token(next, TokenType.CHAR, i));
                    break;
                default:
                    if (afterBackslash)
                        throw new SyntaxError("\\" + next + " is not a valid escape sequence.");
                    else
                        tokens.add(new Token(next, TokenType.CHAR, i));
            }
        }
        return tokens;
    }


    private boolean parseConcat() throws SyntaxError {
        while (parseZeroOrMore() || parseOneOrMore() || parseZeroOrOne() || parseSegment()) {
            boolean a = false;
        }
        return true;
    }

    // 解析 *, 进stack
    private boolean parseZeroOrMore() throws SyntaxError {

        if (getSymbol("*")) {
            if (stack.peek() instanceof ZeroOrOneNode)
                throw new SyntaxError("can't nest a ? inside of a *");
            else {
                stack.push(new ZeroOrMoreNode(stack.pop()));
                return true;
            }
        } else
            return false;

    }

    // 解析 +, 进stack
    private boolean parseOneOrMore() throws SyntaxError {

        if (getSymbol("+")) {
            if (stack.peek() instanceof ZeroOrOneNode)
                throw new SyntaxError("can't nest a ? inside of a +");
            else {
                stack.push(new OneOrMoreNode(stack.pop()));
                return true;
            }
        } else
            return false;

    }

    // 解析 ?, 进stack
    private boolean parseZeroOrOne() throws SyntaxError {

        if (getSymbol("?")) {
            stack.push(new ZeroOrOneNode(stack.pop()));
            return true;
        } else
            return false;

    }

    // 推入stack中(..)整体或者字符串
    private boolean parseSegment() throws SyntaxError {
        return parseOption() || parseChar() || parseESC();
    }

    // 解析(|), [], {}
    private boolean parseOption() throws SyntaxError {
        if (getSymbol("(")) {
            ArrayList<ASTNode> options = new ArrayList<>();
            if (getSymbol(")"))
                throw new SyntaxError("empty options");
            stack.push(new ConcatNode(new ArrayList<>()));
            while (parseString(options)) {
                boolean a = false;
            }
            return true;
        } else if (getSymbol("[")) {
            if (getSymbol("]"))
                throw new SyntaxError("empty options");
            ArrayList<ASTNode> options = new ArrayList<>();
            while (parseOne_Char(options)) {
                boolean a = false;
            }
            stack.push(new OneCharRangeNode(options));
            return true;

        } else if (getSymbol("{")) {
            if (getSymbol("}"))
                throw new SyntaxError("empty options");
            parseMatchTimes();
            return true;
        } else
            return false;
    }

    // 暂时只有（|）里面不能有问号等特殊字符
    private boolean parseString(ArrayList<ASTNode> options) throws SyntaxError {
        if (getSymbol(")")) {
            ((ConcatNode) stack.peek()).getSegments().add(new OptionNode(options));
            return false;
        } else if (getSymbol("|")) {
            OptionNode temp = new OptionNode(new ArrayList<>(options));
            ((ConcatNode) stack.peek()).getSegments().add(temp);
            options.clear();
            return true;
        } else {
            String ch = getChar();
            if (ch != null) {
                options.add(new CharNode(ch.charAt(0)));
                return true;
            } else {
                throw new SyntaxError("Invalid syntax");
            }
        }
    }

    // []里面的a-z 1-9等
    private boolean parseOne_Char(ArrayList<ASTNode> options) throws SyntaxError {
        if (getSymbol("]")) {
            return false;
        } else {
            if (getSymbol("-")) {
                String ch = getChar();
                if (ch != null) {
                    CharNode lower_bound = (CharNode) options.remove(options.size() - 1);
                    char lower_char = lower_bound.getChar();
                    char upper_char = ch.charAt(0);
                    if (((byte) lower_char < (byte) upper_char) &&
                            ((byte) lower_char > 96 && ((byte) upper_char < 123) ||
                                    ((byte) lower_char > 64 && (byte) upper_char < 91) ||
                                    ((byte) lower_char > 47 && (byte) upper_char < 58))) {
                        options.add(new One_CharNode(lower_char, upper_char));
                        return true;
                    } else {
                        throw new SyntaxError("Invalid syntax");
                    }
                } else {
                    return false;
                }
            } else {
                String c = getChar();
                if (c != null) {
                    options.add(new CharNode(c.charAt(0)));
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    // {}里面的n, m
    private void parseMatchTimes() throws SyntaxError {
        String times = "";
        boolean comma = false;
        while (!getSymbol("}")) {
            String c = getChar();
            if (c != null) {
                if (c.equals(","))
                    comma = true;
                times = times + c;
            }
        }
        if (comma) {
            String[] temp = times.split(",");
            if (temp.length == 2)
                stack.push(new MatchTimesNode(temp[0], temp[1], stack.pop()));
            else if (temp.length == 1)
                stack.push(new MatchTimesNode(temp[0], Integer.MAX_VALUE, stack.pop()));
            else throw new SyntaxError("Invalid syntax");
        } else {
            stack.push(new MatchTimesNode(times, stack.pop()));
        }

    }

    // 解析字符，同时将字符推入stack，并且从tokens中删除
    private boolean parseChar() {
        String c = getChar();
        if (c != null) {
            stack.push(new CharNode(c.charAt(0)));
            return true;
        } else {
            return false;
        }
    }

    // 解析转义字符
    private boolean parseESC() throws SyntaxError {
        if (getSymbol("^")) {
            stack.push(new ESCNode(1));
        } else if (getSymbol("$")) {
            stack.push(new ESCNode(2));
        } else if (getSymbol("\\w")) {
            stack.push(new ESCNode(3));
        } else if (getSymbol("\\s")) {
            stack.push(new ESCNode(4));
        } else if (getSymbol("\\d")) {
            stack.push(new ESCNode(5));
        } else if (getSymbol(".")) {
            stack.push(new ESCNode(6));
        } else
            return isSepecialChar();
        return true;
    }

    // 得到tokens首字符，并且删除之
    private String getChar() {
        if (moreCode() && tokens.peek().getType() == TokenType.CHAR) {
            return tokens.poll().getValue();
        } else
            return null;
    }

    // 判断是否为 * + ？
    private boolean isSepecialChar() {
        return (moreCode() && tokens.peek().getType() == TokenType.SYMBOL && (tokens.peek().getValue().equals("?")
                || tokens.peek().getValue().equals("*") || tokens.peek().getValue().equals("+")));
    }

    // 判断tokens首字符类型是否为SYMBOL，是则删除之
    private boolean getSymbol(String val) {
        if (moreCode() && tokens.peek().getType() == TokenType.SYMBOL
                && tokens.peek().getValue().equals(val)) {
            tokens.poll();
            return true;
        } else
            return false;
    }


    // 判断tokens是否为空，否则返回 true
    private boolean moreCode() {
        return !tokens.isEmpty();
    }

}
