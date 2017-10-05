package regex_engine.compile

import java.util.ArrayList

import com.sun.org.apache.bcel.internal.generic.BREAKPOINT
import regex_engine.parse.SyntaxError
import regex_engine.regex.StateChart
import regex_engine.parse.astnode.*

class Compile private constructor(private val astnode: ArrayList<ASTNode>) {
    private val chart: StateChart

    init {
        chart = StateChart()
    }

    @Throws(SyntaxError::class)
    private fun compile(): StateChart {
        var finalstate = 0
        for (node in astnode) {
            finalstate = compile(node, finalstate)
        }
        chart.addFinalState(finalstate)
        return chart
    }

    // 编译
    @Throws(SyntaxError::class)
    private fun compile(tree: ASTNode?, startId: Int): Int {
        return if (tree is ConcatNode)
        // ConcatNode ()
            compileConcat(tree as ConcatNode?, startId)
        else if (tree is ZeroOrOneNode)
        // ZeroOrOneNode  ?
            compileZeroOrOne(tree as ZeroOrOneNode?, startId)
        else if (tree is ZeroOrMoreNode)
        // ZeroOrMoreNode  *
            compileZeroOrMore(tree as ZeroOrMoreNode?, startId)
        else if (tree is OneOrMoreNode)
        // OneOrMoreNode  +
            compileOneOrMore(tree as OneOrMoreNode?, startId)
        else if (tree is OptionNode)
        // OptionNode  |
            compileOption(tree as OptionNode?, startId)
        else if (tree is OneCharRangeNode)
        // OneCharRangeNode  []
            compileOneCharRange(tree as OneCharRangeNode?, startId)
        else if (tree is One_CharNode)
        // One_CharNode []里面的 如: a-z 1-8
            compileOne_Char(tree as One_CharNode?, startId)
        else if (tree is MatchTimesNode)
        // MatchTimesNode {}
            compileMatchTimes(tree as MatchTimesNode?, startId)
        else if (tree is CharNode)
        // CharNode  字符
            compileChar(tree as CharNode?, startId)
        else if (tree is ESCNode)
        // ESCNode ^ $ \\w \\s \\d .
            compileESCNode(tree as ESCNode?, startId)
        else {
            throw UnsupportedOperationException("can't compile " + tree!!.javaClass.name + "'s yet")
        }
    }

    @Throws(SyntaxError::class)
    private fun compileConcat(tree: ConcatNode, startId: Int): Int {
        var endId = startId
        val endIds = ArrayList<Int>()
        for (segment in tree.segments!!) {
            endId = compile(segment, endId)
            endIds.add(endId)
        }
        // ()中的| 若已经满足直接跳到最后,此为第二个链接
        if (endIds.size > 1)
            for (i in 0..endIds.size - 1 - 1) {
                chart.addBlankConnection(endIds[i], endId)
            }
        return endId
    }

    // 编译 ？
    @Throws(SyntaxError::class)
    private fun compileZeroOrOne(tree: ZeroOrOneNode, startId: Int): Int {
        val endId = compile(tree.node, startId)
        //		chart.addBlankConnection(startId,endId);
        chart.connections[startId].remove(null)
        chart.addConnection(startId, endId, "??")
        return endId
    }

    // 编译 *
    @Throws(SyntaxError::class)
    private fun compileZeroOrMore(tree: ZeroOrMoreNode, startId: Int): Int {
        val endId = compile(tree.node, startId)
        //		chart.addBlankConnection(startId,endId);
        chart.connections[startId].remove(null)
        chart.addConnection(startId, endId, "**")
        return endId
    }

    // 编译 +
    @Throws(SyntaxError::class)
    private fun compileOneOrMore(tree: OneOrMoreNode, startId: Int): Int {
        val endId = compile(tree.node, startId)
        //		chart.addBlankConnection(startId,endId);
        chart.connections[startId].remove(null)
        chart.addConnection(startId, endId, "++")
        return endId
    }

    // 编译 []
    @Throws(SyntaxError::class)
    private fun compileOneCharRange(tree: OneCharRangeNode, startId: Int): Int {
        var endId = startId
        val endIds = ArrayList<Int>()
        for (node in tree.options!!) {
            endIds.add(endId)
            endId = compile(node, endId)
        }
        for (i in endIds) {
            chart.addBlankConnection(i, endId)
        }
        return endId
    }

    // 编译[]里面的  如： a-z 1-8, 判断范围将所含input split("-")即可获得范围
    @Throws(SyntaxError::class)
    private fun compileOne_Char(tree: One_CharNode, startId: Int): Int {
        chart.addConnection(startId, startId + 1, tree.lower_bound + "-" + tree.upper_bound)
        return startId + 1
    }

    // 编译 可选项
    @Throws(SyntaxError::class)
    private fun compileOption(tree: OptionNode, startId: Int): Int {
        var endId = startId
        val endIds = ArrayList<Int>()
        for (option in tree.options!!) {
            endIds.add(endId)
            endId = compile(option, endId)
        }
        for (i in endIds)
            chart.addBlankConnection(i, endId)
        return endId
    }

    // 编译{} 前面有个无宽度跳转，判断多少次，将所含input split(",")即可获得两个数字
    @Throws(SyntaxError::class)
    private fun compileMatchTimes(tree: MatchTimesNode, startId: Int): Int {
        chart.addConnection(startId, startId, tree.lowwer_bound + "," + tree.upper_bound)
        return compile(tree.node, startId)
    }

    // 编译 ^ $ \\w \\s \\d
    @Throws(SyntaxError::class)
    private fun compileESCNode(tree: ESCNode, startId: Int): Int {
        when (tree.function) {
            STARTCHAR -> chart.addConnection(startId, startId + 1, "^^")
            OVERCHAR -> chart.addConnection(startId, startId + 1, "$$")
            ANYCHAR -> chart.addConnection(startId, startId + 1, "\\w")
            BLANKCHAR -> chart.addConnection(startId, startId + 1, "\\s")
            NUMBERCHAR -> chart.addConnection(startId, startId + 1, "\\d")
            ANYNODE -> chart.addConnection(startId, startId + 1, "..")
            else -> throw SyntaxError("don't support it.")
        }
        return startId + 1
    }

    // 编译 字符
    private fun compileChar(tree: CharNode, startId: Int): Int {
        chart.addConnection(startId, startId + 1, tree.char.toString())
        return startId + 1
    }

    companion object {

        @Throws(SyntaxError::class)
        fun compile(parsedPattern: ArrayList<ASTNode>): StateChart {
            return Compile(parsedPattern).compile()
        }

        private val STARTCHAR = 1
        private val OVERCHAR = 2
        private val ANYCHAR = 3
        private val BLANKCHAR = 4
        private val NUMBERCHAR = 5
        private val ANYNODE = 6
    }
}
