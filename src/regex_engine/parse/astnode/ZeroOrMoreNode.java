package regex_engine.parse.astnode;

//  *
public class ZeroOrMoreNode implements ASTNode {

	private ASTNode node;

	public ZeroOrMoreNode(ASTNode node) {
		this.node = node;
	}

	@Override
	public String toString() {
		return "(" + node + ")*";
	}

	public ASTNode getNode() {
		return node;
	}

	public void setNode(ASTNode node) {
		this.node = node;
	}
}
