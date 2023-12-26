package advent_of_code_2016.day22

import Util.readInputLines
import advent_of_code_2016.day22.GridComputing.*
import java.util.ArrayDeque
import java.util.regex.Pattern

/**
 * [Grid Computing](https://adventofcode.com/2016/day/22)
 *
 * You're given a matrix `m * n` of nodes containing tuples `(size, used, avail)`
 * representing the memory size and the used vs. available memory in each node.
 *
 * ### Part 1
 *
 * Count the number of **viable pairs** of node, i.e., `(A,B)` such that:
 * - `A` is not empty, i.e. `used > 0`
 * - `A` and `B` are not the same node
 * - The data on node `A` would fit on node `B`, i.e., `A.used <= B.avail`
 *
 *
 * ### Part 2
 *
 * Given a source node `s` and a destination node `d`, you need to move the data from `s` to `d`
 * performing following operation: you can move the complete data from a node `a` to one neighbor node `b`
 * (above, below, left, or right) if `b.avail >= a.used`.
 *
 * Find the minimum number of such steps to move the data from `s` to `d`.
 *
 * #### Idea
 *
 * As explained [here](https://markheath.net/post/aoc-2016-day22),
 * the input has the pattern that there are 3 types of nodes:
 * - nodes with a lot of used data that are effectively walls and can't be moved
 * - an empty node that can take the data from most of the nodes
 * - nodes with data that would fit into the empty node and can thus be moved
 *
 * Thus, we can simplify the grid to use these 3 types of nodes.
 * The solution idea is then to perform two phases:
 * 1) move the empty node to the source node - more exactly, put the empty node to the neighbor of the source node,
 * which needs to be swapped on the way to the target node
 * 2) move the source node data to the target node. Because after each swap the empty node is on the other side
 * from the moved node, we need to make 4 more steps to move it back into the path of the source node data.
 */
class GridComputing {
    data class Node(val x: Int, val y: Int, val used: Int, val avail: Int)
    data class Point(val x: Int, val y: Int)

    fun countViablePairs(nodes: Collection<Node>): Int {
        val nodesSortedByUsed = nodes.sortedBy { it.used }
        val availSorted = nodes.map { it.avail }.toIntArray().apply { sort() }
        var idxAvail = 0

        var count = 0
        for (a in nodesSortedByUsed) {
            if (a.used <= 0) continue

            while (idxAvail < availSorted.size && availSorted[idxAvail] < a.used) {
                idxAvail++
            }
            if (idxAvail == availSorted.size) break

            count += availSorted.size - idxAvail
            if (a.avail >= availSorted[idxAvail]) {
                // the node `A` is contained in the rest of the availSorted array,
                // so we counted it and now subtract it again from the counter
                count--
            }
        }
        return count
    }


    fun minStepsToMoveDataFromSourceToTarget(srcPoint: Point, destPoint: Point, nodes: Collection<Node>): Int {
        val grid = nodesToGrid(nodes)
        println(grid.joinToString(separator = "\n") { it.joinToString(separator = "") })
        val (minDist, prevPoint) = lengthOfShortestPathAndPreviousPoint(grid, destPoint, srcPoint)
        println("minDist=$minDist, prevPoint=$prevPoint")

        val emptyCellPoint = findEmptyCell(grid)
        val (minDistEmptyCell, _) = lengthOfShortestPathAndPreviousPoint(grid, prevPoint, emptyCellPoint)
        println("emptyCellPoint=$emptyCellPoint, minDistEmptyCell=$minDistEmptyCell")
        return minDistEmptyCell + 1 + (minDist - 1) * 5
    }

