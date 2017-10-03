package regex_engine.parse.astnode;

// {}
public class MatchTimesNode {
    private ASTNode node;

    public MatchTimesNode(ASTNode n){
        node = n;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public ASTNode getNode() {
        return node;
    }
}
