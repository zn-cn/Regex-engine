package regex_engine.regex

import java.util.HashMap
import java.util.HashSet

// 状态表
class StateChart {

    val connections: HashMap<Int, HashMap<String, HashSet<Int>>>
    private val finalStates: HashSet<Int>

    init {
        connections = HashMap()
        finalStates = HashSet()
    }

    override fun toString(): String {
        return "regex_engine.regex.StateChart" + connections
    }

    // 增加链接（下一个状态 from -> to )
    fun addConnection(from: Int, to: Int, input: String?) {
        val connectionsFromState: HashMap<String, HashSet<Int>>
        if (!connections.containsKey(from)) {
            connectionsFromState = HashMap()
            connections.put(from, connectionsFromState)
        } else {
            connectionsFromState = connections[from]
        }
        if (!connectionsFromState.containsKey(input)) {
            connectionsFromState.put(input, HashSet())
        } else
            connectionsFromState[input].add(to)  //若已存在则继续增加链接
        connectionsFromState[input].add(to)
    }

    // 增加空白链接，即正则运算符的状态
    fun addBlankConnection(from: Int, to: Int) {
        addConnection(from, to, null)
    }

    // 增加最后状态
    fun addFinalState(state: Int) {
        finalStates.add(state)
    }

    // 获取空白链接(to)
    fun getBlankConnections(state: Int): HashSet<Int> {
        return getConnectionsForInput(state, null)
    }

    // 获取输入字符链接(to)
    fun getConnectionsForInput(state: Int, input: String?): HashSet<Int> {
        return if (connections.containsKey(state) && connections[state].containsKey(input)) {
            connections[state].get(input)
        } else
            HashSet() // no connections from that state
    }

    // 判断是否是空白链接（正则运算符）
    fun notOnlyBlankConnections(state: Int): Boolean {
        val connectionsFrom = connections[state]
        if (connectionsFrom != null) {
            val keys = connectionsFrom.keys
            return keys != hashSetOfNull
        } else
            return true
    }

    // 判断是否达到最后状态
    fun isFinalState(state: Int): Boolean {
        return finalStates.contains(state)
    }

    companion object {
        private val hashSetOfNull = HashSet<String>()

        init {
            hashSetOfNull.add(null)
        }
    }
}
