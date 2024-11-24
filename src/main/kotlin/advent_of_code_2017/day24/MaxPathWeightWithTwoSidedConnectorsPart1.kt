package advent_of_code_2017.day24

import advent_of_code_2017.day24.TwoSidedConnectors.Connector

/**
 * [Electromagnetic Moat](https://adventofcode.com/2017/day/24)
 *
 * - You're given a collection of two-sided connectors `(a, b)`.
 * - A path must start with a connector's side 0, i.e. the first connector must have `a = 0`.
 * - A connector can be attached with any of its sides: if the current end of the path is `x` then
 * a connector `(x, y)` or `(y, x)` can be attached with the x-side, leaving the y-side available
 * for a subsequent connection.
 *
 * Note: The general longest path problem is NP-hard,
 * cf. [Wikipedia](https://en.wikipedia.org/wiki/Longest_path_problem#NP-hardness).
 *
 * ### Part 1
 * - Calculating the path weight as sum of all side values, what is the maximum possible path weight?
 */
class MaxPathWeightWithTwoSidedConnectorsPart1 {
    fun findMaxPathWeight(connectors: List<Connector>): Int =
        MaxPathLengthFinderBacktracking(connectors).maxPathLength()

    private class MaxPathLengthFinderBacktracking(val connectors: List<Connector>) {
        private val connectorIndicesBySideValue: Map<Int, List<Int>> = connectors
                .mapIndexed { index, (a, b) -> listOf(a to index, b to index) }
                .flatten()
                .groupBy({ it.first }) { it.second }
        private var maxWeight = 0

        fun maxPathLength(): Int {
            backtrack()
            return maxWeight
        }

        private fun backtrack(
            currWeight: Int = 0,
            currOpenSideValue: Int = 0,
            numOfUsedConnectors: Int = 0,
            isAvailableConnectorIndex: BooleanArray = BooleanArray(connectors.size) { true }
        ) {
            if (maxWeight < currWeight) maxWeight = currWeight
            if (numOfUsedConnectors == connectors.size) return

            val indicesOfConnectorsWithOpenSideValue = connectorIndicesBySideValue[currOpenSideValue].orEmpty()
            for (connectorIndex in indicesOfConnectorsWithOpenSideValue) {
                if (!isAvailableConnectorIndex[connectorIndex]) continue

                val connector = connectors[connectorIndex]
                isAvailableConnectorIndex[connectorIndex] = false
                backtrack(
                    currWeight = currWeight + connector.weight,
                    currOpenSideValue = connector.otherSideValue(currOpenSideValue),
                    numOfUsedConnectors = numOfUsedConnectors + 1,
                    isAvailableConnectorIndex
                )
                isAvailableConnectorIndex[connectorIndex] = true
            }
        }
    }
}

private fun printResult(inputFileName: String) {
    val connectors = TwoSidedConnectors.readInput(inputFileName)
//    println(connectors.sortedBy { it.a }.joinToString(separator = "\n"))

    val solver = MaxPathWeightWithTwoSidedConnectorsPart1()
    println("Maximum path weight: ${solver.findMaxPathWeight(connectors)}")
}

fun main() {
    printResult("input.txt")
}