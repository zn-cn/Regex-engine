package regex_engine.test;

import regex_engine.parse.SyntaxError;
import regex_engine.regex.Regex;

import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) {
        Regex r = null;
        try {
            r = new Regex("I am( not)? ((zhao |wang |cao )?nan|shuaishuai|xiaoyu)");
        } catch (SyntaxError e) {
            e.printStackTrace();
        }
        r.test("I am zhao nan");
        r.test("I am not zhao nan");
        r.test("I am cao ");
        r.test("I am cao xiaoyu");
        r.test("I am not wang shuaishuai");

        String pattern = "I am( not)? ((zhao |wang |cao )?nan|shuaishuai|xiaoyu)";
        System.out.println(Pattern.matches(pattern, "I am zhao nan"));
        System.out.println(Pattern.matches(pattern, "I am not zhao nan"));
        System.out.println(Pattern.matches(pattern, "I am cao "));
        System.out.println(Pattern.matches(pattern, "I am cao xiaoyu"));
        System.out.println(Pattern.matches(pattern, "I am not wang shuaishuai"));
    }

}
