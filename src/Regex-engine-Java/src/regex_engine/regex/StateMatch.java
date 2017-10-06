package regex_engine.regex;

import java.util.HashMap;
import java.util.HashSet;

public class StateMatch {

	private StateChart stateChart;
	private HashSet<Integer> currentStates;

	public StateMatch(StateChart chart) {
		stateChart = chart;
		currentStates = new HashSet<>();
		addCurrentState(0);
	}

	// 匹配 input字符 []  . \\w \\d \\s  ^  $
	public boolean accept(char input) {

		HashSet<Integer> newStates = new HashSet<>();
		boolean result = true;
		for(int state: currentStates) {
			HashMap<Boolean, HashSet<Integer>> temp = stateChart.getConnectionsForInput(state, String.valueOf(input));
			if (temp.containsKey(true))
				newStates.addAll(temp.get(true));
			else {
				result = false;
				break;
			}
		}
		if (result){
			currentStates.clear();
			for(int state: newStates)
				addCurrentState(state);
		}
		return result;
	}

	// TODO
	// 匹配 +
	public boolean acceptOneOrMore(String input){
		HashSet<Integer> currentStates2 = new HashSet<>(currentStates);
		return true;
	}
	// TODO
	// 匹配 *
	public boolean acceptZeroOrMore(String input){
		HashSet<Integer> currentStates2 = new HashSet<>(currentStates);
		return true;
	}
	// TODO
	// 匹配 ？
	public boolean acceptZeroOrOne(String input){
		HashSet<Integer> currentStates2 = new HashSet<>(currentStates);
		int bound = (Integer)stateChart.getConnections().get((Integer) currentStates.toArray()[0]).get("??").toArray()[0];
		int num = 0;
		for (int i = 0; i < input.length(); i++){
			if ((Integer)currentStates.toArray()[0] < bound){
				accept(input.charAt(i));
			}
		}
		return true;
	}
	// 匹配（）
	public int acceptConcat(String input){
		int bound = 0;
		for (int k: currentStates){
			bound = (Integer)stateChart.getConnections().get(k).get("()").toArray()[0];
		}
		int j = 0;
		for (; j < input.length(); j++){
			if ((Integer)currentStates.toArray()[0] >= bound){
				break;
			}
			if (!accept(input.charAt(j))){
				int min = (Integer)currentStates.toArray()[0];

				currentStates.clear();
				currentStates.add((Integer)stateChart.getConnections().get(min).get(null).toArray()[0]);
				j = -1;
			} else {
				int max = 0;
				for (int i: currentStates){
					if (i > max)
						max = i;
				}
				if (stateChart.getConnections().get(max).containsKey("))")){
					currentStates.clear();
					currentStates.add((Integer)stateChart.getConnections().get(max).get("))").toArray()[0]);
				}
			}
		}
			return j;
	}

	// 匹配() 并捕获
	public HashMap<Boolean, Integer> captureString(String input){
		int j = acceptConcat(input);
		HashMap<Boolean, Integer> result = new HashMap<>();
		if (j == 0){
			result.put(false, 0);
		} else {
			result.put(true, j);
		}
		return result;
	}
	// TODO
	// 匹配{}
	public boolean acceptOneCharRange(String input){
		return false;
	}

	// 增加当前状态
	private void addCurrentState(int state) {
//		for(int newState: stateChart.getBlankConnections(state))
//			addCurrentState(newState);   // 采用回归使（）中的）的状态后返回，先返回（的状态
//		if(stateChart.notOnlyBlankConnections(state))
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

	public StateChart getStateChart(){
		return this.stateChart;
	}

	public HashSet<Integer> getCurrentStates(){
		return this.currentStates;
	}

	public void setCurrentStates(HashSet<Integer> currentStates){
		this.currentStates = currentStates;
	}
}
