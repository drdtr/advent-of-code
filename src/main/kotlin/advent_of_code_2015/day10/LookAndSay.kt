package advent_of_code_2015.day10

import Util.readInputLines

/**
 * [Elves Look, Elves Say](https://adventofcode.com/2015/day/10)
 *
 * A [look-and-say sequence](https://en.wikipedia.org/wiki/Look-and-say_sequence)
 * starts with a given number and the next element is computed by saying the digits of the current element.
 *
 * Example:
 * - `1` -> `11`
 * - `11` -> `21`
 * - `21` -> `1211`
 * - `1211` -> `111221`
 * - `111221` -> `312211`
 *
 * Starting with a given number `n0` apply the `count-and-say` operation `numReps == 40` times
 * and return the length of the result.
 *
 * Note: this is similar to [Count and Say](https://leetcode.com/problems/count-and-say/) on LeetCode.
 */
class LookAndSay {
    fun lookAndSay(n0: Int, numReps: Int): String {
        if (numReps <= 0) return n0.toString()

        var sb = StringBuilder(n0.toString())
        repeat(numReps) { sb = say(sb) }
        return sb.toString()
    }

    private fun say(sb: StringBuilder) = StringBuilder().apply {
        var lastCh = sb[0]
        var cnt = 1
        for (i in 1 until sb.length) {
            val ch = sb[i]
            if (ch == lastCh) {
                cnt++
            } else {
                append(cnt).append(lastCh)
                cnt = 1
                lastCh = ch
            }
        }
        append(cnt).append(lastCh)
    }
}

private data class Input(val n0: Int, val numReps: Int)

private fun readInput(inputFileName: String): List<Input> {
    val lines = readInputLines(2015, 10, inputFileName)
    return lines.map {
        it.split(" ").map { it.toInt() }.let { Input(it[0], it[1]) }
    }
}

private fun printResult(inputFileName: String) {
    readInput(inputFileName).forEach { (n0, numReps) ->
        val solver = LookAndSay()
        val res = solver.lookAndSay(n0, numReps)
        println(
            """
        Look-and-say of $n0 for $numReps times: 
          res=$res
          len=${res.length}
        """.trimIndent()
        )
    }
}

fun main() {
    printResult("input.txt")
}