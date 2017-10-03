package regex_engine.parse.astnode;

// .
public class AnyNode implements ASTNode{
    private ASTNode node;

    public AnyNode(ASTNode n){
        node = n;
    }

    @Override
    public String toString(){
        return ".";
    }
    public ASTNode getNode(){
        return node;
    }

}
