package advent_of_code_2017.day12

import Util.readInputLines
import advent_of_code_2017.day12.ReachableNodes.*
import advent_of_code_2017.day12.ReachableNodes.Graph.Companion.toGraph
import java.util.regex.Pattern

/**
 * [Digital Plumber](https://adventofcode.com/2017/day/12)
 *
 * Given a graph with nodes and bi-directional edges between them, count how many nodes are in the connected component
 * that contains the node `0`.
 */
class ReachableNodes {
    @JvmInline
    value class Graph(val adjLists: Map<Int, List<Int>>) {
        companion object {
            fun Map<Int, List<Int>>.toGraph() = Graph(this)
        }
    }

    fun countReachableNodes(graph: Graph, startNode: Int) = findReachableNodes(graph, startNode).size

    fun countConnectedComponents(graph: Graph): Int {
        var count = 0
        val visited = hashSetOf<Int>()

        for (node in graph.adjLists.keys) {
            if (node in visited) continue
            val nodesInComponent = findReachableNodes(graph, startNode = node)
            visited += nodesInComponent
            count++
        }

        return count
    }

    private fun findReachableNodes(graph: Graph, startNode: Int): Set<Int> {
        val visitedCompNodes = hashSetOf<Int>()
        val stack = java.util.ArrayDeque<Int>()

        visitedCompNodes += startNode
        stack.push(startNode)
        while (stack.isNotEmpty()) {
            val node = stack.pop()
            for (destNode in graph.adjLists[node].orEmpty()) {
                if (destNode in visitedCompNodes) continue
                stack.push(destNode)
                visitedCompNodes += destNode
            }
        }
        return visitedCompNodes
    }
}


private val graphLineInputMatcher = Pattern.compile("(\\d+) <-> (.*)")

private fun readInput(inputFileName: String): Graph =
    readInputLines(2017, 12, inputFileName).map { line ->
        with(graphLineInputMatcher.matcher(line)) {
            require(matches()) { "Invalid input line: $line" }
            val srcNode = group(1).toInt()
            val destNodes = group(2).split(", ").map { it.toInt() }
            srcNode to destNodes
        }
    }.toMap().toGraph()

private fun printResult(inputFileName: String) {
    val graph = readInput(inputFileName)
    val solver = ReachableNodes()

    // part 1
    val res1 = solver.countReachableNodes(graph, startNode = 0)
    println("Size of the connected component containing the node 0: $res1")

    // part 2
    val res2 = solver.countConnectedComponents(graph)
    println("Number of connected components: $res2")
}

fun main() {
    printResult("input.txt")
}
