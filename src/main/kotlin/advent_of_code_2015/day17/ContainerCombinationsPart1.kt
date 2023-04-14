package advent_of_code_2015.day17

import Util.readInputLines

/**
 * [No Such Thing as Too Much - Part 1](https://adventofcode.com/2015/day/17)
 *
 * Given a list of container `capacities` and the exact `target` capacity,
 * find the number of unique combinations of containers that have the total capacity of `target`
 * (two combinations with two different containers of same size are considered different).
 */
class ContainerCombinationsPart1 {
    fun countCombinations(values: IntArray, target: Int): Int =
        CombinationsFinder(values, target).countCombinations()

    private class CombinationsFinder(val values: IntArray, val target: Int) {
        private val n = values.size
        private var count = 0

        init {
            values.sortDescending()
        }

        fun countCombinations(): Int {
            backtrack(0, 0)
            return count
        }

        private fun backtrack(pos: Int, sumSoFar: Int) {
            if (sumSoFar >= target || pos == n) {
                if (sumSoFar == target) count++
                return
            }

            backtrack(pos + 1, sumSoFar + values[pos])
            backtrack(pos + 1, sumSoFar)
        }
    }
}


private fun readInput(inputFileName: String): List<Int> {
    val lines = readInputLines(2015, 17, inputFileName)
    return lines.map { it.toInt() }
}

private fun printResult(inputFileName: String) {
    val values = readInput(inputFileName)
    val target = 150
    val solver = ContainerCombinationsPart1()
    val res = solver.countCombinations(values.toIntArray(), target)
    println("Combinations count: $res")
}

fun main() {
    printResult("input.txt")
}