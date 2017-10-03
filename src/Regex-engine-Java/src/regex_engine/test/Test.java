package regex_engine.test;

import regex_engine.parse.SyntaxError;
import regex_engine.regex.Regex;

import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) {
        Regex r = null;
        try {
            r = new Regex("I (don't )?like ((Hormel |Spamco )?spam|bacon|eggs)( for (breakfast|lunch|dinner))?");
        } catch (SyntaxError e) {
            e.printStackTrace();
        }
        r.test("I like Hormel spam");
        r.test("I don't like Spamco spam");
        r.test("I like bacon for dinner");
        r.test("I like eggs");
        r.test("I don't like eggs for lunch");

        String pattern = "I (don't )?like ((Hormel |Spamco )?spam|bacon|eggs)( for (breakfast|lunch|dinner))?";
        System.out.println(Pattern.matches(pattern, "I like Hormel spam"));
        System.out.println(Pattern.matches(pattern, "I don't like Spamco spam"));
        System.out.println(Pattern.matches(pattern, "I like bacon for dinner"));
        System.out.println(Pattern.matches(pattern, "I like eggs"));
        System.out.println(Pattern.matches(pattern, "I don't like eggs for lunch"));
    }

}
