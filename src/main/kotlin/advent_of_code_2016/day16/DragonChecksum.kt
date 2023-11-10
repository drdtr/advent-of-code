package advent_of_code_2016.day16

import Util.readInputLines

/**
 * [Dragon Checksum](https://adventofcode.com/2016/day/16)
 *
 * Starting with a given binary string `s0`, consisting only of `0` and `1`,
 * generate a string of given length `n` using a modified [dragon curve](https://en.wikipedia.org/wiki/Dragon_curve)
 * repeating the step:
 * - extend `s_i = c0 c1 ... ck` to `s_(i+1) = c0 c1 ... ck 0 !ck !c(k-1) ... !c0`,
 * (where `!0 = 1` and `!1 = 0`)
 *
 * until `|s_(i+1)| >= n`. Then, trim `s_(i+1)` to length `n` to obtain the string`s = c0 c1 ... c_(n-1)`,
 * and compute its checksum as follows:
 * - reduce the string by replacing each pair `c_i c_(i+1)` by `1` if `c_i = c_(i+1)` and by `0` otherwise
 *
 * Until its length is odd. This is the resulting checksum.
 */
class DragonChecksum {
    fun calcDragonChecksum(s: String, n: Int): String {
        require(s.all { ch -> ch == '0' || ch == '1' }) { "Input string must contain only 0 and 1 but was $s" }
        return s.extendToLength(n).calcChecksum()
    }

    private fun String.extendToLength(n: Int): CharArray {
        if (length >= n) return take(n).toCharArray()

        val chars = CharArray(n)
        for (i in indices) chars[i] = this[i]

        var currLen = length
        fun extendString() {
            var i = currLen
            var j = currLen - 1
            chars[i++] = '0'
            while (i < n && j >= 0) {
                chars[i++] = if (chars[j--] == '1') '0' else '1'
            }
            currLen = i
        }

        while (currLen < n) {
            extendString()
        }

        return chars
    }

    private fun CharArray.calcChecksum(): String {
        var currLen = size

        fun reduceString() {
            for (i in 0 until currLen step 2) {
                this[i / 2] = if (this[i] == this[i + 1]) '1' else '0'
            }
            currLen /= 2
        }

        while (currLen % 2 == 0) {
            reduceString()
        }

        return String(this, offset = 0, length = currLen)
    }
}

private fun readInput(inputFileName: String): String =
    readInputLines(2016, 16, inputFileName)[0]

private fun printResult(inputFileName: String) {
    val initString = readInput(inputFileName)
    val solver = DragonChecksum()
    // part 1
    val n1 = 272
    val res1 = solver.calcDragonChecksum(initString, n1)
    println("Dragon checksum for $initString and length $n1: $res1")
    // part 2
    val n2 = 35651584
    val res2 = solver.calcDragonChecksum(initString, n2)
    println("Dragon checksum for $initString and length $n2: $res2")
}

fun main() {
    printResult("input.txt")
}