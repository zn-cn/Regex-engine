package regex_engine.parse.astnode;

// ^
public class StartPosNode {
    private ASTNode node;

    public StartPosNode(ASTNode n){
        node = n;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public ASTNode getNode() {
        return node;
    }

    public void setNode(ASTNode n){
        node = n;
    }
}
