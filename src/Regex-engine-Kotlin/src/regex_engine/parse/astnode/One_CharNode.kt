package regex_engine.parse.astnode

// []中的 a-z等
class One_CharNode(private var lower_bound: Char, private var upper_bound: Char) : ASTNode {

    override fun toString(): String {
        return (lower_bound.toString() + "-" + upper_bound.toString()).toString()
    }

    fun getLower_bound(): String {
        return this.lower_bound.toString()
    }

    fun getUpper_bound(): String {
        return this.upper_bound.toString()
    }

    fun setUpper_bound(upper_bound: Char) {
        this.upper_bound = upper_bound
    }

    fun setLower_bound(lower_bound: Char) {
        this.lower_bound = lower_bound
    }
}
