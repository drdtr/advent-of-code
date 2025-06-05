package advent_of_code_2018.day07

import Util.readInputLines
import advent_of_code_2018.day07.TopologicalOrder.*
import java.util.*
import java.util.regex.Pattern

/**
 * [The Sum of Its Parts](https://adventofcode.com/2018/day/7)
 *
 * You're given a list of directed edges between nodes represented by ASCII letters.
 *
 * ### Part 1
 * Return a list of nodes where each node depends only on the nodes preceding it in the list.
 * If multiple nodes can be at the same position in list then order them alphabetically.
 *
 * Example:
 * - Edges
 * ```
 * `C -> A`
 * `C -> F`
 * `A -> B`
 * `A -> D`
 * `B -> E`
 * `D -> E`
 * `F -> E`
 * ```
 * - DAG
 * ```
 *   -->A--->B--
 *  /    \      \
 * C      -->D----->E
 *  \           /
 *   ---->F-----
 * ```
 * - Resulting node order: `CABDFE`
 *
 * Solution idea: [Kahn's algorithm](https://en.wikipedia.org/wiki/Topological_sorting#Kahn's_algorithm)
 *
 */
class TopologicalOrder {
    data class Edge(val src: Char, val dest: Char)

    fun getTopologicalOrderOfNodes(edges: List<Edge>): String {
        val adjLists: MutableMap<Char, out SortedSet<Char>> = toAdjacencyListsOrderedAlphabetically(edges)
        val incomingEdgeCountByNode: MutableMap<Char, Int> = countIncomingEdgesByNode(edges)
        val nodesWithNoIncomingEdges = TreeSet<Char>()
        for ((node, incomingCount) in incomingEdgeCountByNode) {
            if (incomingCount == 0) nodesWithNoIncomingEdges += node
        }

        val res = StringBuilder()
        while (nodesWithNoIncomingEdges.isNotEmpty()) {
            val src = nodesWithNoIncomingEdges.removeFirst()
            res.append(src)
            for (dest in adjLists[src].orEmpty()) {
                // reduce the incoming count for all nodes with an incoming edge from `src`
                val newCount = incomingEdgeCountByNode.computeIfPresent(dest) { _, count -> count - 1 }
                if (newCount == 0) {
                    nodesWithNoIncomingEdges += dest
                }
            }
            // remove all edges starting from src
            adjLists.remove(src)
        }

        check(adjLists.isEmpty()) { "Graph has at least one cycle" }
        return res.toString()
    }

    private fun toAdjacencyListsOrderedAlphabetically(edges: List<Edge>): MutableMap<Char, out SortedSet<Char>> =
        hashMapOf<Char, TreeSet<Char>>().apply {
            for ((src, dest) in edges) computeIfAbsent(src) { TreeSet() } += dest
        }

    private fun countIncomingEdgesByNode(edges: List<Edge>): MutableMap<Char, Int> =
        hashMapOf<Char, Int>().apply {
            for ((src, dest) in edges) {
                computeIfAbsent(src) { 0 }
                compute(dest) { _, count -> (count ?: 0) + 1 }
            }
        }
}

private val edgePattern = Pattern.compile("Step (\\w) must be finished before step (\\w) can begin.")

private fun parseEdge(s: String): Edge {
    with(edgePattern.matcher(s)) {
        if (matches()) return Edge(group(1)[0], group(2)[0])
    }
    error("Invalid edge input: $s")
}

private fun readInput(inputFileName: String): List<Edge> =
    readInputLines(2018, 7, inputFileName).map(::parseEdge)

private fun printResult(inputFileName: String) {
    val edges = readInput(inputFileName)
//    println(edges.joinToString(separator = "\n"))
    val solver = TopologicalOrder()

    //  Part 1
    val res1 = solver.getTopologicalOrderOfNodes(edges)
    println("Nodes in topological order: $res1")
}

fun main() {
    printResult("input.txt")
}


