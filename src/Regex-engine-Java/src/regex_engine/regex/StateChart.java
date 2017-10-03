package regex_engine.regex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

// 状态表
public class StateChart {

	private HashMap<Integer,HashMap<Character,HashSet<Integer>>> connections;
	private HashSet<Integer> finalStates;
	//	private static final HashSet<Integer> hashSetOfNull = new HashSet<>();
	private static final Set<Character> hashSetOfNull = new HashSet<>();

	static {
		hashSetOfNull.add(null);
	}

	public StateChart() {
		connections = new HashMap<>();
		finalStates = new HashSet<>();
	}
	@Override
	public String toString() {
		return "regex_engine.regex.StateChart" + connections;
	}

	// 增加链接（下一个状态 from -> to )
	public void addConnection(int from, int to, Character input) {
		HashMap<Character,HashSet<Integer>> connectionsFromState;
		if(!connections.containsKey(from)) {
			connectionsFromState = new HashMap<>();
			connections.put(from,connectionsFromState);
		} else {
			connectionsFromState = connections.get(from);
		}
		if(!connectionsFromState.containsKey(input)) {
			connectionsFromState.put(input,new HashSet<>(2));
		}
		connectionsFromState.get(input).add(to);
	}

	// 增加空白链接，即正则运算符的状态
	public void addBlankConnection(int from, int to) {
		addConnection(from,to,null);
	}

	// 增加最后状态
	public void addFinalState(int state) {
		finalStates.add(state);
	}

	// 获取空白链接(to)
	public HashSet<Integer> getBlankConnections(int state) {
		return getConnectionsForInput(state,null);
	}

	// 获取输入字符链接(to)
	public HashSet<Integer> getConnectionsForInput(int state, Character input) {
		if(connections.containsKey(state) && connections.get(state).containsKey(input)) {
				return connections.get(state).get(input);
		} else
			return new HashSet<>(); // no connections from that state
	}

	// 判断是否是空白链接（正则运算符）
	public boolean notOnlyBlankConnections(int state) {
		HashMap<Character,HashSet<Integer>> connectionsFrom = connections.get(state);
		if(connectionsFrom != null) {
			Set<Character> keys = connectionsFrom.keySet();
			return !keys.equals(hashSetOfNull);
		} else
			return true;
	}

	// 判断是否达到最后状态
	public boolean isFinalState(int state) {
		return finalStates.contains(state);
	}

}
