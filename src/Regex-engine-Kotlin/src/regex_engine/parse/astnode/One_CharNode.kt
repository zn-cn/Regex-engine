package regex_engine.parse.astnode

// []中的 a-z等
class One_CharNode(var lower_bound: Char, var upper_bound: Char) : ASTNode {

    override fun toString(): String {
        return (lower_bound.toString() + "-" + upper_bound.toString()).toString()
    }
}
