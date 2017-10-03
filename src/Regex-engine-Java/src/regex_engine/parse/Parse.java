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
    public static ASTNode parse(String input) throws SyntaxError {
        return new Parse(input).parse();
    }

    // 解析
    private ASTNode parse() throws SyntaxError {
        tokens = tokenInit(input);
        if(parseConcat() && stack.size() == 1 && tokens.isEmpty()) {
            return stack.pop();
        } else
            throw new SyntaxError("Invalid syntax");
    }

    // 初始化 tokens
    private Queue<Token> tokenInit(String in) throws SyntaxError {
        Stack<Character> symmetry = new Stack<>();  // 对称
        LinkedList<Token> tokens = new LinkedList<>();
        boolean afterBackslash = false;
        for(int i=0; i < in.length(); i++) {
            char next = in.charAt(i);
            switch(next) {
                case '\\':
                    if(afterBackslash) {
                        tokens.add(new Token(next,TokenType.CHAR,i));
                        afterBackslash = false;
                    } else
                        afterBackslash = true;
                    break;
                case '^':
                    if (afterBackslash){
                        tokens.add(new Token(next, TokenType.CHAR, i));
                        afterBackslash = false;
                    } else {
                        tokens.add(new Token(next, TokenType.SYMBOL, i));
                    }
                case '$':
                    if (afterBackslash){
                        tokens.add(new Token(next, TokenType.CHAR, i));
                        afterBackslash = false;
                    } else {
                        tokens.add(new Token(next, TokenType.SYMBOL, i));
                    }
                case '(':
                    if(afterBackslash) {
                        tokens.add(new Token(next,TokenType.CHAR,i));
                        afterBackslash = false;
                    } else {
                        tokens.add(new Token(next,TokenType.SYMBOL,i));
                        symmetry.push('(');
                    }
                    break;
                case ')':
                    if(afterBackslash) {
                        tokens.add(new Token(next,TokenType.CHAR,i));
                        afterBackslash = false;
                    } else {
                        if(symmetry.peek() == '(') {
                            symmetry.pop();
                            tokens.add(new Token(next,TokenType.SYMBOL,i));
                        } else {
                            throw new SyntaxError("You donn't use ) to close.");
                        }
                    }
                    break;
                case '[':
                    if(afterBackslash) {
                        tokens.add(new Token(next,TokenType.CHAR,i));
                        afterBackslash = false;
                    } else {
                        tokens.add(new Token(next,TokenType.SYMBOL,i));
                        symmetry.push('[');
                    }
                    break;
                case ']':
                    if(afterBackslash) {
                        tokens.add(new Token(next,TokenType.CHAR,i));
                        afterBackslash = false;
                    } else {
                        if(symmetry.peek() == ']') {
                            symmetry.pop();
                            tokens.add(new Token(next,TokenType.SYMBOL,i));
                        } else {
                            throw new SyntaxError("You donn't use ] to close.");
                        }
                    }
                    break;
                case '{':
                    if(afterBackslash) {
                        tokens.add(new Token(next,TokenType.CHAR,i));
                        afterBackslash = false;
                    } else {
                        tokens.add(new Token(next,TokenType.SYMBOL,i));
                        symmetry.push('{');
                    }
                    break;
                case '}':
                    if(afterBackslash) {
                        tokens.add(new Token(next,TokenType.CHAR,i));
                        afterBackslash = false;
                    } else {
                        if(symmetry.peek() == '}') {
                            symmetry.pop();
                            tokens.add(new Token(next,TokenType.SYMBOL,i));
                        } else {
                            throw new SyntaxError("You donn't use } to close.");
                        }
                    }
                    break;
                case '|':
                    if(afterBackslash) {
                        tokens.add(new Token(next,TokenType.CHAR,i));
                        afterBackslash = false;
                    } else {
                        tokens.add(new Token(next,TokenType.SYMBOL,i));
                    }
                    break;
                case '*':
                    if(afterBackslash) {
                        tokens.add(new Token(next,TokenType.CHAR,i));
                        afterBackslash = false;
                    } else {
                        tokens.add(new Token(next,TokenType.SYMBOL,i));
                    }
                    break;
                case '?':
                    if(afterBackslash) {
                        tokens.add(new Token(next,TokenType.CHAR,i));
                        afterBackslash = false;
                    } else {
                        tokens.add(new Token(next,TokenType.SYMBOL,i));
                    }
                    break;
                case 'n':
                    if(afterBackslash) {
                        tokens.add(new Token('\n',TokenType.CHAR,i));
                        afterBackslash = false;
                    } else
                        tokens.add(new Token(next,TokenType.CHAR,i));
                    break;
                case 'r':
                    if(afterBackslash) {
                        tokens.add(new Token('\r',TokenType.CHAR,i));
                        afterBackslash = false;
                    } else
                        tokens.add(new Token(next,TokenType.CHAR,i));
                    break;
                case 't':
                    if(afterBackslash) {
                        tokens.add(new Token('\t',TokenType.CHAR,i));
                        afterBackslash = false;
                    } else
                        tokens.add(new Token(next,TokenType.CHAR,i));
                    break;
                default:
                    if(afterBackslash)
                        throw new SyntaxError("\\" + next + " is not a valid escape sequence.");
                    else
                        tokens.add(new Token(next,TokenType.CHAR,i));
            }
        }
        return tokens;
    }


    private boolean parseConcat() throws SyntaxError {
        ArrayList<ASTNode> segments = new ArrayList<>();
        while(parseZeroOrMore())
            segments.add(stack.pop());
        stack.push(new ConcatNode(segments));
        return true;
    }

    // 解析 *, 进stack
    private boolean parseZeroOrMore() throws SyntaxError {
        if(parseZeroOrOne()) {
            if(getSymbol('*')) {
                if(stack.peek() instanceof ZeroOrOneNode)
                    throw new SyntaxError("can't nest a ? inside of a *");
                else {
                    stack.push(new ZeroOrMoreNode(stack.pop()));
                    return true;
                }
            } else
                return true;
        } else
            return false;
    }

    // 解析 ?, 进stack
    private boolean parseZeroOrOne() throws SyntaxError {
        if(parseSegment()) {
            if(getSymbol('?'))
                stack.push(new ZeroOrOneNode(stack.pop()));
            return true;
        } else
            return false;
    }

    // 推入stack中(..)整体或者字符串
    private boolean parseSegment() throws SyntaxError {
        return parseOption() || parseChar();
    }

    // 解析() 含 |
    private boolean parseOption() throws SyntaxError {
        if(getSymbol('(')) {
            ArrayList<ASTNode> options = new ArrayList<>();
            if(getSymbol(')'))
                throw new SyntaxError("empty options");
            while(parseConcat()) {
                options.add(stack.pop());
                if(!getSymbol('|')) {
                    if(getSymbol(')')) {
                        stack.push(new OptionNode(options));
                        return true;
                    } else
                        throw new SyntaxError("unclosed (");
                }
            }
        } else
            return false;
        return false;
    }

    // 解析字符，同时将字符推入stack，并且从tokens中删除
    private boolean parseChar() {
        Character c = getChar();
        if(c != null) {
            stack.push(new CharNode(c));
            return true;
        } else {
            return false;
        }
    }

    // 得到tokens首字符，并且删除之
    private Character getChar() {
        if(moreCode() && tokens.peek().getType() == TokenType.CHAR) {
            return tokens.poll().getValue();
        } else
            return null;
    }

    // 判断tokens首字符类型是否为SYMBOL，是则删除之
    private boolean getSymbol(char val) {
        if(moreCode() && tokens.peek().getType() == TokenType.SYMBOL
                && tokens.peek().getValue() == val) {
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
