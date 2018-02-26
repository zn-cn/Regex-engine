package regex_engine.test;

import regex_engine.parse.SyntaxError;
import regex_engine.regex.Capture;
import regex_engine.regex.Regex;


public class Test {

    public static void main(String[] args) {
        try {
//           Regex r = new Regex("^You are(n't)? af*(dfa)+(dfa|nfa)(ZN|WSS|CXY|LQM)[adfc-g1-8]b{12,56}\\w\n$");
//            Regex r = new Regex("^I (am){1,2} (tn|zh)(aon)?[a-z][n]\\.\\w\n\\d$");
//            Regex r = new Regex("f[dfa]*gf");

            Capture c = new Capture("()+\nta");
            System.out.println(c.matchAndCapture("fdta\nta").get(1));
//            r.test("fdfagf");
//            r.test("I am zhan.w\n8");
        } catch (SyntaxError e) {
            e.printStackTrace();
        }
    }

}
