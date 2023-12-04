package advent_of_code_2016.day22

import Util.readInputLines
import advent_of_code_2016.day22.GridComputing.*
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
 */
class GridComputing {
    data class Node(val x: Int, val y: Int, val size: Int, val used: Int, val avail: Int)

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
}

private val nodeLineParsingPattern =
    Pattern.compile("/dev/grid/node-x(\\d+)-y(\\d+)\\s+(\\d+)T\\s+(\\d+)T\\s+(\\d+)T.*")

private fun parseNode(s: String): Node = with(nodeLineParsingPattern.matcher(s)) {
    require(matches()) { "Not a valid node string: $s" }
    return Node(
        x = group(1).toInt(),
        y = group(2).toInt(),
        size = group(3).toInt(),
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
    val res = solver.countViablePairs(nodes)
    println("Number of viable pairs: $res")
}

fun main() {
    printResult("input.txt")
}