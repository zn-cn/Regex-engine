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

	private Compile(ASTNode parsedPattern) {
		astnode = parsedPattern;
		chart = new StateChart();
	}

	private StateChart compile() {
		chart.addFinalState(compile(astnode,0));
		return chart;
	}

	// 编译
	private int compile(ASTNode tree, int startId) {
		if(tree instanceof ConcatNode)
			return compileConcat((ConcatNode)tree, startId);
		else if(tree instanceof OptionNode)
			return compileOption((OptionNode)tree, startId);
		else if(tree instanceof CharNode)
			return compileChar((CharNode)tree, startId);
		else if(tree instanceof ZeroOrOneNode)
			return compileZeroOrOne((ZeroOrOneNode)tree, startId);
		else if (tree instanceof StartPosNode)
			return compileStartPos((StartPosNode)tree, startId);
		else if (tree instanceof OverPosNode)
			return compileOverPos((OverPosNode)tree, startId);
		else {
			throw new UnsupportedOperationException("can't compile " + tree.getClass().getName() + "'s yet");
		}
	}

	// 编译 ^
	private int compileStartPos(StartPosNode tree, int startId) {
		chart.addBlankConnection(startId, startId + 1);
		return startId + 1;
	}
	// 编译 $
	private int compileOverPos(OverPosNode tree, int startId) {
		chart.addBlankConnection(startId, startId + 1);
		return startId + 1;
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

	// 编译 []
	private int compileOneCharRange(OneCharRangeNode tree, int startId){
		ArrayList<Integer> startIds = new ArrayList<>();
		ArrayList<Integer> endIds = new ArrayList<>();
		int endId = startId;
		for (ASTNode option: tree.getOptions()){
			int nextStartId = endId + 1;

		}
		int overallEnd = endIds.get(endIds.size()-1) + 1;
		return overallEnd;
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


	private int compileConcat(ConcatNode tree, int startId) {
		int endId = startId;
		for(ASTNode segment: tree.getSegments())
			endId = compile(segment,endId);
		return endId;
	}

}
