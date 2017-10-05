package regex_engine.parse.astnode

// {lower_bound, upper_bound}
class MatchTimesNode : ASTNode {
    private var upper_bound: Int = 0
    private var lower_bound: Int = 0
    var node: ASTNode? = null
        private set   // {}之前的ASTNode

    constructor(lower_bound: String, upper_bound: String, node: ASTNode) {
        this.lower_bound = Integer.parseInt(lower_bound)
        this.upper_bound = Integer.parseInt(upper_bound)
        this.node = node
    }

    constructor(upper_bound: String, node: ASTNode) {
        this.lower_bound = Integer.parseInt(upper_bound)
        this.upper_bound = this.lower_bound
        this.node = node
    }

    constructor(lower_bound: String, up: Int, node: ASTNode) {
        this.lower_bound = Integer.parseInt(lower_bound)
        this.upper_bound = up
        this.node = node
    }

    override fun toString(): String {
        return ("{" + upper_bound.toString() + ", " + lower_bound.toString() + "}").toString()
    }

    val lowwer_bound: String
        get() = this.lower_bound.toString()

    fun getUpper_bound(): String {
        return this.upper_bound.toString()
    }
}
