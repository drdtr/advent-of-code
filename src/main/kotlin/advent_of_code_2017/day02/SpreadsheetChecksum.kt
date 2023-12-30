package advent_of_code_2017.day02

import Util.readInputLines

/**
 * [Corruption Checksum](https://adventofcode.com/2017/day/2)
 *
 * Given a spreadsheet of integers, represented as a list of rows
 * that are integer arrays with possibly different lengths, find their checksum.
 *
 * ### Part 1
 * The checksum is calculated as follows:
 * - the checksum of a row is the difference between the smallest and the largest value in it
 * - the checksum of the spreadsheet is the sum of the checksums of all rows
 *
 * Example:
 * ```
 * 5 1 9 5
 * 7 5 3
 * 2 4 6 8
 * ```
 * - 1st row: max value is 9, min value is 1, checksum is 8
 * - 2nd row: max value is 7, min value is 3, checksum is 4
 * - 3rd row: max value is 8, min value is 2, checksum is 6
 * - Spreadsheet checksum is 8 + 4 + 6 = 18
 *
 *
 * ### Part 2
 * The checksum is calculated as follows:
 * - the checksum of a row is quotient of two elements where one evenly divides the other
 * - the checksum of the spreadsheet is the sum of the checksums of all rows
 *
 * Example:
 * ```
 * 5 9 2 8
 * 9 4 7 3
 * 3 8 6 5
 * ```
 * - 1st row: 2 evenly divides 8, the quotient is 4
 * - 2nd row: 3 evenly divides 9, the quotient is 3
 * - 3rd row: 3 evenly divides 6, the quotient is 2
 * - Spreadsheet checksum is 4 + 3 + 2 = 9
 *
 */
class SpreadsheetChecksum {
    fun calcSumOfMaxMinDiffs(rows: List<IntArray>): Int = rows.sumOf { it.max() - it.min() }

    fun calcSumOfQuotients(rows: List<IntArray>): Int = rows.sumOf { calcQuotientOfTwoEvenlyDivisibleElements(it) }

    private fun calcQuotientOfTwoEvenlyDivisibleElements(nums: IntArray): Int {
        for (i in nums.indices) for (j in nums.indices) {
            if (i == j) continue
            if (nums[i] % nums[j] == 0) return nums[i] / nums[j]
        }
        error("No evenly divisible elements found in row ${nums.contentToString()}")

    }
}

private fun readInput(inputFileName: String): List<IntArray> =
    readInputLines(2017, 2, inputFileName).map { line ->
        line.split(" ", "\t").map { it.toInt() }.toIntArray()
    }

private fun printResult(inputFileName: String) {
    val rows = readInput(inputFileName)
    val solver = SpreadsheetChecksum()

    // part 1
    val res1 = solver.calcSumOfMaxMinDiffs(rows)
    println("Sum of max-min differences in each rows: $res1")

    // part 2
    val res2 = solver.calcSumOfQuotients(rows)
    println("Sum of quotients of two evenly divisible elements in each row: $res2")
}

fun main() {
    printResult("input.txt")
}
