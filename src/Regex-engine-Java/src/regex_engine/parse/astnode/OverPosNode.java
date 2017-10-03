package regex_engine.parse.astnode;

// $
public class OverPosNode {
    private ASTNode node;

    public OverPosNode(ASTNode n){
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
