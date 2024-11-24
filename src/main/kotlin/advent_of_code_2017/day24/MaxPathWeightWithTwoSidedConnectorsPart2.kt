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
class MaxPathWeightWithTwoSidedConnectorsPart2 {
    fun findMaxPathWeightForLongestPath(connectors: List<Connector>): Int =
        MaxPathWeightForLongestPathFinderBacktracking(connectors).maxPathWeightForLongestPath()

    private class MaxPathWeightForLongestPathFinderBacktracking(val connectors: List<Connector>) {
        private val connectorIndicesBySideValue: Map<Int, List<Int>> = connectors
                .mapIndexed { index, (a, b) -> listOf(a to index, b to index) }
                .flatten()
                .groupBy({ it.first }) { it.second }
        private var maxLength = 0
        private var maxWeight = 0

        fun maxPathWeightForLongestPath(): Int {
            backtrack()
            return maxWeight
        }

        private fun backtrack(
            currLength: Int = 0,
            currWeight: Int = 0,
            currOpenSideValue: Int = 0,
            numOfUsedConnectors: Int = 0,
            isAvailableConnectorIndex: BooleanArray = BooleanArray(connectors.size) { true }
        ) {
            if (maxLength <= currLength) {
                maxLength = currLength
                if (maxWeight < currWeight) maxWeight = currWeight
            }
            if (numOfUsedConnectors == connectors.size) return

            val indicesOfConnectorsWithOpenSideValue = connectorIndicesBySideValue[currOpenSideValue].orEmpty()
            for (connectorIndex in indicesOfConnectorsWithOpenSideValue) {
                if (!isAvailableConnectorIndex[connectorIndex]) continue

                val connector = connectors[connectorIndex]
                isAvailableConnectorIndex[connectorIndex] = false
                backtrack(
                    currLength = currLength + 1,
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

    val solver = MaxPathWeightWithTwoSidedConnectorsPart2()
    println("Maximum weight of longest path: ${solver.findMaxPathWeightForLongestPath(connectors)}")
}

fun main() {
    printResult("input.txt")
}