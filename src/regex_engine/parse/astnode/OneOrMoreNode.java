package regex_engine.parse.astnode;

// +
public class OneOrMoreNode implements ASTNode{
    private ASTNode node;

    public OneOrMoreNode(ASTNode node){
        this.node = node;
    }

    @Override
    public String toString(){
        return "(" + node + ")+";
    }

    public ASTNode getNode(){
        return this.node;
    }

    public void setNode(ASTNode node) {
        this.node = node;
    }
}
