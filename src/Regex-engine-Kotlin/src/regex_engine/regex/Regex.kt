package regex_engine.regex

import regex_engine.parse.Parse
import regex_engine.parse.SyntaxError
import regex_engine.compile.Compile

class Regex @Throws(SyntaxError::class)
constructor(private val regex: String) {
    private val match: StateMatch

    init {
        match = StateMatch(Compile.compile(Parse.parse(regex)))
    }

    override fun toString(): String {
        return "/$regex/"
    }

    // 正则匹配
    private fun matches(input: String): Boolean {
        for (i in 0..input.length - 1) {
            match.accept(input[i])
        }
        val result = match.isOnFinalState
        match.reset()
        return result
    }

    // 测试匹配
    fun test(input: String) {
        println(input + ": " + this.matches(input))
    }

}
