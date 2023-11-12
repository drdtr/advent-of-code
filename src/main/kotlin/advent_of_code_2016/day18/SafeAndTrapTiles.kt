package advent_of_code_2016.day18

import Util.readInputLines

/**
 * [Like a Rogue](https://adventofcode.com/2016/day/18)
 *
 * You're given the first row of tiles in a `m * n` grid.
 * The characters `.` and `^` denote _safe_ or _trap_ tiles, respectively.
 * Compute the remaining `n - 1` rows and count the number of safe tiles.
 * For a row `i + 1`, each tile `j` is computed as follows:
 * - the tile at `(j, i + 1)` is a _trap_ if
 *    - the tiles at `(j - 1, i)` and `(j, i)` are traps, but the tile at `(j + 1, i)` is safe
 *    - the tiles at `(j + 1, i)` and `(j, i)` are traps, but the tile at `(j - 1, i)` is safe
 *    - only the tile at `(j - 1, i)` is a trap
 *    - only the tile at `(j + 1, i)` is a trap
 * - note, that a non-existent tile at `(-1, i)` or `(n, i)` is considered safe.
 *
 * When representing a safe tile as `true` and a trap tile as `false, this calculation translates to:
 * ```
 *     !left && !center && right
 *  || !left && center && right
 *  || left && !center && !right
 *  || left && center && !right
 *  =  !left && right
 *  || left && !right
 *  =  left ^ right
 * ```
 * i.e. a tile is a _trap_ if `left ^ right` and it's _safe_ if `left == right`
 *
 * Examples:
 * - `5*3` with 6 safe tiles
 * ```
 * ..^^.
 * .^^^^
 * ^^..^
 * ```
 * - `10*10` with 38 safe tiles
 * ```
 * .^^.^.^^^^
 * ^^^...^..^
 * ^.^^.^.^^.
 * ..^^...^^^
 * .^^^^.^^.^
 * ^^..^.^^..
 * ^^^^..^^^.
 * ^..^^^^.^^
 * .^^^..^.^^
 * ^^.^^^..^^
 * ```
 *
 */
class SafeAndTrapTiles {
    fun countSafeTiles(n: Int, startingRow: String): Int {
        val m = startingRow.length
        var currRowSafeTiles = startingRow.map(::denotesSafeTile).toBooleanArray()
        var nextRowSafeTiles = BooleanArray(m)
        var numOfSafeTiles = currRowSafeTiles.count { it }

        fun swapRows() {
            val t = currRowSafeTiles
            currRowSafeTiles = nextRowSafeTiles
            nextRowSafeTiles = t
        }

        fun calcNextRow() {
            for (i in currRowSafeTiles.indices) {
                val left = currRowSafeTiles.leftFrom(i)
                val right = currRowSafeTiles.rightFrom(i)
                nextRowSafeTiles[i] = left == right
            }
        }

        repeat(n - 1) {
            calcNextRow()
            swapRows()
            numOfSafeTiles += currRowSafeTiles.count { it }
        }

        return numOfSafeTiles
    }

    private fun BooleanArray.leftFrom(i: Int) = if (i > 0) get(i - 1) else true

    private fun BooleanArray.rightFrom(i: Int) = if (i < size - 1) get(i + 1) else true

    private fun denotesSafeTile(ch: Char) = when (ch) {
        '.'  -> true
        '^'  -> false
        else -> error("Unexpected character $ch")
    }
}

private fun readInput(inputFileName: String): String =
    readInputLines(2016, 18, inputFileName)[0]

private fun printResult(inputFileName: String) {
    val startingRow = readInput(inputFileName)
    val m = startingRow.length
    val solver = SafeAndTrapTiles()
    // part 1
    val n1 = 40
    val res1 = solver.countSafeTiles(n1, startingRow)
    println("Number of safe tiles in a $m * $n1 grid starting with the row $startingRow: $res1")
    // part 2
    val n2 = 400_000
    val res2 = solver.countSafeTiles(n2, startingRow)
    println("Number of safe tiles in a $m * $n2 grid starting with the row $startingRow: $res2")
}

fun main() {
    printResult("input.txt")
}

