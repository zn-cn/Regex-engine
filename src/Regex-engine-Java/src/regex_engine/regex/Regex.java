package regex_engine.regex;

import regex_engine.parse.Parse;
import regex_engine.parse.SyntaxError;
import regex_engine.compile.Compile;

public class Regex {

	private String regex;
	private StateMatch match;

	public Regex(String r) throws SyntaxError {
		regex = r;
		match = new StateMatch(Compile.compile(Parse.parse(regex)));
	}

	@Override
	public String toString() {
		return "/" + regex + "/";
	}

	// 正则匹配
	private boolean matches(String input) {
		for(int i=0; i < input.length(); i++) {
			match.accept(input.charAt(i));
		}
		boolean result = match.isOnFinalState();
		match.reset();
		return result;
	}

	// 测试匹配
	public void test(String input) {
		System.out.println(input + ": " + this.matches(input));
	}

}
