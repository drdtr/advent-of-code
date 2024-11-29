package advent_of_code_2018.day01

import Util.readInputLines

/**
 * [Chronal Calibration](https://adventofcode.com/2018/day/1)
 *
 * ### Part 1
 * Sum a sequence of integers.
 *
 * ### Part 2
 * Cyclically sum a sequence of integers until a sum repeats. Return the first repeated sum.
 *
 */
class SumInts {
    fun sumOf(nums: Collection<Int>) = nums.sum()

    fun firstRepeatedSumOf(nums: Collection<Int>): Long {
        val sums = hashSetOf<Long>()
        var sum = 0L
        repeat(1000) {
            for (n in nums) {
                sum += n
                if (sum in sums) return sum
                sums += sum
            }
        }
        error("Couldn't find a repeated sum.")
    }
}

private fun readInput(inputFileName: String): List<Int> =
    readInputLines(2018, 1, inputFileName).map { it.toInt() }

private fun printResult(inputFileName: String) {
    val nums = readInput(inputFileName)
    val solver = SumInts()

    // part 1
    val res1 = solver.sumOf(nums)
    println("Sum of ints: $res1")

    // part 2
    val res2 = solver.firstRepeatedSumOf(nums)
    println("First repeated sum of ints: $res2")
}

fun main() {
    printResult("input.txt")
}
