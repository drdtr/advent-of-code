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
 * #### Idea
 *
 * Perform the complete calculation of the resulting sequence of `n + 1` elements.
 *
 * #### Runtime complexity
 *
 * The `i`-th insertion is expected to shift half of the elements by 1,
 * so the runtime for processing each `i in 1..n` is `O(i)`
 * and hence the runtime for processing all `i in 1..n` is `O(n^2)`.
 * While it's quadratic, it's still acceptable for `n = 2017`.
 *
 * ### Part 2
 * After `n = 50_000_000` insertions, return the element directly after `0`.
 *
 * #### Idea
 *
 * Generating the complete resulting sequence for `n = 50_000_000` will be too slow.
 * However, we don't need all the elements, but just the element after `0`.
 *
 * The element `0` will always remain at position `0` because no element would ever be inserted at `0`,
 * instead it would either be inserted at the end of the list, or after the element `0`:
 * - if the `k` steps forward end at the first element, then the new element wil be inserted at position 1
 * - if the `k` steps forward end at any other element at position `j` (even if `j` is the last position in the list),
 * then the new element will be inserted after position `j`
 * (if `j` is the last position, then the new element will be inserted element at the end of the list)
 *
 * This is why we don't need to calculate the complete resulting sequence,
 * instead we simply need to observe which values are inserted at position `1`
 * and return the most recent value.
 *
 * #### Runtime complexity
 *
 * Each value `i in 1..n` takes constant time to process
 * (we don't need to actually modify the resulting list, which was the step taking `O(i)` in part 1)
 * hence the overall runtime is `O(n)`.
 *
 */
class CyclicalInsertions {

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
        require(n >= 1) { "Number of cyclical insertions must be at least 1 but was $n." }
        var currIndex = 0
        var mostRecentAtOne = 0
        for (i in 1..n) {
            val listSize = i
            currIndex = (currIndex + k) % listSize
            val insertionIndex = (currIndex + 1) % (listSize + 1)
            if (insertionIndex == 1) {
                mostRecentAtOne = i
            }
            currIndex = insertionIndex
        }
        return mostRecentAtOne
    }
}

private fun readInput(inputFileName: String): Int =
    readInputLines(2017, 17, inputFileName)[0].toInt()

private fun printResult(inputFileName: String) {
    val k = readInput(inputFileName)
    val solver = CyclicalInsertions()

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
