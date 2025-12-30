package advent_of_code_2018.day11

import Util.readInputLines

/**
 * [Chronal Charge](https://adventofcode.com/2018/day/11)
 *
 * You're given an `300*300` grid, where each cell's value is determined as follows:
 * - `v1 = x + 10`, where `x` is the cell's x coordinate
 * - `v2 = v1 * y`, where `y` is the cell's y coordinates
 * - `v3 = v2 + k`, where `k` is the puzzle's input
 * - `v4 = v3 * v1`
 * - `v5 = the hundreds digit of v4`, e.g. `3` in `12345`.
 * - `v = v5 - 5`
 *
 * Example:
 * - Cell `(3, 5)`, `k = 8`
 * - `v1 = 3 + 10 = 13`
 * - `v2 = 13 * 5 = 65`
 * - `v3 = 65 + 8 = 73`
 * - `v4 = 73 * 13 = 949`
 * - `v5 = 9`
 * - `v = 9 - 5 = 4`
 *
 * More examples
 * - `v(122,79) = -5`
 * - `v(217,196) = 0`
 * - `v(101,153) = 4`
 *
 * ### Part 1
 * Find the top-left coordinates of the `3*3` sub-square that has the largest sum.
 *
 * Example:
 * - `k = 18`: max sum is 29 in the 3*3 square with top-left corner `(32,44)`:
 * ```
 * -2  -4   4   4   4
 * -4   4   4   4  -5
 *  4   3   3   4  -4
 *  1   1   2   4  -3
 * -1   0   2  -5  -2
 * ```
 * - `k = 42`: max sum is 30 in the 3*3 square with top-left corner `(20,60)`:
 * ```
 * -3   4   2   2   2
 * -4   4   3   3   4
 * -5   3   3   4  -4
 *  4   3   3   4  -3
 *  3   3   3  -5  -1
 * ```
 *
 * ### Part 2
 * Find the top-left coordinates of the `m*m` sub-square, where `0<m<300`, which has the largest sum.
 *
 * Example:
 * - `k = 18`: max sum is 113 in the 16*16 square with top-left corner `(90,269)`.
 * - `k = 42`: max sum is 119 in the 12*12 square with top-left corner `(232,251)`.
 *
 */
class MaxSquareSum {
    data class MaxSquareSumResult(val sum: Int, val x: Int, val y: Int)

    fun findMaxSumOf3x3Square(gridWidth: Int, gridHeight: Int, k: Int): MaxSquareSumResult {
        val grid = Array(gridHeight) { y ->
            IntArray(gridWidth) { x ->
                val v1 = x + 10
                val v2 = v1 * y
                val v3 = v2 + k
                val v4 = v3 * v1
                val v5 = (v4 / 100) % 10
                v5 - 5
            }
        }
        var maxSum = Int.MIN_VALUE
        var maxX = 0
        var maxY = 0
        fun recordMaxIfExceeded(sum: Int, x: Int, y: Int) {
            if (maxSum < sum) {
                maxSum = sum
                maxX = x
                maxY = y
            }
        }
        for (y in 0 until gridHeight - 2) {
            var squareSum = grid[y][0] + grid[y][1] + grid[y][2] +
                    grid[y + 1][0] + grid[y + 1][1] + grid[y + 1][2] +
                    grid[y + 2][0] + grid[y + 2][1] + grid[y + 2][2]
            recordMaxIfExceeded(sum = squareSum, x = 0, y = y)
            for (x in 1 until gridWidth - 2) {
                // Move the square to the right
                squareSum = squareSum - (grid[y][x - 1] + grid[y + 1][x - 1] + grid[y + 2][x - 1]) +
                        grid[y][x + 2] + grid[y + 1][x + 2] + grid[y + 2][x + 2]
                recordMaxIfExceeded(sum = squareSum, x = x, y = y)
            }
        }
        return MaxSquareSumResult(sum = maxSum, x = maxX, y = maxY)
    }
}


private fun readInput(inputFileName: String): Int = readInputLines(2018, 11, inputFileName)[0].toInt()

private fun printResult(inputFileName: String) {
    val k = readInput(inputFileName)
    val solver = MaxSquareSum()

    // part 1
    val res1 = solver.findMaxSumOf3x3Square(gridWidth = 300, gridHeight = 300, k = k)
    println("Maximum 3*3 square sum: $res1")

    // part 2
    // TODO

}

fun main() {
    printResult("input.txt")
}
