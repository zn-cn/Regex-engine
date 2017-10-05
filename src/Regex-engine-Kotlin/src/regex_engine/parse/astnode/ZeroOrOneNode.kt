package regex_engine.parse.astnode

// ?
class ZeroOrOneNode(var node: ASTNode?) : ASTNode {

    override fun toString(): String {
        return "($node)?"
    }
}
