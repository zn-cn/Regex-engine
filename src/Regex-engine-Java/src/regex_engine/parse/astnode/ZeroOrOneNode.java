package regex_engine.parse.astnode;

// ?
public class ZeroOrOneNode implements ASTNode {

	private ASTNode node;

	public ZeroOrOneNode(ASTNode n) {
		node = n;
	}

	@Override
	public String toString() {
		return "(" + node + ")?";
	}

	public ASTNode getNode() {
		return node;
	}

    public void setNode(ASTNode n){
	    node = n;
    }
}
