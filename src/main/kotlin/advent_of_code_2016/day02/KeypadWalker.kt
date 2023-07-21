package advent_of_code_2016.day02

import Util.readInputLines

/**
 * [Bathroom Security](https://adventofcode.com/2016/day/2)
 *
 * You're given a 3x3 keypad with buttons `1..9` and a list of strings,
 * where each strings consists of instructions `U`, `D`, `L`, `R`, denoting a step up/down/left/right resp.
 * The steps in each string start at the previously reached button (or at `5` for the first string),
 * and each step moves in the denoted direction if possible, or skipped otherwise
 * (e.g. a `left` or `up` step from `1` would be skipped).
 *
 * ### Part 1
 *
 * The keypad is
 * ```
 * 1 2 3
 * 4 5 6
 * 7 8 9
 * ```
 *
 * For example:
 * - `ULL`: starts at `5`, moves up to `2`, moves left to `1`, ignores last `L` because it's not possible
 * - `RRDDD`: starts at `1`, moves right twice to `3`, moves down twice to `3`
 * and ignores the remaining `D` because it's not possible
 * - `LURDL`: starts at `9` and reaches `8`
 * - `UUUUD`: starts at `8` and reaches `5`
 *
 * The resulting string is `1985`.
 *
 * ### Part 2
 *
 * The keypad is
 * ```
 *     1
 *   2 3 4
 * 5 6 7 8 9
 *   A B C
 *     D
 * ```
 *
 * For example:
 * - `ULL`: starts at `5`, and stays there becase both `U` and `L` are not possible
 * - `RRDDD`: starts at `5`, moves right twice to `3`, moves down twice to `D`
 * and ignores the remaining `D` because it's not possible
 * - `LURDL`: starts at `D` and reaches `B`
 * - `UUUUD`: starts at `B` and reaches `3`
 *
 * The resulting string is `5DB3`.
 *
 */
class KeypadWalker {
    fun calculateCodeOnKeypad3_3_3(stepStrings: List<String>) =
        calculateCodeOnKeypad(stepStrings, keypad3_3_3, start = 1 to 1)

    fun calculateCodeOnKeypad1_3_5_3_1(stepStrings: List<String>) =
        calculateCodeOnKeypad(stepStrings, keypad1_3_5_3_1, start = 0 to 2)

    fun calculateCodeOnKeypad(
        stepStrings: List<String>,
        keypad: List<List<Char?>>,
        start: Pair<Int, Int>
    ): String {
        fun getFromKeypadIfValid(x: Int, y: Int): Char? =
            if (y in keypad.indices && x in keypad[y].indices) keypad[y][x] else null

        var (x, y) = start
        return buildString {
            for (steps in stepStrings) {
                for (step in steps) {
                    val (dx, dy) = stepDiffs[step] ?: error("Invalid direction $step")
                    val newX = x + dx
                    val newY = y + dy
                    getFromKeypadIfValid(newX, newY)?.let {
                        x = newX
                        y = newY
                    }
                }
                append(keypad[y][x])
            }
        }
    }

    companion object {
        /** A 3x3 keypad with digits `1..9` in 3 lines. */
        val keypad3_3_3 = listOf(
            listOf('1', '2', '3'),
            listOf('4', '5', '6'),
            listOf('7', '8', '9'),
        )

        /**
         * A 5x5 keypad with digits `1..9` and letters `A..D` in 5 lines,
         * where the lines have lengths 1, 3, 5, 3, and 1.
         */
        val keypad1_3_5_3_1 = listOf(
            listOf(null, null, '1', null, null),
            listOf(null, '2', '3', '4', null),
            listOf('5', '6', '7', '8', '9'),
            listOf(null, 'A', 'B', 'C', null),
            listOf(null, null, 'D', null, null),
        )

        private val stepDiffs = mapOf(
            'L' to (-1 to 0),
            'R' to (1 to 0),
            'U' to (0 to -1),
            'D' to (0 to 1),
        )
    }
}

private fun readInput(inputFileName: String): List<String> =
    readInputLines(2016, 2, inputFileName)

private fun printResult(inputFileName: String) {
    val steps = readInput(inputFileName)
    val solver = KeypadWalker()
    println("Calculated code on 3_3_3 keypad: ${solver.calculateCodeOnKeypad3_3_3(steps)}")
    println("Calculated code on 1_3_5_3_1 keypad: ${solver.calculateCodeOnKeypad1_3_5_3_1(steps)}")
}

fun main() {
    printResult("input.txt")
}