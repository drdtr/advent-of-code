package advent_of_code_2018.day07

import Util.readInputLines
import advent_of_code_2018.day07.TopologicalOrderUtil.Edge
import advent_of_code_2018.day07.TopologicalOrderUtil.countIncomingEdgesByNode
import advent_of_code_2018.day07.TopologicalOrderUtil.parseEdge
import advent_of_code_2018.day07.TopologicalOrderUtil.toAdjacencyListsOrderedAlphabetically
import java.util.*

/**
 * [The Sum of Its Parts](https://adventofcode.com/2018/day/7)
 *
 * You're given a list of directed edges between nodes represented by ASCII letters.
 *
 * ### Part 2
 * Return the time to processing all nodes such that:
 * - each node can be processed only after the processing is completed for all nodes it depends on
 * - the processing of a node takes 61 seconds for `A`, 62 for `B`, etc.
 * - 5 workers are processing the nodes simultaneously.
 *
 * Example:
 * - Graph from Part 1, see [TopologicalOrder].
 * - Simpler processing time of 1 for `A`, 2 for `B`, etc.
 * - The processing is then as follows:
 * ```
 * Second  Worker 1  Worker 2  Done
 * 0       C         .
 * 1       C         .
 * 2       C         .
 * 3       A         F         C
 * 4       B         F         CA
 * 5       B         F         CA
 * 6       D         F         CAB
 * 7       D         F         CAB
 * 8       D         F         CAB
 * 9       D         .         CABF
 * 10      E         .         CABFD
 * 11      E         .         CABFD
 * 12      E         .         CABFD
 * 13      E         .         CABFD
 * 14      E         .         CABFD
 * 15      .         .         CABFDE
 * ```
 * - Resulting processing time: 15 seconds
 *
 * Solution idea: extend the algorithm from Part 1, see [TopologicalOrder.getTopologicalOrderOfNodes],
 * putting nodes and their respective completion times into a priority queue so that we can process
 * each node in the order of completion.
 *
 */
class TopologicalOrderProcessingTime {
    fun getProcessingTimeForAllNodesWithDependencies(
        edges: List<Edge>,
        numOfWorkers: Int,
        processingTime: (Char) -> Int,
    ): Int {
        val adjLists: MutableMap<Char, out SortedSet<Char>> = toAdjacencyListsOrderedAlphabetically(edges)
        val incomingEdgeCountByNode: MutableMap<Char, Int> = countIncomingEdgesByNode(edges)
        val nodesWithNoIncomingEdges = TreeSet<Char>()
        for ((node, incomingCount) in incomingEdgeCountByNode) {
            if (incomingCount == 0) nodesWithNoIncomingEdges += node
        }


        var time = 0

        data class NodeWithCompletionTime(val node: Char, val completionTime: Int)

        infix fun Char.completeAt(completionTime: Int) = NodeWithCompletionTime(this, completionTime)

        val nodesInProgressQueue = PriorityQueue<NodeWithCompletionTime>(compareBy { it.completionTime })
        fun fillNodesInProgressQueue() {
            while (nodesInProgressQueue.size < numOfWorkers && nodesWithNoIncomingEdges.isNotEmpty()) {
                val nextNode = nodesWithNoIncomingEdges.removeFirst()
                val completionTime = time + processingTime(nextNode)
                nodesInProgressQueue += nextNode completeAt completionTime
            }
        }

        fillNodesInProgressQueue()
        while (nodesInProgressQueue.isNotEmpty()) {
            val (src, completionTime) = nodesInProgressQueue.poll()
            for (dest in adjLists[src].orEmpty()) {
                // reduce the incoming count for all nodes with an incoming edge from `src`
                val newCount = incomingEdgeCountByNode.computeIfPresent(dest) { _, count -> count - 1 }
                if (newCount == 0) {
                    nodesWithNoIncomingEdges += dest
                }
            }
            // remove all edges starting from src
            adjLists.remove(src)
            time = completionTime
            fillNodesInProgressQueue()
        }

        check(adjLists.isEmpty()) { "Graph has at least one cycle" }
        return time
    }
}

private fun readInput(inputFileName: String): List<Edge> =
    readInputLines(2018, 7, inputFileName).map(::parseEdge)

private fun printResult(inputFileName: String) {
    val edges = readInput(inputFileName)
    val solver = TopologicalOrderProcessingTime()
    val numOfWorkers = 5

    val res = solver.getProcessingTimeForAllNodesWithDependencies(edges, numOfWorkers) { ch -> ch - 'A' + 61 }
    println("Processing time for all nodes in topological order with $numOfWorkers worker(s): $res")
}

fun main() {
    printResult("input.txt")
}


