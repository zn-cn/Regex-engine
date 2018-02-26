package regex_engine.compile;

import java.util.ArrayList;
import java.util.HashSet;

import com.sun.org.apache.bcel.internal.generic.BREAKPOINT;
import regex_engine.parse.SyntaxError;
import regex_engine.regex.StateChart;
import regex_engine.parse.astnode.*;

public class Compile {

    public static StateChart compile(ArrayList<ASTNode> parsedPattern) throws SyntaxError {
        return new Compile(parsedPattern).compile();
    }

    private ArrayList<ASTNode> astnode;
    private StateChart chart;

    private static final int STARTCHAR = 1;
    private static final int OVERCHAR = 2;
    private static final int ANYCHAR = 3;
    private static final int BLANKCHAR = 4;
    private static final int NUMBERCHAR = 5;
    private static final int ANYNODE = 6;

    private Compile(ArrayList<ASTNode> parsedPattern) {
        astnode = parsedPattern;
        chart = new StateChart();
    }

    private StateChart compile() throws SyntaxError {
        int finalstate = 0;
        for (ASTNode node : astnode) {
            finalstate = compile(node, finalstate);
        }
        if (chart.getConnections().get(finalstate - 1).containsKey("$$"))
            finalstate = finalstate - 1;
        chart.addFinalState(finalstate);
        return chart;
    }

    // 编译
    private int compile(ASTNode tree, int startId) throws SyntaxError {
        if (tree instanceof ConcatNode)            // ConcatNode ()
            return compileConcat((ConcatNode) tree, startId);
        else if (tree instanceof ZeroOrOneNode)      // ZeroOrOneNode  ?
            return compileZeroOrOne((ZeroOrOneNode) tree, startId);
        else if (tree instanceof ZeroOrMoreNode)      // ZeroOrMoreNode  *
            return compileZeroOrMore((ZeroOrMoreNode) tree, startId);
        else if (tree instanceof OneOrMoreNode)      // OneOrMoreNode  +
            return compileOneOrMore((OneOrMoreNode) tree, startId);
        else if (tree instanceof OptionNode)      // OptionNode  |
            return compileOption((OptionNode) tree, startId);
        else if (tree instanceof OneCharRangeNode)  // OneCharRangeNode  []
            return compileOneCharRange((OneCharRangeNode) tree, startId);
        else if (tree instanceof One_CharNode)       // One_CharNode []里面的 如: a-z 1-8
            return compileOne_Char((One_CharNode) tree, startId);
        else if (tree instanceof MatchTimesNode)    // MatchTimesNode {}
            return compileMatchTimes((MatchTimesNode) tree, startId);
        else if (tree instanceof CharNode)         // CharNode  字符
            return compileChar((CharNode) tree, startId);
        else if (tree instanceof ESCNode)         // ESCNode ^ $ \\w \\s \\d .
            return compileESCNode((ESCNode) tree, startId);
        else {
            throw new UnsupportedOperationException("can't compile " + tree.getClass().getName() + "'s yet");
        }
    }

    // (|)
    private int compileConcat(ConcatNode tree, int startId) throws SyntaxError {
        int endId = startId;
        ArrayList<Integer> endIds = new ArrayList<>();
        for (ASTNode segment : tree.getSegments()) {
            endId = compile(segment, endId);
            endIds.add(endId);
        }
        // ()中的| 若已经满足直接跳到最后,此为第二个链接

        for (int i: endIds){
            chart.addConnection(i, endId, "))");
        }
//        chart.getConnections().get(endId).remove("))");
//        HashSet<Integer> temp = new HashSet<>();
//        temp.add(endId + 1);
//        chart.getConnections().get(endId).put("))", temp);

        chart.addConnection(startId, endId, "()");
        return endId;
    }

    // 编译 ？
    private int compileZeroOrOne(ZeroOrOneNode tree, int startId) throws SyntaxError {
        int endId = compile(tree.getNode(), startId);
//		chart.addBlankConnection(startId,endId);
//        chart.getConnections().get(startId).remove(null);
        chart.addConnection(startId, endId, "??");
        return endId;
    }

    // 编译 *
    private int compileZeroOrMore(ZeroOrMoreNode tree, int startId) throws SyntaxError {
        int endId = compile(tree.getNode(), startId);
//		chart.addBlankConnection(startId,endId);
//        chart.getConnections().get(startId).remove(null);
        chart.addConnection(startId, endId, "**");
        return endId;
    }

    // 编译 +
    private int compileOneOrMore(OneOrMoreNode tree, int startId) throws SyntaxError {
        int endId = compile(tree.getNode(), startId);
//		chart.addBlankConnection(startId,endId);
//        chart.getConnections().get(startId).remove(null);
        chart.addConnection(startId, endId, "++");
        return endId;
    }

    // 编译 []
    private int compileOneCharRange(OneCharRangeNode tree, int startId) throws SyntaxError {
        int endId = startId;
        for (ASTNode node : tree.getOptions()) {
            endId = compile(node, endId);
        }
        chart.addConnection(startId, endId, "[]");
        return endId;
    }

    // 编译[]里面的  如： a-z 1-8
    private int compileOne_Char(One_CharNode tree, int startId) throws SyntaxError {
        chart.addConnection(startId, (byte)tree.getLower_bound(), null);
        chart.addConnection(startId, (byte)tree.getUpper_bound(), null);
        return startId + 1;
    }

    // 编译 可选项
    private int compileOption(OptionNode tree, int startId) throws SyntaxError {
        int endId = startId;
        ArrayList<Integer> endIds = new ArrayList<>();
        for (ASTNode option : tree.getOptions()) {
            endIds.add(endId);
            endId = compile(option, endId);
        }
        for (int i : endIds)
            chart.addBlankConnection(i, endId);
        return endId;
    }

    // 编译{} 前面有个无宽度跳转，判断多少次，将所含input split(",")即可获得两个数字
    private int compileMatchTimes(MatchTimesNode tree, int startId) throws SyntaxError {
        chart.addConnection(startId, tree.getLower_bound(), "}}");
        chart.addConnection(startId, tree.getUpper_bound(), "}}");
        chart.addConnection(startId, startId, "{}");
        return compile(tree.getNode(), startId);
    }

    // 编译 ^ $ \\w \\s \\d
    private int compileESCNode(ESCNode tree, int startId) throws SyntaxError {
        switch (tree.getFunction()) {
            case STARTCHAR:
                chart.addConnection(startId, startId + 1, "^^");
                break;
            case OVERCHAR:
                chart.addConnection(startId, startId + 1, "$$");
                break;
            case ANYCHAR:
                chart.addConnection(startId, startId + 1, "\\w");
                break;
            case BLANKCHAR:
                chart.addConnection(startId, startId + 1, "\\s");
                break;
            case NUMBERCHAR:
                chart.addConnection(startId, startId + 1, "\\d");
                break;
            case ANYNODE:
                chart.addConnection(startId, startId + 1, "..");
                break;
            default:
                throw new SyntaxError("don't support it.");
        }
        return startId + 1;
    }

    // 编译 字符
    private int compileChar(CharNode tree, int startId) {
        chart.addConnection(startId, startId + 1, String.valueOf(tree.getChar()));
        return startId + 1;
    }
}
