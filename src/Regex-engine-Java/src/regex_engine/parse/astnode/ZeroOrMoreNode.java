package regex_engine.parse.astnode;

//  *
public class ZeroOrMoreNode implements ASTNode {

	private ASTNode node;

	public ZeroOrMoreNode(ASTNode n) {
		node = n;
	}

	@Override
	public String toString() {
		return "(" + node + ")*";
	}

	public ASTNode getNode() {
		return node;
	}

	public void setNode(ASTNode n){
		node = n;
	}
}
