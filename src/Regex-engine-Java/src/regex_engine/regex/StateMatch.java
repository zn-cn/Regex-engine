package regex_engine.regex;

import java.util.HashSet;

public class StateMatch {

	private StateChart stateChart;
	private HashSet<Integer> currentStates;

	public StateMatch(StateChart chart) {
		stateChart = chart;
		currentStates = new HashSet<>();
		addCurrentState(0);
	}
	// 匹配 input字符
	public void accept(char input) {
		HashSet<Integer> newStates = new HashSet<>();
		for(int state: currentStates)
			newStates.addAll(stateChart.getConnectionsForInput(state, input));

		currentStates.clear();
		for(int state: newStates)
			addCurrentState(state);
	}
	// 增加当前状态
	private void addCurrentState(int state) {
		for(int newState: stateChart.getBlankConnections(state))
			addCurrentState(newState);   // 采用回归使（）中的）的状态后返回，先返回（的状态
		if(stateChart.notOnlyBlankConnections(state))
			currentStates.add(state);
	}
	// 是否到达最后的状态
	public boolean isOnFinalState() {
		for(int state: currentStates)
			if(stateChart.isFinalState(state))
				return true;
		return false;
	}

	// 清空，准备下一次正则匹配
	public void reset() {
		currentStates.clear();
		addCurrentState(0);
	}

}
