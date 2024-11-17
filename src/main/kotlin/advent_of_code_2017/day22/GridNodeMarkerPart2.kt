package advent_of_code_2017.day22

import Util.readInputLines
import advent_of_code_2017.day22.GridNodeMarkerPart2.NodeState.*
import advent_of_code_2017.day22.GridStepDirection.*
import advent_of_code_2017.day22.MarkedNodeUtil.nodeMarkingStringsToBooleanArrays

/**
 * [Sporifica Virus - Part 2](https://adventofcode.com/2017/day/22)
 *
 * Now the marking procedure becomes more complicated:
 * - A node has 4 states:
 *    - Clean
 *    - Pre-marked
 *    - Marked
 *    - Flagged
 * - The cursor changes the node states cyclically in the order `Clean -> Pre-marked -> Marked -> Flagged -> Clean`.
 * - The cursor changes the direction depending on the node state:
 *    - Clean node: turn left
 *    - Pre-marked node: keep direction
 *    - Marked node: turn right
 *    - Flagged node: turn around
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
 * - The cursor turns left, pre-marks the clean node, and moves.
 * ```
 * . . . . . . . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * . . . . . # . . .
 * . . .[#]P . . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * ```
 * - The cursor turns right, flags the node, and moves.
 * ```
 * . . . . . . . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * . . .[.]. # . . .
 * . . . F P . . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * ```
 * - After another 3 moves, the grid is:
 * ```
 * . . . . . . . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * . . P P . # . . .
 * . . P[F]P . . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * ```
 * - The cursor now turns around, cleans the flagged node, and moves.
 * ```
 * . . . . . . . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * . . P P . # . . .
 * . .[P]. P . . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * ```
 * - The pre-marked node now gets marked, and the cursor keeps the direction.
 * ```
 * . . . . . . . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * . . P P . # . . .
 * .[.]# . P . . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * . . . . . . . . .
 * ```
 *
 * - In 100 moves, there were 26 moves that marked a node.
 * - In 10_000_000 moves, there were 2_511_944 moves that marked a node.
 */
class GridNodeMarkerPart2 {
    data class Point(val x: Int, val y: Int)
    enum class NodeState {
        Premarked, Marked, Flagged;
    }

    data class MarkingOutcome(val nodeStates: Map<Point, NodeState>, val markedCount: Int)

    /**
     * The returned list of points is normalized so that the minimum `x` and `y` coordinates of any point is 0.
     */
    fun markAndCount(grid: List<List<Boolean>>, numOfMoves: Int = 1): MarkingOutcome {
        val nodeStates = hashMapOf<Point, NodeState>()
        for (y in grid.indices) {
            val row = grid[y]
            for (x in row.indices) {
                if (row[x]) nodeStates[Point(x, y)] = Marked
            }
        }

        var markedCount = 0
        // Cursor position and direction
        var y = grid.size / 2
        var x = grid[0].size / 2
        var dir = Up
        repeat(numOfMoves) { i ->
            val p = Point(x, y)
            val nodeState = nodeStates[p]
            when (nodeState) {
                null      -> {
                    nodeStates[p] = Premarked
                    dir = dir.afterLeftTurn()
                }

                Premarked -> {
                    nodeStates[p] = Marked
                    markedCount++
                }

                Marked    -> {
                    nodeStates[p] = Flagged
                    dir = dir.afterRightTurn()
                }

                Flagged   -> {
                    nodeStates -= p
                    dir = dir.afterTurnaround()
                }
            }
            x += dir.dx
            y += dir.dy
        }

        val minX = nodeStates.keys.minOf { it.x }
        val minY = nodeStates.keys.minOf { it.y }
        val markedNodesNormalized = nodeStates.mapKeys { (p, _) -> Point(p.x - minX, p.y - minY) }
        return MarkingOutcome(markedNodesNormalized, markedCount)
    }
}

private fun readInput(inputFileName: String): List<String> =
    readInputLines(2017, 22, inputFileName)

private fun printResult(inputFileName: String) {
    val nodeMarkingStrings = readInput(inputFileName)
    val grid: List<List<Boolean>> = nodeMarkingStringsToBooleanArrays(nodeMarkingStrings)
//    println(nodeMarkingStrings.joinToString(separator = "\n"))

    val solver = GridNodeMarkerPart2()
    val numOfMoves = 10_000_000
    val res = solver.markAndCount(grid, numOfMoves)
    println("Number of marked nodes after $numOfMoves moves: ${res.markedCount}")

}

fun main() {
    printResult("input.txt")
}
