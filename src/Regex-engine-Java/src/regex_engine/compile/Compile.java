package regex_engine.compile;

import java.util.ArrayList;

import regex_engine.regex.StateChart;
import regex_engine.parse.astnode.*;

public class Compile {

	public static StateChart compile(ASTNode parsedPattern) {
		return new Compile(parsedPattern).compile();
	}

	private ASTNode astnode;
	private StateChart chart;

	// ^ $ \\w  \\s \\d   分别对应
    private static final int STARTCHAR = 1;
    private static final int OVERCHAR = 2;
    private static final int ANYCHAR = 3;
    private static final int BLANKCHAR = 4;
    private static final int NUMBERCHAR = 5;


	private Compile(ASTNode parsedPattern) {
		astnode = parsedPattern;
		chart = new StateChart();
	}

	private StateChart compile() {
		chart.addFinalState(compile(astnode,0));
		return chart;
	}

	// 编译
	// TODO
	private int compile(ASTNode tree, int startId) {
		if(tree instanceof ConcatNode)            // ConcatNode
			return compileConcat((ConcatNode)tree, startId);
		else if(tree instanceof OptionNode)      // OptionNode
			return compileOption((OptionNode)tree, startId);
		else if(tree instanceof OneCharRangeNode)  // OneCharRangeNode  []
			return compileOneCharRange((OneCharRangeNode)tree, startId);
		else if(tree instanceof CharNode)         // CharNode  字符
			return compileChar((CharNode)tree, startId);
		else if(tree instanceof ZeroOrOneNode)      // ZeroOrOneNode
			return compileZeroOrOne((ZeroOrOneNode)tree, startId);
		else if(tree instanceof ZeroOrMoreNode)      // ZeroOrMoreNode
			return compileZeroOrMore((ZeroOrMoreNode)tree, startId);
		else if(tree instanceof OneOrMoreNode)      // OneOrMoreNode
			return compileOneOrMore((OneOrMoreNode)tree, startId);
		else {
			throw new UnsupportedOperationException("can't compile " + tree.getClass().getName() + "'s yet");
		}
	}

    private int compileConcat(ConcatNode tree, int startId) {
        int endId = startId;
        for(ASTNode segment: tree.getSegments())
            endId = compile(segment,endId);
        return endId;
    }


	// 编译 ？
	private int compileZeroOrOne(ZeroOrOneNode tree, int startId) {
		int endId = compile(tree.getNode(),startId);
		chart.addBlankConnection(startId,endId);
		return endId;
	}

	// TODO
	// 编译 *
	private int compileZeroOrMore(ZeroOrMoreNode tree, int startId) {
		int endId = compile(tree.getNode(),startId);
		chart.addBlankConnection(startId,endId);
		return endId;
	}

	// TODO
	// 编译 *
	private int compileOneOrMore(OneOrMoreNode tree, int startId) {
		int endId = compile(tree.getNode(),startId);
		chart.addBlankConnection(startId,endId);
		return endId;
	}

	// TODO
	// 编译 .
	private int compileAny(AnyNode tree, int startId) {
		int endId = compile(tree.getNode(),startId);
		chart.addBlankConnection(startId,endId);
		return endId;
	}

	// 编译 字符
	private int compileChar(CharNode tree, int startId) {
		chart.addConnection(startId,startId + 1,tree.getChar());
		return startId + 1;
	}

	// TODO
	// 编译 []
	private int compileOneCharRange(OneCharRangeNode tree, int startId){
//		ArrayList<Integer> startIds = new ArrayList<>();
//		ArrayList<Integer> endIds = new ArrayList<>();
//		int endId = startId;
//		for(ASTNode option: tree.getOptions()) {
//			int nextStartId = endId + 1;
//			startIds.add(nextStartId);
//			endId = compile(option,nextStartId);
//			endIds.add(endId);
//		}
//		int overallEnd = endIds.get(endIds.size()-1) + 1;
//		for(int i=0; i < startIds.size(); i++) {
//			chart.addBlankConnection(startId,startIds.get(i));
//			chart.addBlankConnection(endIds.get(i),overallEnd);
//		}
//		return overallEnd;
		return 0;
	}

	// 编译（|）
	private int compileOption(OptionNode tree, int startId) {
		ArrayList<Integer> startIds = new ArrayList<>();
		ArrayList<Integer> endIds = new ArrayList<>();
		int endId = startId;
		for(ASTNode option: tree.getOptions()) {
			int nextStartId = endId + 1;
			startIds.add(nextStartId);
			endId = compile(option,nextStartId);
			endIds.add(endId);
		}
		int overallEnd = endIds.get(endIds.size()-1) + 1;
		for(int i=0; i < startIds.size(); i++) {
			chart.addBlankConnection(startId,startIds.get(i));
			chart.addBlankConnection(endIds.get(i),overallEnd);
		}
		return overallEnd;
	}



}
