package advent_of_code_2017.day22

import Util.readInputLines
import advent_of_code_2017.day22.GridStepDirection.*
import advent_of_code_2017.day22.MarkedNodeUtil.nodeMarkingStringsToBooleanArrays

/**
 * [Sporifica Virus - Part 1](https://adventofcode.com/2017/day/22)
 *
 * You're given an infinite grid, where each not is marked or empty, a cursor starts in the center and moves
 * depending on the current node `(x, y)`:
 * - If `(x, y)` is marked, then the cursor turns right, otherwise it turns left.
 * - If the current node is empty, then it's marked, otherwise it's cleared.
 * - Then the cursor moves in the direction it's facing.
 *
 * The cursor starts in the middle, facing up. Given an input grid, count how many moves mark a node.
 *
 * Example:
 * - You're given the grid:
 * ```
 * ..#
 * #..
 * ...
 * ```
 * - The cursor then starts in the middle of the infinite grid:
 * ```
 * . . . . . . . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * . . . . . # . . .
 * . . . #[.]. . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * ```
 * - The cursor turns left, marks the clean node, and moves.
 * ```
 * . . . . . . . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * . . . . . # . . .
 * . . .[#]# . . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * ```
 * - The cursor turns right, clears the node, and moves.
 * ```
 * . . . . . . . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * . . .[.]. # . . .
 * . . . . # . . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * ```
 * - In the next 4 moves, the cursor only encounters clean nodes, so it turns left each time, marks them, and moves.
 * ```
 * . . . . . . . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * . . #[#]. # . . .
 * . . # # # . . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * ```
 * - In the marked node, the cursor turns right, clears the node, and moves.
 * ```
 * . . . . . . . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * . . # .[.]# . . .
 * . . # # # . . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * ```
 * - In the 7 moves above, there were 5 moves that marked a node.
 * - After a total of 70 moves, the grid will look like this, with the cursor facing up:
 * ```
 * . . . . . # # . .
 * . . . . # . . # .
 * . . . # . . . . #
 * . . # . #[.]. . #
 * . . # . # . . # .
 * . . . . . # # . .
 * . . . . . . . . .
 * . . . . . . . . .
 * ```
 * - By this time, there were 41 moves that marked a node.
 * - After a total of 10000 moves, there were 5587 moves that marked a node.
 *
 *
 *
 */
class GridNodeMarkerPart1 {
    data class Point(val x: Int, val y: Int)
    data class MarkingOutcome(val markedNodes: Set<Point>, val markedCount: Int)

    /**
     * The returned list of points is normalized so that the minimum `x` and `y` coordinates of any point is 0.
     */
    fun markAndCount(grid: List<List<Boolean>>, numOfMoves: Int = 1): MarkingOutcome {
        val markedNodes = hashSetOf<Point>()
        for (y in grid.indices) {
            val row = grid[y]
            for (x in row.indices) {
                if (row[x]) markedNodes += Point(x, y)
            }
        }

        var markedCount = 0
        // Cursor position and direction
        var y = grid.size / 2
        var x = grid[0].size / 2
        var dir = Up
        repeat(numOfMoves) { i ->
            val p = Point(x, y)
            if (p in markedNodes) {
                markedNodes -= p
                dir = dir.afterRightTurn()
            } else {
                markedNodes += p
                markedCount++
                dir = dir.afterLeftTurn()
            }
            x += dir.dx
            y += dir.dy
        }

        val minX = markedNodes.minOf { it.x }
        val minY = markedNodes.minOf { it.y }
        val markedNodesNormalized = markedNodes.map { p -> Point(p.x - minX, p.y - minY) }.toSet()
        return MarkingOutcome(markedNodesNormalized, markedCount)
    }
}

private fun readInput(inputFileName: String): List<String> =
    readInputLines(2017, 22, inputFileName)

private fun printResult(inputFileName: String) {
    val nodeMarkingStrings = readInput(inputFileName)
    val grid: List<List<Boolean>> = nodeMarkingStringsToBooleanArrays(nodeMarkingStrings)
//    println(nodeMarkingStrings.joinToString(separator = "\n"))

    val solver = GridNodeMarkerPart1()
    val numOfMoves = 10_000
    val res = solver.markAndCount(grid, numOfMoves)
    println("Number of marked nodes after $numOfMoves moves: ${res.markedCount}")

}

fun main() {
    printResult("input.txt")
}
