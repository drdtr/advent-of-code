package advent_of_code_2018.day07

import java.util.*
import java.util.regex.Pattern

object TopologicalOrderUtil {
    data class Edge(val src: Char, val dest: Char)

    private val edgePattern = Pattern.compile("Step (\\w) must be finished before step (\\w) can begin.")

    fun parseEdge(s: String): Edge {
        with(edgePattern.matcher(s)) {
            if (matches()) return Edge(group(1)[0], group(2)[0])
        }
        error("Invalid edge input: $s")
    }

    fun toAdjacencyListsOrderedAlphabetically(edges: List<Edge>): MutableMap<Char, out SortedSet<Char>> =
        hashMapOf<Char, TreeSet<Char>>().apply {
            for ((src, dest) in edges) computeIfAbsent(src) { TreeSet() } += dest
        }

    fun countIncomingEdgesByNode(edges: List<Edge>): MutableMap<Char, Int> =
        hashMapOf<Char, Int>().apply {
            for ((src, dest) in edges) {
                computeIfAbsent(src) { 0 }
                compute(dest) { _, count -> (count ?: 0) + 1 }
            }
        }
}