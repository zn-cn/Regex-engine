package regex_engine.parse;


public class Token {

	private String value;
	private TokenType type;
	private int pos;

	public Token(String v, TokenType t, int p) {
		super();
		value = v;
		type = t;
		pos = p;
	}

	public String toString() {
		return "<" + type + ":" + value + ">";
	}

	public String getValue() {
		return value;
	}

	public TokenType getType() {
		return type;
	}

	public int getPos() {
		return pos;
	}

}
