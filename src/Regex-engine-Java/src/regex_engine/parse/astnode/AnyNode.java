package regex_engine.parse.astnode;

// .
// 匹配除 "\n" 之外的任何单个字符
public class AnyNode implements ASTNode{
    private ASTNode node;

    public AnyNode(ASTNode node){
        this.node = node;
    }

    @Override
    public String toString(){
        return ".";
    }

    public ASTNode getNode(){
        return this.node;
    }

    public void setNode(ASTNode node) {
        this.node = node;
    }
}
