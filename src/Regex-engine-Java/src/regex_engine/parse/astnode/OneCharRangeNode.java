package regex_engine.parse.astnode;

// []
public class OneCharRangeNode {
    private ASTNode node;

    public OneCharRangeNode(ASTNode n){
        node = n;
    }

    @Override
    public String toString(){
        return "[" + node + "]";
    }

    public ASTNode getNode() {
        return node;
    }

    public void setNode(ASTNode n) {
        node = n;
    }
}
