package advent_of_code_2015.day17

import Util.readInputLines

/**
 * [No Such Thing as Too Much - Part 2](https://adventofcode.com/2015/day/17)
 *
 * Given a list of container `capacities` and the exact `target` capacity,
 * find the minimum number `minUsed` of containers needed to exactly reach the target capacity
 * and the number of unique combinations of `minUsed` containers that have the total capacity of `target`
 * (two combinations with two different containers of same size are considered different).
 */
class ContainerCombinationsPart2 {
    data class ContainerCombinationsResult(val minUsed: Int, val combinationsCount: Int)

    fun countCombinations(values: IntArray, target: Int): ContainerCombinationsResult =
        MinCombinationsFinder(values, target).countCombinations()

    private class MinCombinationsFinder(val values: IntArray, val target: Int) {
        private val n = values.size
        private var minUsed = -1
        private var count = 0

        init {
            values.sortDescending()
        }

        fun countCombinations(): ContainerCombinationsResult {
            backtrack(0, 0, 0)
            return ContainerCombinationsResult(minUsed = minUsed, combinationsCount = count)
        }

        private fun backtrack(pos: Int, sumSoFar: Int, usedSoFar: Int) {
            if (sumSoFar >= target || pos == n || (minUsed >= 0 && usedSoFar >= minUsed)) {
                if (sumSoFar == target) {
                    if (minUsed < 0 || usedSoFar < minUsed) {
                        minUsed = usedSoFar
                        count = 0
                    }
                    if (usedSoFar == minUsed) count++
                }
                return
            }

            backtrack(pos + 1, sumSoFar + values[pos], usedSoFar + 1)
            backtrack(pos + 1, sumSoFar, usedSoFar)
        }
    }
}


private fun readInput(inputFileName: String): List<Int> {
    val lines = readInputLines(2015, 17, inputFileName)
    return lines.map { it.toInt() }.also { println(it.sortedDescending()) }
}

private fun printResult(inputFileName: String) {
    val values = readInput(inputFileName)
    val target = 150
    val solver = ContainerCombinationsPart2()
    val res = solver.countCombinations(values.toIntArray(), target)
    println("Minimum used: ${res.minUsed}")
    println("Combinations count: ${res.combinationsCount}")
}

fun main() {
    printResult("input.txt")
}