package regex_engine.parse.astnode;

// 字母
public class CharNode implements ASTNode {

	private char c;
	private ASTNode node;

	public CharNode(char theChar) {
		c = theChar;
	}

	public CharNode(String st){
		c = st.charAt(0);
	}

	@Override
	public String toString() {
		return String.valueOf(c);
	}

	public char getChar() {
		return c;
	}

	public void setChar(char ch){
		c = ch;
	}
}
