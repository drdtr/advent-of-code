package advent_of_code_2015.day15

import Util.readInputLines

/**
 * [Science for Hungry People](https://adventofcode.com/2015/day/15)
 *
 * Given a `m * n` matrix `mat` and a coefficient sum `cSum`,
 * pick `k` indices `i in [0..m-1]` and coefficients `c` (where `c0 + ... c_k-1 = cSum`),
 * to maximize the product of weighted sums:
 * ```
 * P = s0 * s1 * ... * s_n-1
 * s_j = c0 * mat[j][i0] + ... + c_k-1 * mat[j][i_k-1]
 * ```
 * Note that if _any_ `s_j <= 0` then it becomes zero and with it `P = 0`.
 *
 */
class MaximizeProductOfWeightedSums {
    fun findMax(mat: Array<IntArray>, cSum: Int): Long = MaxFinder(mat, cSum).findMax()

    fun findMaxWithFixedSum(mat: Array<IntArray>, cSum: Int, fixedSumParams: IntArray, fixedSum: Long) =
        MaxFinder(mat, cSum, fixedSumParams, fixedSum).findMax()

    private class MaxFinder(
        val mat: Array<IntArray>,
        val cSum: Int,
        val fixedSumParams: IntArray = intArrayOf(),
        val fixedSum: Long = 0
    ) {
        private val m = mat.size
        private val n = mat[0].size
        private var max = 0L
        private val coefficients = IntArray(m)

        fun findMax(): Long {
            backtrack(0, cSum)
            return max
        }

        private fun backtrack(pos: Int, remainingSum: Int) {
            if (pos == m - 1) {
                coefficients[pos] = remainingSum
                if (!matchesFixedSum()) return
                val curr = calcProdOfSums()
                if (max < curr) max = curr
                return
            }

            for (c in 0..remainingSum) {
                coefficients[pos] = c
                backtrack(pos + 1, remainingSum - c)
            }
        }

        private fun matchesFixedSum(): Boolean {
            if (fixedSumParams.isEmpty()) return true // nothing to check
            var sum = 0L
            for (j in 0 until m) sum += coefficients[j] * fixedSumParams[j].toLong()
            return sum == fixedSum
        }

        private fun calcProdOfSums(): Long {
            var prod = 1L
            for (i in 0 until n) {
                var sum = 0L
                for (j in 0 until m) sum += coefficients[j] * mat[j][i].toLong()
                if (sum <= 0) return 0
                prod *= sum
            }
            return prod
        }
    }
}

private fun readInput(inputFileName: String): Pair<Array<IntArray>, IntArray> {
    val lines = readInputLines(2015, 15, inputFileName)
    val splitLines = lines.map { it.split(" ", ",") }
    val mat = splitLines.map { it.slice(listOf(2, 5, 8, 11)).map { it.toInt() }.toIntArray() }.toTypedArray()
    val fixedSumParams = splitLines.map { it[14].toInt() }.toIntArray()
    return mat to fixedSumParams
}

private fun printResult(inputFileName: String) {
    val (mat, fixedSumParams) = readInput(inputFileName)
    val cSum = 100
    val fixedSum = 500L
    val solver = MaximizeProductOfWeightedSums()
    val res1 = solver.findMax(mat, cSum)
    val res2 = solver.findMaxWithFixedSum(mat, cSum, fixedSumParams, fixedSum)
    println("Maximum product of weighted sums: $res1")
    println("Maximum product of weighted sums with additional fixed sum: $res2")
}

fun main() {
    printResult("input.txt")
}