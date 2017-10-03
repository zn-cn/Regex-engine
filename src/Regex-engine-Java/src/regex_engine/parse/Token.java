package regex_engine.parse;


public class Token {

	private char value;
	private TokenType type;
	private int pos;

	public Token(char v, TokenType t, int p) {
		super();
		value = v;
		type = t;
		pos = p;
	}

	public String toString() {
		return "<" + type + ":" + value + ">";
	}

	public char getValue() {
		return value;
	}

	public TokenType getType() {
		return type;
	}

	public int getPos() {
		return pos;
	}

}
