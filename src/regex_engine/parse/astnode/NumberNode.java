package regex_engine.parse.astnode;

// "\\d"
// 匹配一个数字字符
public class NumberNode implements ASTNode {
    private ASTNode node;

    public NumberNode(ASTNode n){
        node = n;
    }

    @Override
    public String toString(){
        return "\\d";
    }

    public ASTNode getNode(){
        return node;
    }

    public void setNode(ASTNode n) {
        node = n;
    }
}
