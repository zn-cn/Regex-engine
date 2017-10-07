package regex_engine.parse

import java.util.ArrayList
import java.util.LinkedList
import java.util.Queue
import java.util.Stack

import regex_engine.parse.astnode.*

class Parse private constructor(private val input: String) {
    private var tokens: Queue<Token>? = null
    private val stack: Stack<ASTNode>

    init {
        tokens = null
        stack = Stack()
    }

    // 解析
    @Throws(SyntaxError::class)
    private fun parse(): ArrayList<ASTNode> {
        tokens = tokenInit(input)
        if (parseConcat() && tokens!!.isEmpty()) {
            val temp = ArrayList(stack)
            stack.clear()
            return temp
        } else
            throw SyntaxError("Invalid syntax")
    }

    // 初始化 tokens
    @Throws(SyntaxError::class)
    private fun tokenInit(`in`: String): Queue<Token> {
        val symmetry = Stack<Char>()  // 对称
        val tokens = LinkedList<Token>()
        var afterBackslash = false
        for (i in 0..`in`.length - 1) {
            val next = `in`[i].toString()
            when (next) {
                "\\" -> if (afterBackslash) {
                    tokens.add(Token(next, TokenType.CHAR, i))
                    afterBackslash = false
                } else
                    afterBackslash = true
                "(" -> if (afterBackslash) {
                    tokens.add(Token(next, TokenType.CHAR, i))
                    afterBackslash = false
                } else {
                    tokens.add(Token(next, TokenType.SYMBOL, i))
                    symmetry.push('(')
                }
                ")" -> if (afterBackslash) {
                    tokens.add(Token(next, TokenType.CHAR, i))
                    afterBackslash = false
                } else {
                    if (symmetry.peek() === '(') {
                        symmetry.pop()
                        tokens.add(Token(next, TokenType.SYMBOL, i))
                    } else {
                        throw SyntaxError("You donn't use ) to close.")
                    }
                }
                "[" -> if (afterBackslash) {
                    tokens.add(Token(next, TokenType.CHAR, i))
                    afterBackslash = false
                } else {
                    tokens.add(Token(next, TokenType.SYMBOL, i))
                    symmetry.push('[')
                }
                "]" -> if (afterBackslash) {
                    tokens.add(Token(next, TokenType.CHAR, i))
                    afterBackslash = false
                } else {
                    if (symmetry.peek() === '[') {
                        symmetry.pop()
                        tokens.add(Token(next, TokenType.SYMBOL, i))
                    } else {
                        throw SyntaxError("You donn't use ] to close.")
                    }
                }
                "-" -> if (symmetry.peek() === '[') {
                    tokens.add(Token(next, TokenType.SYMBOL, i))
                } else {
                    tokens.add(Token(next, TokenType.CHAR, i))
                }
                "{" -> if (afterBackslash) {
                    tokens.add(Token(next, TokenType.CHAR, i))
                    afterBackslash = false
                } else {
                    tokens.add(Token(next, TokenType.SYMBOL, i))
                    symmetry.push('{')
                }
                "}" -> if (afterBackslash) {
                    tokens.add(Token(next, TokenType.CHAR, i))
                    afterBackslash = false
                } else {
                    if (symmetry.peek() === '{') {
                        symmetry.pop()
                        tokens.add(Token(next, TokenType.SYMBOL, i))
                    } else {
                        throw SyntaxError("You donn't use } to close.")
                    }
                }
                "|" -> if (afterBackslash) {
                    tokens.add(Token(next, TokenType.CHAR, i))
                    afterBackslash = false
                } else {
                    tokens.add(Token(next, TokenType.SYMBOL, i))
                }
                "*" -> if (afterBackslash) {
                    tokens.add(Token(next, TokenType.CHAR, i))
                    afterBackslash = false
                } else {
                    tokens.add(Token(next, TokenType.SYMBOL, i))
                }
                "+" -> if (afterBackslash) {
                    tokens.add(Token(next, TokenType.CHAR, i))
                    afterBackslash = false
                } else {
                    tokens.add(Token(next, TokenType.SYMBOL, i))
                }
                "." -> if (afterBackslash) {
                    tokens.add(Token(next, TokenType.CHAR, i))
                    afterBackslash = false
                } else {
                    tokens.add(Token(next, TokenType.SYMBOL, i))
                }
                "?" -> if (afterBackslash) {
                    tokens.add(Token(next, TokenType.CHAR, i))
                    afterBackslash = false
                } else {
                    tokens.add(Token(next, TokenType.SYMBOL, i))
                }
                "^" ->
                    // 暂时不开发 [^a-z]类,此处^仅表示匹配开头位置
                    if (afterBackslash) {
                        tokens.add(Token(next, TokenType.CHAR, i))
                        afterBackslash = false
                    } else {
                        tokens.add(Token(next, TokenType.SYMBOL, i))
                    }
                "$" -> if (afterBackslash) {
                    tokens.add(Token(next, TokenType.CHAR, i))
                    afterBackslash = false
                } else {
                    tokens.add(Token(next, TokenType.SYMBOL, i))
                }
                "w" -> if (afterBackslash) {
                    tokens.add(Token("\\w", TokenType.SYMBOL, i))
                    afterBackslash = false
                } else
                    tokens.add(Token(next, TokenType.CHAR, i))
                "s" -> if (afterBackslash) {
                    tokens.add(Token("\\s", TokenType.SYMBOL, i))
                    afterBackslash = false
                } else
                    tokens.add(Token(next, TokenType.CHAR, i))
                "d" -> if (afterBackslash) {
                    tokens.add(Token("\\d", TokenType.SYMBOL, i))
                    afterBackslash = false
                } else
                    tokens.add(Token(next, TokenType.CHAR, i))
                else -> if (afterBackslash)
                    throw SyntaxError("\\" + next + " is not a valid escape sequence.")
                else
                    tokens.add(Token(next, TokenType.CHAR, i))
            }
        }
        return tokens
    }

