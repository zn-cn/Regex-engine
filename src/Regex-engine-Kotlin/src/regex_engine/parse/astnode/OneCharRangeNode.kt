package regex_engine.parse.astnode

import java.util.ArrayList

// []
class OneCharRangeNode(var options: ArrayList<ASTNode>?) : ASTNode {

    override fun toString(): String {
        return "[]" + options!!
    }
}
