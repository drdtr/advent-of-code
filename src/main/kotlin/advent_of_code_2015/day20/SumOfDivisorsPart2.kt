package advent_of_code_2015.day20

import Util.readInputLines
import kotlin.math.ceil

/**
 * [Infinite Elves and Infinite Houses - Part 2](https://adventofcode.com/2015/day/20)
 *
 * For `i, j >= 1`, let
 * - `C_i = {i, 2 * i, 3 * i, ..., m * i}` be finite cycle of the first `m` multiples of `i`
 * - `S_j = Sum{ i | j \in C_i }` be the sum of cycle indices for all cycles containing `j`
 *
 * For a given `k`, find the smallest number `n` such that `S_n >= k`.
 */
class SumOfDivisorsPart2 {
    fun minNumberToReachSum(k: Int, m: Int): Int {
        if (k <= 1) return if (k < 1) 0 else 1

        val sums = IntArray(k + 1)

        for (i in 1..k) {
            sums[i] += i
            // we can check sums[i] already here because `i` is the highest divisor ever added to `sum[i]`
            // and we'll not add anything else to it later
            if (sums[i] >= k) return i

            for (j in 2 * i..k.coerceAtMost(i * m) step i) {
                sums[j] += i
            }
        }

        error("Didn't find a `sums[j] >= k`, `sums` needs to be larger.")
    }
}

private fun readInput(inputFileName: String): Int =
    readInputLines(2015, 20, inputFileName)[0].toInt()

private fun printResult(inputFileName: String) {
    // the elves leave number of presents equal to 11 times their number
    val k = ceil(readInput(inputFileName) / 11.0).toInt()
    // the elves visit at most 50 houses
    val m = 50
    val solver = SumOfDivisorsPart2()
    println("Minimum number to reach sum $k: ${solver.minNumberToReachSum(k, m)}")
}

fun main() {
    printResult("input.txt")
}
