package advent_of_code_2015.day25

import Util.readInputLines

/**
 * [Let It Snow](https://adventofcode.com/2015/day/25)
 *
 * A diagnoal matrix is calculated in the following order:
 * ```
 *    | 1   2   3   4   5   6   7
 * ---+---+---+---+---+---+---+---+
 * 1 |  1   3   6  10  15  21  28
 * 2 |  2   5   9  14  20  27
 * 3 |  4   8  13  19  26
 * 4 |  7  12  18  25
 * 5 | 11  17  24
 * 6 | 16  23
 * 7 | 22
 * ```
 *
 * For a cell value `n`, the next value is calculated as `n * 252533 % 33554393`.
 * The calculation starts with the value `20151125` in the cell `(1, 1)`.
 * For example, the first 6 rows and columns are:
 * ```
 * |    1         2         3         4         5         6
 * ---+---------+---------+---------+---------+---------+---------+
 * 1 | 20151125  18749137  17289845  30943339  10071777  33511524
 * 2 | 31916031  21629792  16929656   7726640  15514188   4041754
 * 3 | 16080970   8057251   1601130   7981243  11661866  16474243
 * 4 | 24592653  32451966  21345942   9380097  10600672  31527494
 * 5 |    77061  17552253  28094349   6899651   9250759  31663883
 * 6 | 33071741   6796745  25397450  24659492   1534922  27995004
 * ```
 *
 * For a given cell `(row, col)`, calculate the value in it.
 */
class DiagonalMatrix {
    fun calcDiagonally(row: Int, col: Int, startingValue: Long = 20151125): Int {
        var r = 1
        var c = 1
        var n = startingValue
        while (!(r == row && c == col)) {
            n = n * 252533 % 33554393
            r--
            c++
            if (r == 0) {
                r = c
                c = 1
            }
        }
        return n.toInt()
    }
}

private fun readInput(inputFileName: String): Pair<Int, Int> {
    val (row, col) = readInputLines(2015, 25, inputFileName)[0]
            .split(" ", ",", ".")
            .map { it.toIntOrNull() }
            .filterNotNull()
    return row to col
}

private fun printResult(inputFileName: String) {
    val (row, col) = readInput(inputFileName)
    val solver = DiagonalMatrix()
    val res = solver.calcDiagonally(row, col)
    println("Value in cell ($row, $col): $res")
}

fun main() {
    printResult("input.txt")
}

