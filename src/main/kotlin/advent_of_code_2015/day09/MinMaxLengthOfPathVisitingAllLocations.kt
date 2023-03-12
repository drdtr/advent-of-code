package advent_of_code_2015.day09

import Util.readInputLines
import advent_of_code_2015.day09.MinMaxLengthOfPathVisitingAllLocations.*

/**
 * [All in a Single Night](https://adventofcode.com/2015/day/9)
 *
 * Given the distances between locations:
 * - part 1: find the minimum path length that visits each location exactly ones.
 * - part 2: find the maximum path length that visits each location exactly ones.
 *
 * Example:
 * - Pairwise distances
 *    - `L -> D: 464`
 *    - `L -> B: 518`
 *    - `D -> B: 141`
 * - Minimum path length: `L -> D -> B: 605`
 * - Maximum path length: `D -> L -> B: 982`
 *
 * Implementation: finding a shortest path that visits all cities is NP-complete,
 * see [Hamiltonian path problem](https://en.wikipedia.org/wiki/Hamiltonian_path_problem).
 * Given the small input size, we will simply solve it by brute force.
 */
class MinMaxLengthOfPathVisitingAllLocations {
    data class WeightedEdge(val src: String, val dest: String, val weight: Int)

    fun maxPathLen(edges: List<WeightedEdge>): Int = minMaxPathLen(edges).first
    fun minPathLen(edges: List<WeightedEdge>): Int = minMaxPathLen(edges).second

    fun minMaxPathLen(edges: List<WeightedEdge>): Pair<Int, Int> = MinMaxPathFinder(edges).findMinMaxPathLen()

    class MinMaxPathFinder(edges: List<WeightedEdge>) {
        private var minPathLen = Int.MAX_VALUE
        private var maxPathLen = 0

        private val nodes = (edges.map { it.src } + edges.map { it.dest }).distinct()
        private val n = nodes.size
        private val edgeWeights = edges.associate { (it.src to it.dest) to it.weight }
        private val visitedNodes = nodes.map { it to false }.toMap(mutableMapOf())

        fun findMinMaxPathLen(): Pair<Int, Int> {
            for (start in nodes) {
                visitedNodes[start] = true
                backtrack(start, 1, 0)
                visitedNodes[start] = false
            }
            return minPathLen to maxPathLen
        }

        private fun backtrack(prevNode: String, numOfVisited: Int, currPathLen: Int) {
            if (numOfVisited == n) {
                if (minPathLen > currPathLen) minPathLen = currPathLen
                if (maxPathLen < currPathLen) maxPathLen = currPathLen
                return
            }

            for (node in nodes) {
                if (visitedNodes[node]!!) continue
                val edgeWeight = edgeWeights[prevNode to node] ?: edgeWeights[node to prevNode] ?: continue // error("")

                visitedNodes[node] = true
                backtrack(node, numOfVisited + 1, currPathLen + edgeWeight)
                visitedNodes[node] = false
            }
        }
    }
}

private fun readInput(inputFileName: String): List<WeightedEdge> {
    val lines = readInputLines(2015, 9, inputFileName)
    return lines.asSequence()
            .map { it.split(" ") }
            .map { WeightedEdge(it[0], it[2], it[4].toInt()) }
            .toList()
}

private fun printResult(inputFileName: String) {
    val edges = readInput(inputFileName)
    val solver = MinMaxLengthOfPathVisitingAllLocations()
    val (minLen, maxLen) = solver.minMaxPathLen(edges)
    println("Min. path len: $minLen")
    println("Max. path len: $maxLen")
}

fun main() {
    printResult("input.txt")
}