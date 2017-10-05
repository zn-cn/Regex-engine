package regex_engine.test;

import regex_engine.parse.SyntaxError;
import regex_engine.regex.Regex;

import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) {
        Regex r = null;
        try {
            r = new Regex("You are(n't)? af*(dfa)+(dfa|nfa)(ZN|WSS|CXY|LQM)[adfc-g1-8]b{12,56}\\w\n");
        } catch (SyntaxError e) {
            e.printStackTrace();
        }
        r.test("You are dfaZN");
        r.test("You aren't dfaWSS");


        String pattern = "You are(n't)? (ZN|WSS|CXY|LQM)";
        System.out.println(Pattern.matches(pattern, "You are ZN"));
        System.out.println(Pattern.matches(pattern, "You aren't WSS"));

    }

}
