package regex_engine.parse.astnode

class ESCNode(
        // ^ $  \\w  \\s \\d

        var function: Int) : ASTNode {

    override fun toString(): String {
        return if (function == 1)
            "ESC: ^"
        else if (function == 2)
            "ESC: $"
        else if (function == 3)
            "ESC: \\w"
        else if (function == 4)
            "ESC: \\s"
        else if (function == 5)
            "ESC: \\d"
        else if (function == 6)
            "ESC: ."
        else
            super.toString()
    }
}
