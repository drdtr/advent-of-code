package advent_of_code_2017.day07

import Util.readInputLines
import advent_of_code_2017.day07.TowerGraph.*
import java.util.regex.Pattern

/**
 * [Recursive Circus](https://adventofcode.com/2017/day/7)
 *
 * You're given a directed graph of weighted nodes, representing a tower of nodes,
 * where the node with no incoming edges is at the bottom
 * (i.e. an upside down directed tree because the root is at the bottom instead of at the top)
 *
 * ### Part 1
 *
 * Find the node at the bottom of the tower, i.e., the node with no incoming edges.
 *
 * Example:
 * - Nodes:
 * ```
 * pbga (66)
 * xhth (57)
 * ebii (61)
 * havc (66)
 * ktlj (57)
 * fwft (72) -> ktlj, cntj, xhth
 * qoyq (66)
 * padx (45) -> pbga, havc, qoyq
 * tknk (41) -> ugml, padx, fwft
 * jptl (61)
 * ugml (68) -> gyxo, ebii, jptl
 * gyxo (61)
 * cntj (57)
 * ```
 * - Resulting graph:
 * ```
 *                 gyxo
 *               /
 *          ugml - ebii
 *        /      \
 *       |         jptl
 *       |
 *       |         pbga
 *      /        /
 * tknk --- padx - havc
 *      \        \
 *       |         qoyq
 *       |
 *       |         ktlj
 *        \        /
 *          fwft - cntj
 *               \
 *                xhth
 * ```
 *
 *
 * ### Part 2
 *
 * Find the node whose weight needs to be corrected so that all sub-towers in the tower are balanced,
 * and calculate the corrected weight, assuming that there's exactly one such node.
 *
 * Example:
 * - `ugml + (gyxo + ebii + jptl) = 68 + (61 + 61 + 61) = 251`
 * - `padx + (pbga + havc + qoyq) = 45 + (66 + 66 + 66) = 243`
 * - `fwft + (ktlj + cntj + xhth) = 72 + (57 + 57 + 57) = 243`
 * - Here, the weight of the node `ugml` needs to be corrected from `68` to `60`.
 *
 */
class TowerGraph {
    data class Node(val name: String, val weight: Int, val childNames: Set<String> = emptySet())
    data class NodeWeight(val name: String, val weight: Int)

    fun findBottomNode(nodes: List<Node>): Node = BottomNodeFinder().findBottomNode(nodes)

    fun calcCorrectedNodeWeight(nodes: List<Node>): NodeWeight =
        SubtowerWeightsCalculator(nodes).calcCorrectedNodeWeight()

    private class BottomNodeFinder {
        fun findBottomNode(nodes: List<Node>): Node {
            val nodeNamesWithIncomingEdges = nodes.flatMapTo(hashSetOf()) { it.childNames }.apply {
                require(isNotEmpty()) { "Found no nodes without incoming edges." }
            }
            val nodesWithoutIncomingEdges = nodes.filter { it.name !in nodeNamesWithIncomingEdges }.apply {
                require(size < 2) { "Found more than 1 node with incoming edges: ${map { it.name }}" }
            }
            return nodesWithoutIncomingEdges.first()
        }
    }

    private class SubtowerWeightsCalculator(nodes: List<Node>) {
        private val nodesByName = nodes.associateBy { it.name }
        private val subtowerWeights = hashMapOf<String, Int>()
        private val bottomNode = BottomNodeFinder().findBottomNode(nodes)

        fun calcCorrectedNodeWeight(): NodeWeight {
            calcSubtowerWeights(bottomNode)
            val minUnbalancedSubtower =
                requireNotNull(findMinUnbalancedSubtower(bottomNode)) { "Found no unbalanced subtowers" }
            val childSubtowersByWeight = minUnbalancedSubtower.childNames.groupBy { subtowerWeights[it]!! }.apply {
                require(size == 2) { "There must be exactly two subtower sizes in an unbalanced subtree: but found $this" }
            }
            val childSubtowersByWeightSortedByGroupSize =
                childSubtowersByWeight.toList().sortedBy { (_, childNames) -> childNames.size }
            val (incorrectSubtowerWeight, incorrectWeightNodeNames) = childSubtowersByWeightSortedByGroupSize[0]
            require(incorrectWeightNodeNames.size == 1) {
                "There must be exactly one node with incorrect weight: but found $incorrectWeightNodeNames"
            }
            val incorrectWeightNode = nodesByName[incorrectWeightNodeNames[0]]!!
            val correctSubtowerWeight = childSubtowersByWeightSortedByGroupSize[1].first
            val weightDiff = incorrectSubtowerWeight - correctSubtowerWeight

            return NodeWeight(name = incorrectWeightNode.name, weight = incorrectWeightNode.weight - weightDiff)
        }

        private fun calcSubtowerWeights(node: Node) {
            node.childNames.forEach { calcSubtowerWeights(it) }
            subtowerWeights[node.name] = node.childNames.sumOf { subtowerWeights[it]!! } + node.weight
        }

        private fun calcSubtowerWeights(nodeName: String) {
            calcSubtowerWeights(nodesByName[nodeName]!!)
        }

        private fun findMinUnbalancedSubtower(node: Node): Node? {
            if (node.childNames.isEmpty()) {
                return null // no child nodes - the subtower is balanced
            }
            val childSubtowerWeights = node.childNames.map { subtowerWeights[it]!! }
            if (childSubtowerWeights.distinct().size == 1) {
                return null // all child subtowers have same weight - the subtower is balanced
            }
            val unbalancedChildNodes = node.childNames.mapNotNull { findMinUnbalancedSubtower(it) }
            return when (unbalancedChildNodes.size) {
                0 -> node // we've reached the smallest unbalanced subtower
                1 -> findMinUnbalancedSubtower(unbalancedChildNodes.first())
                else -> error("Found more than one unbalanced subtowers in $node")
            }
        }

        private fun findMinUnbalancedSubtower(nodeName: String) =
            findMinUnbalancedSubtower(nodesByName[nodeName]!!)
    }
}

private val leaveNodePattern = Pattern.compile("(\\w+) \\((\\d+)\\)")
private val nodePattern = Pattern.compile("(\\w+) \\((\\d+)\\) -> (.*)")

private fun parseNode(s: String): Node {
    with(leaveNodePattern.matcher(s)) {
        if (matches()) return Node(name = group(1), weight = group(2).toInt())
    }
    with(nodePattern.matcher(s)) {
        if (matches()) return Node(name = group(1),
            weight = group(2).toInt(),
            childNames = group(3).split(",").mapTo(hashSetOf()) { it.trim() })
    }
    error("Invalid node $s")
}

private fun readInput(inputFileName: String): List<Node> = readInputLines(2017, 7, inputFileName).map(::parseNode)

private fun printResult(inputFileName: String) {
    val solver = TowerGraph()
    val nodes = readInput(inputFileName)
//    println(nodes.joinToString(separator = "\n"))
    // part 1
    val res1 = solver.findBottomNode(nodes)
    println("Bottom node: $res1")
    // part 2
    val res2 = solver.calcCorrectedNodeWeight(nodes)
    println("Corrected node weight: $res2")
}

fun main() {
    printResult("input.txt")
}