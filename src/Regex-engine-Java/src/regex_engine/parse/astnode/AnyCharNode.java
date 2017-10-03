package regex_engine.parse.astnode;

// "\\w"
// 匹配包括下划线的任何单词字符。等价于'[A-Za-z0-9_]'
public class AnyCharNode implements ASTNode {
    private ASTNode node;

    public AnyCharNode(ASTNode n){
        node = n;
    }

    @Override
    public String toString(){
        return "\\w";
    }

    public ASTNode getNode(){
        return node;
    }

    public void setNode(ASTNode n) {
        node = n;
    }
}
