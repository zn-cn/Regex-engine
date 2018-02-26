package regex_engine.parse.astnode;
// "\\s"
// 匹配任何空白字符,等价于 [ \f\n\r\t\v]
public class BlankCharNode {
    private ASTNode node;

    public BlankCharNode(ASTNode n){
        node = n;
    }

    @Override
    public String toString(){
        return "\\s";
    }

    public ASTNode getNode(){
        return node;
    }

    public void setNode(ASTNode n) {
        node = n;
    }
}