    @Throws(SyntaxError::class)
    private fun parseConcat(): Boolean {
        while (parseZeroOrMore() || parseOneOrMore() || parseZeroOrOne() || parseSegment()) {
            val a = false
        }
        return true
    }

    // 解析 *, 进stack
    @Throws(SyntaxError::class)
    private fun parseZeroOrMore(): Boolean {

        if (getSymbol("*")) {
            if (stack.peek() is ZeroOrOneNode)
                throw SyntaxError("can't nest a ? inside of a *")
            else {
                stack.push(ZeroOrMoreNode(stack.pop()))
                return true
            }
        } else
            return false

    }

    // 解析 +, 进stack
    @Throws(SyntaxError::class)
    private fun parseOneOrMore(): Boolean {

        if (getSymbol("+")) {
            if (stack.peek() is ZeroOrOneNode)
                throw SyntaxError("can't nest a ? inside of a +")
            else {
                stack.push(OneOrMoreNode(stack.pop()))
                return true
            }
        } else
            return false

    }

    // 解析 ?, 进stack
    @Throws(SyntaxError::class)
    private fun parseZeroOrOne(): Boolean {

        if (getSymbol("?")) {
            stack.push(ZeroOrOneNode(stack.pop()))
            return true
        } else
            return false

    }

    // 推入stack中(..)整体或者字符串
    @Throws(SyntaxError::class)
    private fun parseSegment(): Boolean {
        return parseOption() || parseChar() || parseESC()
    }

    // 解析(|), [], {}
    @Throws(SyntaxError::class)
    private fun parseOption(): Boolean {
        if (getSymbol("(")) {
            val options = ArrayList<ASTNode>()
            if (getSymbol(")"))
                throw SyntaxError("empty options")
            stack.push(ConcatNode(ArrayList()))
            while (parseString(options)) {
                val a = false
            }
            return true
        } else if (getSymbol("[")) {
            if (getSymbol("]"))
                throw SyntaxError("empty options")
            val options = ArrayList<ASTNode>()
            while (parseOne_Char(options)) {
                val a = false
            }
            stack.push(OneCharRangeNode(options))
            return true

        } else if (getSymbol("{")) {
            if (getSymbol("}"))
                throw SyntaxError("empty options")
            parseMatchTimes()
            return true
        } else
            return false
    }

