package regex_engine.test

import regex_engine.parse.SyntaxError
import regex_engine.regex.Regex

import java.util.regex.Pattern

object Test {

    @JvmStatic
    fun main(args: Array<String>) {
        var r: Regex? = null
        try {
            r = Regex("You are(n't)? af*(dfa)+(dfa|nfa)(ZN|WSS|CXY|LQM)[adfc-g1-8]b{12,56}\\w\n")
        } catch (e: SyntaxError) {
            e.printStackTrace()
        }

        r!!.test("You are dfaZN")
        r.test("You aren't dfaWSS")


        val pattern = "You are(n't)? (ZN|WSS|CXY|LQM)"
        println(Pattern.matches(pattern, "You are ZN"))
        println(Pattern.matches(pattern, "You aren't WSS"))

    }

}
