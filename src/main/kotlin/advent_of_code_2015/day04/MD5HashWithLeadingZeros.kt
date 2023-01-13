package advent_of_code_2015.day04

import Util.md5
import Util.readInputLines

/**
 * [The Ideal Stocking Stuffer](https://adventofcode.com/2015/day/4)
 *
 * Given a string `s`, find the smallest positive integer `n` such that
 * the hexadecimal representation of the [MD5](https://en.wikipedia.org/wiki/MD5) hash of `s + n.toString()`
 * starts with five leading zeros. For example, for `s = "abcdef"`, the answer is `609043` because
 * the MD5 hash of `abcdef609043` is `000001dbbfa3a5c83a2d506429c7b00e`,
 * and `609043` is the smallest number producing a hash with five leading zeros.
 *
 */
class MD5HashWithLeadingZeros {
    fun findSmallestIntegerSuffixToProduceLeadingZeros(s: String, numOfZeros: Int): Int {
        val expectedMd5Prefix = String(CharArray(numOfZeros) { '0' })
        for (n in 1..Int.MAX_VALUE) {
            if ("$s$n".md5().startsWith(expectedMd5Prefix)) return n
        }
        error("Couldn't find a positive 32-bit integer that produces $numOfZeros leading zeros in the MD5 hash.")
    }
}


private fun printResult(inputFileName: String) {
    val s = readInputLines(2015, 4, inputFileName).first()
    val solver = MD5HashWithLeadingZeros()
    println("Smallest integer for 5 leading zeros: ${solver.findSmallestIntegerSuffixToProduceLeadingZeros(s, 5)}")
    println("Smallest integer for 6 leading zeros: ${solver.findSmallestIntegerSuffixToProduceLeadingZeros(s, 6)}")
}

fun main() {
    printResult("input.txt")
}