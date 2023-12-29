package advent_of_code_2016.day24

import Util.readInputLines

/**
 * [Air Duct Spelunking](https://adventofcode.com/2016/day/24)
 *
 * You're given a `m * n` grid, where each cell is denoted as follows:
 * - `.` - empty
 * - `#` - wall
 * - `0` - starting node
 * - `1..9` - one-digit number of one of `k` nodes to be visited
 *
 * ## Part 1
 * Find the length of the shortest path visiting each of the `k` nodes at least once, starting from node `0`.
 *
 * ### Solution idea
 *
 * BFS where visited set include a bitmask with `k` bits to store which of the `k` nodes have already been visited
 * (see also [Minimum time to visit all nodes of given Graph at least once](https://www.geeksforgeeks.org/minimum-time-to-visit-all-nodes-of-given-graph-at-least-once/)).
 * We then search the shortest path that reaches the target bitmask `2^k - 1`.
 *
 * For the input of size `m * n` and `k` nodes to visit, the runtime is `O(m * n * 2^k)`.
 *
 * Even more efficient, if not fast enough for the puzzle input, could be Extended Dijkstra algorithm with bitmask,
 * see [Finding the Shortest Path in a Graph Visiting All Nodes](https://www.baeldung.com/cs/shortest-path-visiting-all-nodes#dijkstra-approach).
 */
class ShortestPathVisitingGivenNodes {
    data class Point(val x: Int, val y: Int)
    data class PointBitmask(val x: Int, val y: Int, val bitmask: Int)

    private fun Point.withBitmask(bitmask: Int) = PointBitmask(x, y, bitmask)

    fun minLengthOfPathVisitingAllGivenNodes(grid: Array<CharArray>): Int {
        val nodesToVisit = buildMap {
            for (y in grid.indices) for (x in grid[y].indices) {
                val ch = grid[y][x]
                if (ch in NODES_TO_VISIT_RANGE) {
                    put(Point(x, y), ch - '0')
                }
            }
        }
        val startingPoint = nodesToVisit.entries.find { (point, node) -> node == 0 }?.key
            ?: error { "Couldn't find starting node $START" }

        nodesToVisit.values.toList().sorted().let { sortedListOfNodesToVisit ->
            val expected = (0 until nodesToVisit.size).toList()
            require(sortedListOfNodesToVisit == expected) {
                "Incorrect node numbering - expected $expected but was $sortedListOfNodesToVisit"
            }
        }
        return minLengthOfPathVisitingAllGivenNodes(grid, startingPoint, nodesToVisit)
    }

    fun minLengthOfPathVisitingAllGivenNodes(
        grid: Array<CharArray>,
        startingPoint: Point,
        nodesToVisit: Map<Point, Int>
    ): Int {
        var minTotalLen = Int.MAX_VALUE
        val m = grid.size
        val n = grid[0].size
        val k = nodesToVisit.size
        val targetBitmask = (1 shl k) - 1

        val q = java.util.ArrayDeque<Pair<PointBitmask, Int>>()
        val visited = hashSetOf<PointBitmask>()

        fun isFree(p: Point) = p.y in 0 until m && p.x in 0 until n && grid[p.y][p.x] != WALL

        val startingPointBitmask = startingPoint.withBitmask(1)
        q += startingPointBitmask to 0
        visited += startingPointBitmask

        while (q.isNotEmpty()) {
            val (pointBitmask, dist) = q.poll()
            val (x, y, bitmask) = pointBitmask
            for ((dx, dy) in moveDiffs) {
                val pNext = Point(x + dx, y + dy)
                if (!isFree(pNext)) continue
                val distNext = dist + 1

                val bitmaskNext = bitmask or (nodesToVisit[pNext]?.let { 1 shl it } ?: 0)
                if (bitmaskNext == targetBitmask) {
                    if (minTotalLen > distNext) minTotalLen = distNext
                } else {
                    val pointBitmaskNext = pNext.withBitmask(bitmaskNext)
                    if (pointBitmaskNext !in visited) {
                        q += pointBitmaskNext to distNext
                        visited += pointBitmaskNext
                    }
                }
            }
        }

        return minTotalLen
    }

    companion object {
        private const val WALL = '#'
        private const val START = '0'
        private val NODES_TO_VISIT_RANGE = '0'..'9'
        private val moveDiffs = listOf(
            0 to 1, // up
            1 to 0, // right
            0 to -1, // down
            -1 to 0, // left
        )
    }
}


private fun readInput(inputFileName: String): Array<CharArray> =
    readInputLines(2016, 24, inputFileName).map { it.toCharArray() }.toTypedArray()

private fun printResult(inputFileName: String) {
    val solver = ShortestPathVisitingGivenNodes()
    val grid = readInput(inputFileName)
    //println(grid.joinToString(separator = "\n") { it.joinToString(separator = "") })
    // part 1
    val res1 = solver.minLengthOfPathVisitingAllGivenNodes(grid)
    println("Minimum number of steps to visit all given nodes: $res1")

}

fun main() {
    printResult("input.txt")
}