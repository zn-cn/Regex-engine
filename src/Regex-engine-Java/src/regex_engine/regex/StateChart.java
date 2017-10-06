package regex_engine.regex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// 状态表
public class StateChart {

    private HashMap<Integer, HashMap<String, HashSet<Integer>>> connections;
    private HashSet<Integer> finalStates;
    private static final Set<String> hashSetOfNull = new HashSet<>();

    static {
        hashSetOfNull.add(null);
    }

    public StateChart() {
        connections = new HashMap<>();
        finalStates = new HashSet<>();
    }

    @Override
    public String toString() {
        return "StateChart" + connections;
    }

    // 增加链接（下一个状态 from -> to )
    public void addConnection(int from, int to, String input) {
        HashMap<String, HashSet<Integer>> connectionsFromState;
        if (!connections.containsKey(from)) {
            connectionsFromState = new HashMap<>();
            connections.put(from, connectionsFromState);
        } else {
            connectionsFromState = connections.get(from);
        }
        if (!connectionsFromState.containsKey(input)) {
            connectionsFromState.put(input, new HashSet<>());
        }
        connectionsFromState.get(input).add(to);

    }

    // TODO
    // 增加空白链接，即正则运算符的状态
    public void addBlankConnection(int from, int to) {
        addConnection(from, to, null);
    }

    // TODO
    // 增加最后状态
    public void addFinalState(int state) {
        finalStates.add(state);
    }

    // 获取空白链接(to)
    public HashSet<Integer> getBlankConnections(int state) {
        return getConnectionsForInput(state, null).get(true);
    }

    // 获取输入字符链接(to)
    public HashMap<Boolean, HashSet<Integer>> getConnectionsForInput(int state, String input) {
        HashMap<Boolean, HashSet<Integer>> result = new HashMap<>();
        if (connections.containsKey(state)) {
            if (connections.get(state).containsKey("..")) {
                if (input.equals("\n")) {
                    result.put(false, new HashSet<>());
                } else {
                    result.put(true, connections.get(state).get(".."));
                }
            } else if (connections.get(state).containsKey("^^")) {
                if (state == 0) {
                    result = getConnectionsForInput(1, input);
                } else {
                    result.put(false, new HashSet<>());
                }
            } else if (connections.get(state).containsKey("$$")) {
                if (isFinalState(state + 1)) {
                    result.put(true, connections.get(state).get("$$"));
                } else {
                    result.put(false, new HashSet<>());
                }
            } else if (connections.get(state).containsKey("\\w")) {
                byte temp = (byte) input.charAt(0);
                if (temp == 45 || (temp > 96 && temp < 123) || (temp > 64 && temp < 91) || (temp > 47 && temp < 58)) {
                    result.put(true, connections.get(state).get("\\w"));
                } else {
                    result.put(false, new HashSet<>());
                }
            } else if (connections.get(state).containsKey("\\s")) {
                switch (input) {
                    case "\n":
                    case "\t":
                    case "\r":
                        result.put(true, connections.get(state).get("\\s"));
                        break;
                    default:
                        result.put(false, new HashSet<>());
                        break;
                }
            } else if (connections.get(state).containsKey("\\d")) {
                byte temp = (byte) input.charAt(0);
                if (temp > 47 && temp < 58) {
                    result.put(true, connections.get(state).get("\\d"));
                } else {
                    result.put(false, new HashSet<>());
                }
            } else if (connections.get(state).containsKey("[]")) {
                // 处理 []

                int endId = (Integer)connections.get(state).get("[]").toArray()[0];
                int i = state;
                byte ascii_input = (byte)input.charAt(0);
                while (i < endId){
                    int lower = 0;
                    int upper = 0;
                    if (connections.get(i).containsKey(null))
                    {
                        lower = (Integer)connections.get(i).get(null).toArray()[0];
                        upper = (Integer)connections.get(i).get(null).toArray()[0];

                        if ((Integer)connections.get(i).get(null).toArray()[0] < (Integer)connections.get(i).get(null).toArray()[1]){
                            upper = (Integer)connections.get(i).get(null).toArray()[1];
                        } else {
                            lower = (Integer)connections.get(i).get(null).toArray()[1];
                        }
                        if (ascii_input >= lower && ascii_input <= upper){
                            HashSet<Integer> temp = new HashSet<>();
                            temp.add(endId);
                            result.put(true, temp);
                            break;
                        } else {
                            i++;
                        }
                    } else {
                        if (connections.get(i).containsKey(input)){
                            HashSet<Integer> temp = new HashSet<>();
                            temp.add(endId);
                            result.put(true, temp);
                            break;
                        } else {
                            i++;
                        }
                    }
                }
                if (i == endId){
                    result.put(false, new HashSet<>());
                }

            } else if (connections.get(state).containsKey(input)) {
                result.put(true, connections.get(state).get(input));
            } else {
                result.put(false, new HashSet<>());
            }
        } else {
            result.put(false, new HashSet<>());  // no connections from that state
        }
        return result;
    }

    // 判断是否是空白链接（正则运算符）
    public boolean notOnlyBlankConnections(int state) {
        HashMap<String, HashSet<Integer>> connectionsFrom = connections.get(state);
        if (connectionsFrom != null) {
            Set<String> keys = connectionsFrom.keySet();
            return !keys.equals(hashSetOfNull);
        } else
            return true;
    }

    // 判断是否达到最后状态
    public boolean isFinalState(int state) {
        return finalStates.contains(state);
    }

    public HashMap<Integer, HashMap<String, HashSet<Integer>>> getConnections() {
        return connections;
    }
}
