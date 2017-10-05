package regex_engine.parse.astnode

import java.util.ArrayList

// (|)
class OptionNode(var options: ArrayList<ASTNode>?) : ASTNode {

    override fun toString(): String {
        return "Options" + options!!
    }

}
