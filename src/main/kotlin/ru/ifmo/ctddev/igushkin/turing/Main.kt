package ru.ifmo.ctddev.igushkin.turing

import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.*

/**
 * Created by Sergey on 24.12.2014.
 */

fun printSnapshot(snapshot: TuringMachine.Snapshot) {
    var posInStr = 0;
    val p = snapshot.machine.rules.maxBy { r -> Math.max(r.fromState.length, r.toState.length) }
    val padding = Math.max(p!!.fromState.length, p.toState.length) + 1
    var chars = padding
    System.out.print(java.lang.String.format("%-${padding}s", snapshot.state))
    var onlyBlank = true;
    for (i in snapshot.tape.indices) {
        val c = snapshot.tape[i];
        onlyBlank = onlyBlank && snapshot.machine.blankChar == c && i != snapshot.pos;
        if (!onlyBlank) {
            if (i == snapshot.pos)
                posInStr = chars;
            chars += snapshot.tape[i].length + 1
            System.out.print(snapshot.tape[i] + " ")
        }
    }
    System.out.println("\n${String(CharArray(if (snapshot.pos < snapshot.tape.size) posInStr else chars + 1)).replace(0.toChar(), ' ')}^")
    if (haltEveryStep)
        System.`in`.read()
}

var haltEveryStep = false;

fun main(args: Array<String>) {
    if ("halt" in args) haltEveryStep = true

    var br = BufferedReader(InputStreamReader(FileInputStream("machine.in")))
    var line = br.readLine()?.split(' ')
    var startState = line!![1]
    line = br.readLine()?.split(' ')
    var acceptState = line!![1]
    line = br.readLine()?.split(' ')
    var rejectState = line!![1]
    line = br.readLine()?.split(' ')
    var blank = line!![1]
    line = br.readLine()?.split(' ')
    var rules = ArrayList<Rule>()
    while (line != null) {
        if (line.size != 1) {
            var fromState = line[0]
            var fromChar = line[1]
            var toState = line[3]
            var toChar = line[4]
            var movement = when (line[5]) {"<" -> Movement.Left; ">" -> Movement.Right; else -> Movement.Stay
            }
            rules.add(Rule(fromState, fromChar, toState, toChar, movement))
        }
        line = br.readLine()?.split(' ')
    }
    br.close()
    var machine = TuringMachine(startState, acceptState, rejectState, blank, rules)
    br = BufferedReader(InputStreamReader(FileInputStream("tape.in")))
    var tape = br.readLine()?.split("\\s+".toRegex())
    br.close()
    if (tape != null)
        machine.process(tape.toTypedArray(), 0, ::printSnapshot)
}