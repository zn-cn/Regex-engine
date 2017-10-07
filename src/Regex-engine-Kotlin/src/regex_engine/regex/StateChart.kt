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
        return "StateChart" + connections
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
        }
        connectionsFromState.get(input).add(to)

    }

    // TODO
    // 增加空白链接，即正则运算符的状态
    fun addBlankConnection(from: Int, to: Int) {
        addConnection(from, to, null)
    }

    // TODO
    // 增加最后状态
    fun addFinalState(state: Int) {
        finalStates.add(state)
    }

    // 获取空白链接(to)
    fun getBlankConnections(state: Int): HashSet<Int> {
        return getConnectionsForInput(state, null)[true]
    }

    // 获取输入字符链接(to)
    fun getConnectionsForInput(state: Int, input: String?): HashMap<Boolean, HashSet<Int>> {
        var result = HashMap<Boolean, HashSet<Int>>()
        if (connections.containsKey(state)) {
            if (connections[state].containsKey("..")) {
                if (input == "\n") {
                    result.put(false, HashSet())
                } else {
                    result.put(true, connections[state].get(".."))
                }
            } else if (connections[state].containsKey("^^")) {
                if (state == 0) {
                    result = getConnectionsForInput(1, input)
                } else {
                    result.put(false, HashSet())
                }
            } else if (connections[state].containsKey("$$")) {
                if (isFinalState(state + 1)) {
                    result.put(true, connections[state].get("$$"))
                } else {
                    result.put(false, HashSet())
                }
            } else if (connections[state].containsKey("\\w")) {
                val temp = input!![0].toByte()
                if (temp.toInt() == 95 || temp > 96 && temp < 123 || temp > 64 && temp < 91 || temp > 47 && temp < 58) {
                    result.put(true, connections[state].get("\\w"))
                } else {
                    result.put(false, HashSet())
                }
            } else if (connections[state].containsKey("\\s")) {
                when (input) {
                    "\n", "\t", "\r" -> result.put(true, connections[state].get("\\s"))
                    else -> result.put(false, HashSet())
                }
            } else if (connections[state].containsKey("\\d")) {
                val temp = input!![0].toByte()
                if (temp > 47 && temp < 58) {
                    result.put(true, connections[state].get("\\d"))
                } else {
                    result.put(false, HashSet())
                }
            } else if (connections[state].containsKey("[]")) {
                // 处理 []

                val endId = connections[state].get("[]").toTypedArray()[0] as Int
                var i = state
                val ascii_input = input!![0].toByte()
                while (i < endId) {
                    var lower = 0
                    var upper = 0
                    if (connections[i].containsKey(null)) {
                        lower = connections[i].get(null).toTypedArray()[0]
                        upper = connections[i].get(null).toTypedArray()[0]
                        if (connections[i].get(null).toTypedArray().size > 1) {
                            if (connections[i].get(null).toTypedArray()[0] as Int<connections.get i.get(null).toTypedArray()[1])
                            {
                                upper = connections[i].get(null).toTypedArray()[1]
                            }
                            else
                            {
                                lower = connections[i].get(null).toTypedArray()[1]
                            }
                        }
                        if (ascii_input >= lower && ascii_input <= upper) {
                            val temp = HashSet<Int>()
                            temp.add(endId)
                            result.put(true, temp)
                            break
                        } else {
                            i++
                        }
                    } else {
                        if (connections[i].containsKey(input)) {
                            val temp = HashSet<Int>()
                            temp.add(endId)
                            result.put(true, temp)
                            break
                        } else {
                            i++
                        }
                    }
                }
                if (i == endId) {
                    result.put(false, HashSet())
                }

            } else if (connections[state].containsKey(input)) {
                result.put(true, connections[state].get(input))
            } else {
                result.put(false, HashSet())
            }
        } else {
            result.put(false, HashSet())  // no connections from that state
        }
        return result
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
