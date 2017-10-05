package regex_engine.parse.astnode

import java.util.ArrayList

// ()或者一个区块
class ConcatNode(var segments: ArrayList<ASTNode>?) : ASTNode {

    override fun toString(): String {
        return "Concat" + this.segments!!
    }

}
