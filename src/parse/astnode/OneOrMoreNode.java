package regex_engine.parse.astnode;

public class OneOrMoreNode {
    private ASTNode node;

    public OneOrMoreNode(ASTNode n){
        node = n;
    }

    @Override
    public String toString(){
        return "(" + node + ")+";
    }

    public ASTNode getNode(){
        return node;
    }
}
