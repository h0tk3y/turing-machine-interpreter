package ru.ifmo.ctddev.igushkin.turing

import java.util.*

/**
 * Created by Sergey on 24.12.2014.
 */

enum class Movement { Left, Right, Stay }

class Rule(val fromState: String,
           val fromChar: String,
           val toState: String,
           val toChar: String,
           val move: Movement)

class TuringMachine(val startState: String,
                    val acceptState: String,
                    val rejectState: String,
                    val blankChar: String,
                    val rules: List<Rule>) {

    private val rulesMap: HashMap<String, HashMap<String, Rule>> = HashMap();

    init {
        for (r in rules) {
            if (r.fromState !in rulesMap)
                rulesMap.put(r.fromState, HashMap())
            if (r.fromChar in rulesMap[r.fromState]!!)
                throw IllegalArgumentException("Multiple rules for ${r.fromState} ${r.fromChar}.")
            rulesMap[r.fromState]!![r.fromChar] = r
        }
    }

    private val ensureStep = 10;

    class Snapshot(val state: String, val pos: Int, val tape: Array<String>, val machine: TuringMachine)

    fun process(tape: Array<String>, startPos: Int = 0, callback: (Snapshot) -> Unit = {}): Pair<Boolean, Array<String>> {
        var t = tape
        var pos = startPos

        var ensure = { i: Int ->
            if (i < t.indices.start) {
                t = Array(t.size + ensureStep,
                          { i -> if (i < ensureStep) blankChar else t[i - ensureStep] })
                pos += ensureStep
            } else if (i > t.indices.endInclusive)
                t = Array(t.size + ensureStep,
                          { i -> if (i >= t.size) blankChar else t[i] })
        }

        var state = startState
        while (state != acceptState && state != rejectState) {
            callback(Snapshot(state, pos, t, this))
            if (state in rulesMap && t[pos] in rulesMap[state]!!) {
                var r = rulesMap[state]!![t[pos]]!!
                state = r.toState
                t[pos] = r.toChar
                pos += when (r.move) { Movement.Right -> 1; Movement.Left -> -1; else -> 0 }
                ensure(pos)
            } else
                state = rejectState
        }
        callback(Snapshot(state, pos, t, this))
        return Pair(state == acceptState, tape)
    }
}