package advent_of_code_2017.day17

import Util.readInputLines

/**
 * [Spinlock](https://adventofcode.com/2017/day/17)
 *
 * Given an integer `k`, a `spinlock` performs following operations:
 * - start with a sequence of `0`
 * - in each step `i`
 *    - step forward `k` times, cyclically
 *    - insert `i`
 *
 * ### Part 1
 * After `n = 2017` insertions, return the element directly after `n`.
 *
 * ### Part 2
 * After `n = 50_000_000` insertions, return the element directly after `0`.
 *
 */
class Spinlock {

    data class CyclicalInsertionResult(val elements: List<Int>, val mostRecentIndex: Int)

    fun insertCyclically(n: Int, k: Int): CyclicalInsertionResult {
        val resList = ArrayList<Int>(n).apply { add(0) }
        var currIndex = 0
        for (i in 1..n) {
            currIndex = (currIndex + k) % resList.size
            val insertionIndex = (currIndex + 1) % (resList.size + 1)
            resList.add(insertionIndex, i)
            currIndex = insertionIndex
        }
        return CyclicalInsertionResult(resList, currIndex)
    }

    fun getElementAfterCyclicallyInsertingNElements(n: Int, k: Int): Int =
        insertCyclically(n, k).let { (elements, index) -> elements[(index + 1) % elements.size] }

    fun getElementAtOneAfterCyclicallyInsertingNElements(n: Int, k: Int): Int {
        TODO()
    }
}

private fun readInput(inputFileName: String): Int =
    readInputLines(2017, 17, inputFileName)[0].toInt()

private fun printResult(inputFileName: String) {
    val k = readInput(inputFileName)
    val solver = Spinlock()

    // part 1
    val n1 = 2017
    val res1 = solver.getElementAfterCyclicallyInsertingNElements(n1, k)
    println("Element after the last inserted element $n1: $res1")

    // part 2
    val n2 = 50_000_000
    val res2 = solver.getElementAtOneAfterCyclicallyInsertingNElements(n2, k)
    println("Element after 0 after inserting $n2 elements: $res2")
}

fun main() {
    printResult("input.txt")
}
