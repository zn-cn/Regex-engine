package regex_engine.parse.astnode

// 字母
class CharNode(var char: Char) : ASTNode {

    override fun toString(): String {
        return char.toString()
    }
}
