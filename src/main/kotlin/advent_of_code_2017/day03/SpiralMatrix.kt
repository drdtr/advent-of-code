package advent_of_code_2017.day03

import Util.readInputLines
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * [Spiral Memory](https://adventofcode.com/2017/day/3)
 *
 * Given a spiral matrix, starting in the center, find the Manhattan distance to a given cell `n`.
 *
 * ### Part 1
 *
 * The matrix cells are filled with positive integers, starting at 1:
 * ```
 * 37  36  35  34  33  32  31
 * 38  17  16  15  14  13  30
 * 39  18   5   4   3  12  29
 * 40  19   6   1   2  11  28
 * 41  20   7   8   9  10  27
 * 42  21  22  23  24  25  26
 * 43  44  45  46  47---> ...
 * ```
 * - Distances
 *   - `1` - distance is 0
 *   - `12` - distance is 3
 *   - `23` - distance is 2
 *   - `1024` - distance is 31
 *
 */
class SpiralMatrix {
    data class Point(val x: Int, val y: Int)

    fun calcManhattanDistance(n: Int): Int = calcCellCoordinates(n).let { (x, y) -> abs(x) + abs(y) }

    fun calcCellCoordinates(n: Int): Point {
        require(n > 0) { "Cell value must be positive" }
        if (n == 1) return Point(0, 0)
        val sq = sqrt(n.toDouble()).toInt()
        var r = n - sq * sq
        var x: Int
        var y: Int
        if (sq % 2 == 0) {
            x = -(sq / 2 - 1)
            y = sq / 2
            if (r > 0) {
                // step one to the left, into the next spiral
                x--
                r--
            }
            if (r > 0) {
                val stepsDown = r.coerceAtMost(sq)
                y -= stepsDown
                r -= stepsDown
            }
            if (r > 0) {
                val stepsRight = r.coerceAtMost(sq)
                x += stepsRight
                r -= stepsRight
            }
        } else {
            x = sq / 2
            y = -sq / 2
            if (r > 0) {
                // step one to the right, into the next spiral
                x++
                r--
            }
            if (r > 0) {
                val stepsUp = r.coerceAtMost(sq)
                y += stepsUp
                r -= stepsUp
            }
            if (r > 0) {
                val stepsLeft = r.coerceAtMost(sq)
                x -= stepsLeft
                r -= stepsLeft
            }
        }
        if (r > 0) error("Wrong spiral - this should never happen.")
        return Point(x, y)
    }
}

private fun readInput(inputFileName: String): Int =
    readInputLines(2017, 3, inputFileName)[0].toInt()

private fun printResult(inputFileName: String) {
    val n = readInput(inputFileName)
    val solver = SpiralMatrix()

    // part 1
    val res1 = solver.calcManhattanDistance(n)
    println("Manhattan distance to cell $n: $res1")
}

fun main() {
    printResult("input.txt")
}
