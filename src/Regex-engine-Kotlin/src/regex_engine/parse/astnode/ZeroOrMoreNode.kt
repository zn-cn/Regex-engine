package regex_engine.parse.astnode

//  *
class ZeroOrMoreNode(var node: ASTNode?) : ASTNode {

    override fun toString(): String {
        return "($node)*"
    }
}