    /**
     * Returns the length of the shortest path from [startPoint] to [targetPoint]
     * and the previous point visited before reaching the [targetPoint].
     */
    private fun lengthOfShortestPathAndPreviousPoint(
        grid: Array<CharArray>,
        startPoint: Point,
        targetPoint: Point,
    ): Pair<Int, Point> {
        val m = grid.size
        val n = grid[0].size

        val visited = hashSetOf<Point>()
        val q = ArrayDeque<Pair<Point, Int>>()
        q.offer(startPoint to 0)
        visited += startPoint

        fun isFree(p: Point) = p.y in 0 until m && p.x in 0 until n && grid[p.y][p.x] != WALL

        while (q.isNotEmpty()) {
            val (p, dist) = q.poll()
            for ((dx, dy) in moveDiffs) {
                val pNext = Point(p.x + dx, p.y + dy)
                if (!isFree(pNext) || pNext in visited) continue
                val distNext = dist + 1
                if (pNext == targetPoint) return distNext to p
                q.offer(pNext to distNext)
                visited += pNext
            }
        }

        error("No path found from $startPoint to $targetPoint")
    }

    private fun nodesToGrid(nodes: Collection<Node>): Array<CharArray> {
        var xMax = 0
        var yMax = 0
        var emptyNode: Node? = null
        for (node in nodes) {
            if (xMax < node.x) xMax = node.x
            if (yMax < node.y) yMax = node.y
            if (node.used == 0 && (emptyNode == null || emptyNode.avail < node.avail)) {
                emptyNode = node
            }
        }
        requireNotNull(emptyNode) { "No empty node found" }
        val grid = Array(yMax + 1) { CharArray(xMax + 1) { MOBILE } }
        for (node in nodes) {
            grid[node.y][node.x] = when {
                node === emptyNode          -> EMPTY
                node.used > emptyNode.avail -> WALL
                else                        -> MOBILE
            }
        }
        return grid
    }

    private fun findEmptyCell(grid: Array<CharArray>): Point {
        for (y in grid.indices) {
            for (x in grid[y].indices) {
                if (grid[y][x] == EMPTY) return Point(x, y)
            }
        }
        error("No empty cell found")
    }

    companion object {
        private const val EMPTY = ' '

        // Black circle, see https://en.wikipedia.org/wiki/Geometric_Shapes_(Unicode_block)
        private const val MOBILE = '\u25CF'

        // Full block, see https://en.wikipedia.org/wiki/Block_Elements
        private const val WALL = '\u2588'

        private val moveDiffs = listOf(
            0 to 1, // up
            1 to 0, // right
            0 to -1, // down
            -1 to 0, // left
        )
    }
}

private val nodeLineParsingPattern =
    Pattern.compile("/dev/grid/node-x(\\d+)-y(\\d+)\\s+(\\d+)T\\s+(\\d+)T\\s+(\\d+)T.*")

private fun parseNode(s: String): Node = with(nodeLineParsingPattern.matcher(s)) {
    require(matches()) { "Not a valid node string: $s" }
    return Node(
        x = group(1).toInt(),
        y = group(2).toInt(),
        used = group(4).toInt(),
        avail = group(5).toInt(),
    )
}

private fun readInput(inputFileName: String): List<Node> =
    readInputLines(2016, 22, inputFileName)
            .drop(2) // the first two lines are command line and heading that don't contain node data
            .map { s -> parseNode(s) }

private fun printResult(inputFileName: String) {
    val solver = GridComputing()
    val nodes = readInput(inputFileName)
    println(nodes.sortedByDescending { it.used }.joinToString(separator = "\n"))
    // part 1
    val res1 = solver.countViablePairs(nodes)
    println("Number of viable pairs: $res1")
    // part 2
    val srcPoint = Point(nodes.filter { it.y == 0 }.maxOf { it.x }, 0)
    val destPoint = Point(0, 0)
    val res2 = solver.minStepsToMoveDataFromSourceToTarget(srcPoint = srcPoint, destPoint = destPoint, nodes = nodes)
    println("Minimum number of steps to move data from node $srcPoint to node $destPoint: $res2")
}

fun main() {
    printResult("input.txt")
}