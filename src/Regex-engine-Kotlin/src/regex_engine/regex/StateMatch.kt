package regex_engine.regex

import java.util.HashMap
import java.util.HashSet

class StateMatch(val stateChart: StateChart) {
    var currentStates: HashSet<Int>? = null

    init {
        currentStates = HashSet()
        addCurrentState(0)
    }

    // 匹配 input字符 . \\w \\d \\s  ^  $
    fun accept(input: Char): Boolean {

        val newStates = HashSet<Int>()
        var result = true
        for (state in currentStates!!) {
            val temp = stateChart.getConnectionsForInput(state, input.toString())
            if (temp.containsKey(true))
                newStates.addAll(temp[true])
            else {
                result = false
                break
            }
        }
        if (result) {
            currentStates!!.clear()
            for (state in newStates)
                addCurrentState(state)
        }
        return result
    }


    // 匹配（）
    fun acceptConcat(input: String): Int {
        var bound = 0
        for (k in currentStates!!) {
            bound = stateChart.connections[k].get("()").toTypedArray()[0]
        }
        var j = 0
        while (j < input.length) {
            if (currentStates!!.toTypedArray()[0] as Int >= bound) {
                break
            }
            if (!accept(input[j])) {
                val min = currentStates!!.toTypedArray()[0] as Int

                currentStates!!.clear()
                currentStates!!.add(stateChart.connections[min].get(null).toTypedArray()[0] as Int)
                j = -1
            } else {
                var max = 0
                for (i in currentStates!!) {
                    if (i > max)
                        max = i
                }
                if (stateChart.connections[max].containsKey("))")) {
                    currentStates!!.clear()
                    currentStates!!.add(stateChart.connections[max].get("))").toTypedArray()[0] as Int)
                }
            }
            j++
        }
        return j
    }

    // 匹配() 并捕获
    fun captureString(input: String): HashMap<Boolean, Int> {
        val j = acceptConcat(input)
        val result = HashMap<Boolean, Int>()
        if (j == 0) {
            result.put(false, 0)
        } else {
            result.put(true, j)
        }
        return result
    }


    // 增加当前状态
    private fun addCurrentState(state: Int) {
        //		for(int newState: stateChart.getBlankConnections(state))
        //			addCurrentState(newState);   // 采用回归使（）中的）的状态后返回，先返回（的状态
        //		if(stateChart.notOnlyBlankConnections(state))
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
