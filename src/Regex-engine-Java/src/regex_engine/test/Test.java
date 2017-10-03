package regex_engine.test;

import regex_engine.parse.SyntaxError;
import regex_engine.regex.Regex;

import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) {
        Regex r = null;
        try {
            r = new Regex("You are(n't)? (ZN|WSS|CXY|LQM)");
        } catch (SyntaxError e) {
            e.printStackTrace();
        }
        r.test("You are ZN");
        r.test("You aren't WSS");


        String pattern = "You are(n't)? (ZN|WSS|CXY|LQM)";
        System.out.println(Pattern.matches(pattern, "You are ZN"));
        System.out.println(Pattern.matches(pattern, "You aren't WSS"));

    }

}