    // 暂时只有（|）里面不能有问号等特殊字符
    @Throws(SyntaxError::class)
    private fun parseString(options: ArrayList<ASTNode>): Boolean {
        if (getSymbol(")")) {
            (stack.peek() as ConcatNode).segments!!.add(OptionNode(options))
            return false
        } else if (getSymbol("|")) {
            val temp = OptionNode(ArrayList(options))
            (stack.peek() as ConcatNode).segments!!.add(temp)
            options.clear()
            return true
        } else {
            val ch = char
            if (ch != null) {
                options.add(CharNode(ch[0]))
                return true
            } else {
                throw SyntaxError("Invalid syntax")
            }
        }
    }

    // []里面的a-z 1-9等
    @Throws(SyntaxError::class)
    private fun parseOne_Char(options: ArrayList<ASTNode>): Boolean {
        if (getSymbol("]")) {
            return false
        } else {
            if (getSymbol("-")) {
                val ch = char
                if (ch != null) {
                    val lower_bound = options.removeAt(options.size - 1) as CharNode
                    val lower_char = lower_bound.char
                    val upper_char = ch[0]
                    if (lower_char.toByte() <= upper_char.toByte() && (lower_char.toByte() > 96 && upper_char.toByte() < 123 ||
                            lower_char.toByte() > 64 && upper_char.toByte() < 91 ||
                            lower_char.toByte() > 47 && upper_char.toByte() < 58)) {
                        options.add(One_CharNode(lower_char, upper_char))
                        return true
                    } else {
                        throw SyntaxError("Invalid syntax")
                    }
                } else {
                    return false
                }
            } else {
                val c = char
                if (c != null) {
                    options.add(CharNode(c[0]))
                    return true
                } else {
                    return false
                }
            }
        }
    }

    // {}里面的n, m
    @Throws(SyntaxError::class)
    private fun parseMatchTimes() {
        var times = ""
        var comma = false
        while (!getSymbol("}")) {
            val c = char
            if (c != null) {
                if (c == ",")
                    comma = true
                times = times + c
            }
        }
        if (comma) {
            val temp = times.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            if (temp.size == 2)
                stack.push(MatchTimesNode(temp[0], temp[1], stack.pop()))
            else if (temp.size == 1)
                stack.push(MatchTimesNode(temp[0], Integer.MAX_VALUE, stack.pop()))
            else
                throw SyntaxError("Invalid syntax")
        } else {
            stack.push(MatchTimesNode(times, stack.pop()))
        }

    }

    // 解析字符，同时将字符推入stack，并且从tokens中删除
    private fun parseChar(): Boolean {
        val c = char
        if (c != null) {
            stack.push(CharNode(c[0]))
            return true
        } else {
            return false
        }
    }

    // 解析转义字符
    @Throws(SyntaxError::class)
    private fun parseESC(): Boolean {
        if (getSymbol("^")) {
            stack.push(ESCNode(1))
        } else if (getSymbol("$")) {
            stack.push(ESCNode(2))
        } else if (getSymbol("\\w")) {
            stack.push(ESCNode(3))
        } else if (getSymbol("\\s")) {
            stack.push(ESCNode(4))
        } else if (getSymbol("\\d")) {
            stack.push(ESCNode(5))
        } else if (getSymbol(".")) {
            stack.push(ESCNode(6))
        } else
            return isSepecialChar
        return true
    }

    // 得到tokens首字符，并且删除之
    private val char: String?
        get() = if (moreCode() && tokens!!.peek().type == TokenType.CHAR) {
            tokens!!.poll().value
        } else
            null

    // 判断是否为 * + ？
    private val isSepecialChar: Boolean
        get() = moreCode() && tokens!!.peek().type == TokenType.SYMBOL && (tokens!!.peek().value == "?"
                || tokens!!.peek().value == "*" || tokens!!.peek().value == "+")

    // 判断tokens首字符类型是否为SYMBOL，是则删除之
    private fun getSymbol(`val`: String): Boolean {
        if (moreCode() && tokens!!.peek().type == TokenType.SYMBOL
                && tokens!!.peek().value == `val`) {
            tokens!!.poll()
            return true
        } else
            return false
    }

    // 判断tokens是否为空，否则返回 true
    private fun moreCode(): Boolean {
        return !tokens!!.isEmpty()
    }

    companion object {

        // 外部引用
        @Throws(SyntaxError::class)
        fun parse(input: String): ArrayList<ASTNode> {
            return Parse(input).parse()
        }
    }

}
