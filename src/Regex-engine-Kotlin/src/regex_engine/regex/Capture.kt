package regex_engine.regex

import regex_engine.compile.Compile
import regex_engine.parse.Parse
import regex_engine.parse.SyntaxError

import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet

class Capture @Throws(SyntaxError::class)
constructor(private val regex: String) {
    private val match: StateMatch
    private val captures = ArrayList<String>()

    init {
        match = StateMatch(Compile.compile(Parse.parse(regex)))
    }

    @Throws(SyntaxError::class)
    fun matchAndCapture(input: String): ArrayList<String> {
        var isfinalstate = true
        var i = 0
        while (i < input.length) {
            val judge = judgeFunction(0)
            if (judge == 0) {
                if (!match.accept(input[i])) {
                    isfinalstate = false
                    break
                }
            } else if (judge == 1) {
                if (!acceptZeroOrMore(input.substring(i)))
                    isfinalstate = false
                break
            } else if (judge == 2) {
                if (!acceptZeroOrOne(input.substring(i)))
                    isfinalstate = false
                break
            } else if (judge == 3) {
                if (!acceptOneOrMore(input.substring(i)))
                    isfinalstate = false
                break
            } else if (judge == 4) {
                val capture = match.captureString(input.substring(i))

                if (capture.containsKey(true)) {
                    captures.add(input.substring(i, i + capture[true]))
                    i = i + capture[true] - 1

                } else {
                    isfinalstate = false
                    break
                }
            } else if (judge == 5) {
                if (!acceptOneCharRange(input.substring(i)))
                    isfinalstate = false
                break
            } else
                throw SyntaxError("The engine has some problems.")
            i++
        }

        if (isfinalstate && match.isOnFinalState)
            isfinalstate = true
        //        boolean result = match.isOnFinalState();
        match.reset()
        return if (isfinalstate) {
            captures
        } else {
            throw SyntaxError("The regex doesn't match your string.")
        }
    }

    @Throws(SyntaxError::class)
    private fun judgeFunction(f: Int): Int {
        val min = match.currentStates!!.toTypedArray()[0] as Int
        val state = match.stateChart.connections[min]
        return if (f == 0) {
            if (state.containsKey("**"))
                1
            else if (state.containsKey("??"))
                2
            else if (state.containsKey("++"))
                3
            else if (state.containsKey("{}"))
                5
            else if (state.containsKey("()"))
                4
            else
                0
        } else if (f == 1) {
            if (state.containsKey("??"))
                2
            else if (state.containsKey("++"))
                3
            else if (state.containsKey("{}"))
                5
            else if (state.containsKey("()"))
                4
            else
                0
        } else if (f == 2) {
            if (state.containsKey("**"))
                1
            else if (state.containsKey("++"))
                3
            else if (state.containsKey("{}"))
                5
            else if (state.containsKey("()"))
                4
            else
                0
        } else if (f == 3) {
            if (state.containsKey("**"))
                1
            else if (state.containsKey("??"))
                2
            else if (state.containsKey("{}"))
                5
            else if (state.containsKey("()"))
                4
            else
                0
        } else if (f == 5) {
            if (state.containsKey("**"))
                1
            else if (state.containsKey("??"))
                2
            else if (state.containsKey("++"))
                3
            else if (state.containsKey("()"))
                4
            else
                0
        } else
            throw SyntaxError("The engine has some ploblems.")

    }

    // 匹配 ？
    @Throws(SyntaxError::class)
    fun acceptZeroOrOne(input: String): Boolean {
        //        HashSet<Integer> currentStates2 = new HashSet<>(currentStates);

        val bound = match.stateChart.connections[match.currentStates!!.toTypedArray()[0]].get("??").toTypedArray()[0] as Int
        var times = 0
        var i = 0
        while (i < input.length) {
            val judge: Int
            if (i == 0)
                judge = judgeFunction(2)
            else
                judge = judgeFunction(0)

            if (judge == 0) {
                if (!match.accept(input[i])) {
                    if (times == 0) {
                        match.currentStates!!.clear()
                        match.currentStates!!.add(bound)
                        i = -1
                        times++
                    } else
                        break

                }
            } else if (judge == 1) {
                if (times != 0 && !acceptZeroOrMore(input.substring(i))) {
                    break
                } else
                    throw SyntaxError("Invalid syntax.")
            } else if (judge == 2) {
                if (times != 0 && !acceptZeroOrOne(input.substring(i))) {
                    break
                } else
                    throw SyntaxError("Invalid syntax.")
            } else if (judge == 3) {
                if (times != 0 && !acceptOneOrMore(input.substring(i))) {
                    break
                } else
                    throw SyntaxError("Invalid syntax.")
            } else if (judge == 4) {
                val capture = match.captureString(input.substring(i))
                if (capture.containsKey(false)) {
                    if (times == 0) {
                        match.currentStates!!.clear()
                        match.currentStates!!.add(bound)
                        i = -1
                        times++
                    } else
                        break
                } else {
                    captures.add(input.substring(i, i + capture[true]))
                    i = i + capture[true] - 1
                }
            } else if (judge == 5) {
                if (times != 0) {
                    acceptOneCharRange(input.substring(i))
                    break
                } else
                    throw SyntaxError("Invalid syntax.")
            } else
                throw SyntaxError("The engine has some problems.")
            i++

        }
        return match.isOnFinalState
    }

    // 匹配{}  暂时只支持 字符
    @Throws(SyntaxError::class)
    fun acceptOneCharRange(input: String): Boolean {
        var lower_bound = match.stateChart.connections[match.currentStates!!.toTypedArray()[0]].get("}}").toTypedArray()[0] as Int
        var upper_bound = match.stateChart.connections[match.currentStates!!.toTypedArray()[0]].get("}}").toTypedArray()[0] as Int
        if (match.stateChart.connections[match.currentStates!!.toTypedArray()[0]].get("}}").toTypedArray().size > 1) {
            val temp = match.stateChart.connections[match.currentStates!!.toTypedArray()[0]].get("}}").toTypedArray()[1] as Int
            if (temp > lower_bound)
                upper_bound = temp
            else
                lower_bound = temp
        }
        val currentstate = match.currentStates!!.toTypedArray()[0] as Int
        var times = 0
        var i = 0
        while (i < input.length) {
            val judge: Int
            if (times < lower_bound)
                judge = judgeFunction(5)
            else
                judge = judgeFunction(0)

            if (judge == 0) {
                if (!match.accept(input[i])) {
                    if (times < lower_bound || times > upper_bound) {
                        break
                    } else {
                        i--
                        if (upper_bound == Integer.MAX_VALUE)
                            times = input.length
                        else
                            times = upper_bound + 1
                        match.currentStates!!.clear()
                        match.currentStates!!.add(currentstate)
                    }
                } else {
                    times++
                    if (times < lower_bound) {
                        match.currentStates!!.clear()
                        match.currentStates!!.add(currentstate)

                    }
                }
            } else if (judge == 1) {
                if (!acceptZeroOrMore(input.substring(i)))

                    break
            } else if (judge == 2) {
                if (!acceptZeroOrOne(input.substring(i)))

                    break
            } else if (judge == 3) {
                if (!acceptOneOrMore(input.substring(i)))

                    break
            } else if (judge == 4) {
                val capture = match.captureString(input.substring(i))
                val concat_state = match.acceptConcat(input.substring(i))
                if (capture.containsKey(false)) {
                    if (times < lower_bound || times > upper_bound) {
                        break
                    } else {
                        i--
                        if (upper_bound == Integer.MAX_VALUE)
                            times = input.length
                        else
                            times = upper_bound + 1
                        match.currentStates!!.clear()
                        match.currentStates!!.add(match.stateChart.connections[currentstate].get("()").toTypedArray()[0] as Int)
                    }
                } else {
                    times++
                    if (times < lower_bound) {
                        match.currentStates!!.clear()
                        match.currentStates!!.add(currentstate)
                    }
                    captures.add(input.substring(i, i + capture[true]))
                    i = i + concat_state - 1
                }

            } else
                throw SyntaxError("don't support it")
            i++

        }
        return if (times < lower_bound)
            false
        else
            match.isOnFinalState
    }

    // 匹配 +
    @Throws(SyntaxError::class)
    fun acceptOneOrMore(input: String): Boolean {
        val currentstate = match.currentStates!!.toTypedArray()[0] as Int
        var times = 0
        var is_match = false
        var i = 0
        while (i < input.length) {
            val judge: Int
            if (!is_match)
                judge = judgeFunction(3)
            else
                judge = judgeFunction(0)

            if (judge == 0) {
                if (!match.accept(input[i])) {
                    if (times < 1) {
                        break
                    } else {
                        i = i - 1
                        is_match = true
                        match.currentStates!!.clear()
                        match.currentStates!!.add(match.stateChart.connections[currentstate].get("++").toTypedArray()[0] as Int)
                    }
                } else {
                    if (!is_match) {
                        times++
                        if (times >= input.length) {
                            break
                        } else {
                            match.currentStates!!.clear()
                            match.currentStates!!.add(currentstate)
                            //                            i--;
                        }
                    }
                }
            } else if (judge == 1) {
                if (!is_match && !acceptZeroOrMore(input.substring(i))) {
                    break
                } else
                    throw SyntaxError("Invalid syntax.")
            } else if (judge == 2) {
                if (!is_match && !acceptZeroOrOne(input.substring(i))) {
                    break
                } else
                    throw SyntaxError("Invalid syntax.")
            } else if (judge == 3) {
                if (!is_match && !acceptOneOrMore(input.substring(i))) {
                    break
                } else
                    throw SyntaxError("Invalid syntax.")
            } else if (judge == 4) {
                val capture = match.captureString(input.substring(i))
                if (capture.containsKey(false)) {
                    if (times < 1) {
                        break
                    } else {
                        i = i - 1
                        is_match = true
                        match.currentStates!!.clear()
                        match.currentStates!!.add(match.stateChart.connections[currentstate].get("++").toTypedArray()[0] as Int)
                    }
                } else {
                    captures.add(input.substring(i, i + capture[true]))
                    i = i + capture[true] - 1
                    if (!is_match) {
                        times++
                        if (times >= input.length) {
                            break
                        } else {
                            match.currentStates!!.clear()
                            match.currentStates!!.add(currentstate)
                        }
                    }
                }

            } else if (judge == 5) {
                if (is_match) {
                    acceptOneCharRange(input.substring(i))
                    break
                } else
                    throw SyntaxError("Invalid syntax")
            } else
                throw SyntaxError("don't support it")
            i++

        }
        return match.isOnFinalState
    }

    // 匹配 *
    @Throws(SyntaxError::class)
    fun acceptZeroOrMore(input: String): Boolean {
        val currentstate = match.currentStates!!.toTypedArray()[0] as Int
        var is_match = false
        var times = 0
        var i = 0
        while (i < input.length) {
            val judge: Int
            if (!is_match)
                judge = judgeFunction(1)
            else
                judge = judgeFunction(0)

            if (judge == 0) {
                if (!match.accept(input[i])) {
                    i = i - 1
                    is_match = true
                    match.currentStates!!.clear()
                    match.currentStates!!.add(match.stateChart.connections[currentstate].get("**").toTypedArray()[0] as Int)
                } else {
                    times++
                    if (!is_match) {
                        if (times >= input.length) {
                            break
                        } else {
                            match.currentStates!!.clear()
                            match.currentStates!!.add(currentstate)
                            //                            i--;
                        }
                    }
                }
            } else if (judge == 1) {
                if (!is_match && !acceptZeroOrMore(input.substring(i))) {
                    break
                } else
                    throw SyntaxError("Invalid syntax.")
            } else if (judge == 2) {
                if (!is_match && !acceptZeroOrOne(input.substring(i))) {
                    break
                } else
                    throw SyntaxError("Invalid syntax.")
            } else if (judge == 3) {
                if (!is_match && !acceptOneOrMore(input.substring(i))) {
                    break
                } else
                    throw SyntaxError("Invalid syntax.")
            } else if (judge == 4) {
                val capture = match.captureString(input.substring(i))
                if (capture.containsKey(false)) {
                    i = i - 1
                    is_match = true
                    match.currentStates!!.clear()
                    match.currentStates!!.add(match.stateChart.connections[currentstate].get("()").toTypedArray()[0] as Int)
                } else {
                    captures.add(input.substring(i, i + capture[true]))
                    i = i + capture[true] - 1
                    if (!is_match) {
                        times++
                        if (times >= input.length) {
                            break
                        } else {
                            match.currentStates!!.clear()
                            match.currentStates!!.add(currentstate)
                            //                            i--;
                        }
                    }
                }
            } else if (judge == 5) {
                if (is_match) {
                    acceptOneCharRange(input.substring(i))
                    break
                } else
                    throw SyntaxError("Invalid syntax")
            } else
                throw SyntaxError("don't support it")
            i++

        }
        return match.isOnFinalState
    }
}
