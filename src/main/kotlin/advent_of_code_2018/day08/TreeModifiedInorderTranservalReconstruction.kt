package advent_of_code_2018.day08

import Util.readInputLines

/**
 * [Memory Maneuver](https://adventofcode.com/2018/day/8)
 *
 * You're given a list of nodes and additional data representing an extended version of tree preorder traversal.
 * Each node is represented in the list as follows:
 * - A _header_ consisting of two numbers:
 *    - the number `n >= 0` of child nodes
 *    - the number `m >= 1` of metadata entries
 * - `n` child nodes
 * - `m` metadata entries
 *
 * Example:
 * - `A`: 2 child nodes `B`, `C`; 3 metadata entries 1, 1, 2.
 * - `B`: 0 child nodes; 3 metadata entries 11, 11, 12.
 * - `C`: 1 child node `D`; 1 metadata entry 2.
 * - `D`: 0 child nodes; 1 metadata entry 99.
 *
 * ```
 * 2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2
 * A----------------------------------
 *     B----------- C-----------
 *                      D-----
 * ```
 *
 * ### Part 1
 *
 * Return the sum of all metadata entries.
 *
 * In the example above, the sum is `1+1+2+10+11+12+2+99=138`.
 *
 * ### Part 2
 *
 * Return the _value of the node_ calculated as follows:
 * - If a node has no child nodes then its value is the sum of its metadata entries.
 * - If a node has child nodes then its metadata entries are interpreted as 1-based indices of the child nodes and
 * the value is the sum of values of child node.
 * Metadata entries with no child node corresponding to the index are skipped.
 *
 * In the example above, the value of the root node is 66.
 *
 */
class TreeModifiedInorderTranservalReconstruction {
    data class TreeNode(val metadataEntries: List<Int>, val children: List<TreeNode>) {
        init {
            require(metadataEntries.isNotEmpty()) { "Must have at least one metadata entry." }
        }
    }

    fun TreeNode.toStringIndented(): String {
        fun toStringIndentedRec(node: TreeNode, indentLen: Int, sb: StringBuilder) {
            val indentStr = " ".repeat(indentLen)
            sb.append(indentStr)
                    .append(node.metadataEntries.joinToString(prefix = "(", separator = ", ", postfix = ")"))
                    .append("\n")
            node.children.forEach { child ->
                toStringIndentedRec(child, indentLen + 2, sb)
            }
        }

        val sb = StringBuilder();
        toStringIndentedRec(this, indentLen = 0, sb)
        return sb.toString()
    }

    /**
     * Returns the root of the tree reconstructed from the given list of nodes contents obtained
     * by an extended preorder traversal. Each node is represented by:
     * - number of its children
     * - number of its metadata entries
     * - the traversal results of all children.
     */
    fun reconstructTree(nodesContents: List<Int>): TreeNode {
        data class SubtreeReconstructionResult(val subtree: TreeNode, val nextPos: Int)

        fun reconstructSubtree(pos: Int): SubtreeReconstructionResult {
            var currPos = pos
            val numOfChildren = nodesContents[currPos++]
            val numOfMetadataEntries = nodesContents[currPos++]
            val children = List(numOfChildren) {
                val res = reconstructSubtree(currPos)
                currPos = res.nextPos
                res.subtree
            }
            val metadataEntries = List(numOfMetadataEntries) {
                nodesContents[currPos++]
            }
            return SubtreeReconstructionResult(TreeNode(metadataEntries, children), currPos)
        }

        return reconstructSubtree(pos = 0).subtree
    }

    fun sumOfMetadataEntries(node: TreeNode): Int =
        node.metadataEntries.sum() + node.children.sumOf { sumOfMetadataEntries(it) }

    fun calcValueOfNode(node: TreeNode): Int =
        if (node.children.isEmpty())
            node.metadataEntries.sum()
        else
            node.metadataEntries.sumOf { metadataEntry ->
                val idx = metadataEntry - 1 // The indexing is 1-based, i.e., entry 1 refers to the first child node
                if (idx in node.children.indices) calcValueOfNode(node.children[idx]) else 0
            }

}

private fun readInput(inputFileName: String): List<Int> =
    readInputLines(2018, 8, inputFileName)[0].split(" ").map { it.toInt() }

private fun printResult(inputFileName: String) {
    val preorderNodesWithMetadata = readInput(inputFileName)
    val solver = TreeModifiedInorderTranservalReconstruction()

    val tree = solver.reconstructTree(preorderNodesWithMetadata)
    val res1 = solver.sumOfMetadataEntries(tree)
    println("Sum of metadata entries: $res1")

    val res2 = solver.calcValueOfNode(tree)
    println("Value of the root node: $res2")
}

fun main() {
    printResult("input.txt")
}


