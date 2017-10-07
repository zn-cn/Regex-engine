package regex_engine.test;

import regex_engine.parse.SyntaxError;
import regex_engine.regex.Capture;
import regex_engine.regex.Regex;


public class Test {

    public static void main(String[] args) {
        try {
//           Regex r = new Regex("^You are(n't)? af*(dfa)+(dfa|nfa)(ZN|WSS|CXY|LQM)[adfc-g1-8]b{12,56}\\w\n$");
            Regex r = new Regex("^I am{2,} (tn|zh)(aon)?[a-z][n]\\.\\w\n\\d$");
//            Capture c = new Capture("^I am (tn|zh|cd)(aon)[a-z][n]\\.\\w\n\\d$");
//            System.out.println(c.matchAndCapture("I am zhaonan.w\n8").get(1));
//            r.test("I am zhaonan\n_\n9");
            r.test("I amm zhan.w\n8");
        } catch (SyntaxError e) {
            e.printStackTrace();
        }
    }

}
