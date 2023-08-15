package advent_of_code_2016.day08

import Util.readInputLines
import advent_of_code_2016.day08.MatrixPixelShift.ShiftOp
import advent_of_code_2016.day08.MatrixPixelShift.ShiftOp.*
import advent_of_code_2016.day08.MatrixPixelShift.apply
import advent_of_code_2016.day08.MatrixPixelShift.createMatrix
import advent_of_code_2016.day08.MatrixPixelShift.sum
import java.util.regex.Pattern

/**
 * [Two-Factor Authentication](https://adventofcode.com/2016/day/8)
 *
 * Given a `n * m` matrix of pixels, which can be on or off, following operations are possible:
 * - `fillRect(x, y)` - turn on all pixels in the rectangle starting at the top left corner `(0, 0)`
 * and ending at `(y - 1, x - 1)`
 * - `shiftDown(x, k)` - shift all pixels in the column `x` down by `k` pixels, rotating them around the
 * margin, i.e. when a pixel goes below the bottom row, it reappears in the top row.
 * - `shiftRight(y, k)` - shift all pixels in the row `y` to the right by `k` pixels, rotating them around the
 * margin, i.e. when a pixel goes beyond the right-most column, it reappears in the left-top column.
 *
 * For example, in a `3 x 7` matrix:
 * - `fillRect 3x2` creates a small rectangle in the top-left corner:
 * ```
 *   ###....
 *   ###....
 *   .......
 *```
 * - `shiftDown(1, 1)` shifts the seconds column down by one pixel:
 * ```
 *   #.#....
 *   ###....
 *   .#.....
 *```
 * - `shiftRight(0, 4)` shifts the top row to the right by 4 pixels:
 * ```
 *   ....#.#
 *   ###....
 *   .#.....
 *```
 * - `shiftDown(1, 1)` again shifts the seconds column down by one pixel:
 * ```
 *   .#..#.#
 *   #.#....
 *   .#.....
 *```
 *
 * Calculate the resulting matrix after a given list of operations and the number of turned on pixels in it.
 */
object MatrixPixelShift {
    sealed interface ShiftOp {
        data class FillRect(val x: Int, val y: Int) : ShiftOp
        data class ShiftDown(val x: Int, val k: Int) : ShiftOp
        data class ShiftRight(val y: Int, val k: Int) : ShiftOp
    }

    @JvmInline
    value class Matrix(val matrix: Array<BooleanArray>)

    fun createMatrix(m: Int, n: Int) = Matrix(Array(m) { BooleanArray(n) })

    fun Matrix.apply(ops: Iterable<ShiftOp>) = ops.forEach { apply(it) }

    fun Matrix.sum() = matrix.sumOf { row -> row.count { it } }

    private fun Matrix.apply(op: ShiftOp) = when (op) {
        is FillRect   -> fillRect(op.x, op.y)
        is ShiftDown  -> shiftDown(op.x, op.k)
        is ShiftRight -> shiftRight(op.y, op.k)
    }

    private fun Matrix.fillRect(x: Int, y: Int) {
        for (row in 0 until y) {
            for (col in 0 until x) {
                matrix[row][col] = true
            }
        }
    }

    private fun Matrix.shiftDown(x: Int, k: Int) {
        val m = matrix.size
        val col = BooleanArray(m) { matrix[it][x] }
        for (y in 0 until m) {
            matrix[(y + k) % m][x] = col[y]
        }
    }

    private fun Matrix.shiftRight(y: Int, k: Int) {
        val n = matrix[y].size
        val row = BooleanArray(n) { matrix[y][it] }
        for (x in 0 until n) {
            matrix[y][(x + k) % n] = row[x]
        }
    }
}

private object ShiftOpInputPatterns {
    val fillRect = Pattern.compile("rect (\\d+)x(\\d+)")
    val shiftDown = Pattern.compile("rotate column x=(\\d+) by (\\d+)")
    val shiftRight = Pattern.compile("rotate row y=(\\d+) by (\\d+)")
}

private fun parseShiftOp(s: String): ShiftOp {
    with(ShiftOpInputPatterns.fillRect.matcher(s)) {
        if (matches()) return FillRect(group(1).toInt(), group(2).toInt())
    }
    with(ShiftOpInputPatterns.shiftDown.matcher(s)) {
        if (matches()) return ShiftDown(group(1).toInt(), group(2).toInt())
    }
    with(ShiftOpInputPatterns.shiftRight.matcher(s)) {
        if (matches()) return ShiftRight(group(1).toInt(), group(2).toInt())
    }
    error("Invalid shift operation $s")
}

private fun readInput(inputFileName: String): List<ShiftOp> =
    readInputLines(2016, 8, inputFileName).map { parseShiftOp(it) }

private fun printResult(inputFileName: String, matrixSize: Pair<Int, Int>) {
    val ops = readInput(inputFileName)
    val (m, n) = matrixSize
    val matrix = createMatrix(m, n)
    matrix.apply(ops)
    val res = matrix.sum()
    println("Part 1: number of turned on pixels: $res")

    val pixelChar = '\u2588' // Full block, see https://en.wikipedia.org/wiki/Block_Elements
    val matrixWithLinebreaks =
        matrix.matrix.joinToString(separator = "\n") { row ->
            row.map { if (it) pixelChar else ' ' }.joinToString(separator = " ")
        }
    println("Part 2: resulting matrix:\n$matrixWithLinebreaks")
}

fun main() {
    printResult("input.txt", 6 to 50)
}

