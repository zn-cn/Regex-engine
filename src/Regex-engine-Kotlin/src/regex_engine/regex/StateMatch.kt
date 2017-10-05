package regex_engine.regex

import java.util.HashSet

class StateMatch(val stateChart: StateChart) {
    var currentStates: HashSet<Int>? = null

    init {
        currentStates = HashSet()
        addCurrentState(0)
    }

    // 匹配 input字符
    fun accept(input: Char) {
        val newStates = HashSet<Int>()
        for (state in currentStates!!)
            newStates.addAll(stateChart.getConnectionsForInput(state, input.toString()))

        currentStates!!.clear()
        for (state in newStates)
            addCurrentState(state)
    }

    // 增加当前状态
    private fun addCurrentState(state: Int) {
        for (newState in stateChart.getBlankConnections(state))
            addCurrentState(newState)   // 采用回归使（）中的）的状态后返回，先返回（的状态
        if (stateChart.notOnlyBlankConnections(state))
            currentStates!!.add(state)
    }

    // 是否到达最后的状态
    val isOnFinalState: Boolean
        get() {
            for (state in currentStates!!)
                if (stateChart.isFinalState(state))
                    return true
            return false
        }

    // 清空，准备下一次正则匹配
    fun reset() {
        currentStates!!.clear()
        addCurrentState(0)
    }
}
