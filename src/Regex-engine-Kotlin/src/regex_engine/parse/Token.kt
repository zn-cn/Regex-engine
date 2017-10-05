package regex_engine.parse


class Token(val value: String, val type: TokenType, val pos: Int) {

    override fun toString(): String {
        return "<$type:$value>"
    }

}
