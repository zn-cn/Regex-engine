package regex_engine.test

import regex_engine.parse.SyntaxError
import regex_engine.regex.Capture
import regex_engine.regex.Regex


object Test {

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            //           Regex r = new Regex("^You are(n't)? af*(dfa)+(dfa|nfa)(ZN|WSS|CXY|LQM)[adfc-g1-8]b{12,56}\\w\n$");
            //            Regex r = new Regex("^I (am){1,2} (tn|zh)(aon)?[a-z][n]\\.\\w\n\\d$");
            //            Regex r = new Regex("f[dfa]*gf");

            val c = Capture("()+\nta")
            println(c.matchAndCapture("fdta\nta")[1])
            //            r.test("fdfagf");
            //            r.test("I am zhan.w\n8");
        } catch (e: SyntaxError) {
            e.printStackTrace()
        }

    }

}
