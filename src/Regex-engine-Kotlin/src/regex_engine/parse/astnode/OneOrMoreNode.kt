package regex_engine.parse.astnode

// +
class OneOrMoreNode(var node: ASTNode?) : ASTNode {

    override fun toString(): String {
        return "($node)+"
    }
}
