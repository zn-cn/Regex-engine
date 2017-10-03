package regex_engine.parse.astnode;

// 字母
public class CharNode implements ASTNode {

	private char c;

	public CharNode(char theChar) {
		c = theChar;
	}

	@Override
	public String toString() {
		return String.valueOf(c);
	}

	public char getChar() {
		return c;
	}

}
