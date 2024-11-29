package advent_of_code_2018.day02

import Util.readInputLines

/**
 * [Inventory Management System](https://adventofcode.com/2018/day/2)
 *
 * You're given a list of strings of equal length.
 *
 * ### Part 1
 * - count the number of strings that have letters appearing exactly two times as `r2`
 * - count the number of strings that have letters appearing exactly three times as `r3`
 * - compute the checksum as `r2 * r3`.
 *
 * ### Part 2
 * Find two strings differing by exactly one letter and return their common letters.
 *
 * Implementation note:
 * - A brute-force solution compares each string pair, resulting in runtime `O(n^2 * len)`.
 * This implementation was fast enough for the given problem input.
 * - A more efficient implementation is described in
 * [1554. Strings Differ by One Character](https://algo.monster/liteproblems/1554) and
 * [Find string with a single differing position in the given n strings](https://www.geeksforgeeks.org/find-string-with-a-single-differing-position-in-the-given-n-strings/).
 *
 */
class CheckStringIds {
    fun calcChecksumOfRepeatedLetters(strings: List<String>): Int {
        val numOfStringWithExactlyTwoRepeatedLetters = strings.count { it.hasLettersRepeatingExactly(2) }
        val numOfStringWithExactlyThreeRepeatedLetters = strings.count { it.hasLettersRepeatingExactly(3) }
        return numOfStringWithExactlyTwoRepeatedLetters * numOfStringWithExactlyThreeRepeatedLetters
    }

    private fun String.hasLettersRepeatingExactly(n: Int): Boolean {
        val counts = IntArray('z' - 'a' + 1)
        for (ch in this) {
            counts[ch - 'a']++
        }
        return counts.any { it == n }
    }

    fun getCommonLettersInTwoStringsDifferingByExactlyOneLetter(strings: List<String>): String {
        for (i in strings.indices) {
            for (j in i + 1 until strings.size) {
                if (stringsDifferByExactlyOneLetter(strings[i], strings[j])) {
                    return buildString {
                        for (idx in strings[i].indices) {
                            if (strings[i][idx] == strings[j][idx]) append(strings[i][idx])
                        }
                    }
                }
            }
        }
        error("No two strings found differing by exactly one letter")
    }

    private fun stringsDifferByExactlyOneLetter(s1: String, s2: String): Boolean {
        if (s1.length != s2.length) return false
        var differingLetterFound = false
        for (i in s1.indices) {
            if (s1[i] != s2[i]) {
                if (differingLetterFound) return false
                differingLetterFound = true
            }
        }
        return differingLetterFound
    }
}

private fun readInput(inputFileName: String): List<String> =
    readInputLines(2018, 2, inputFileName)

private fun printResult(inputFileName: String) {
    val strings = readInput(inputFileName)
    val solver = CheckStringIds()

    // part 1
    val res1 = solver.calcChecksumOfRepeatedLetters(strings)
    println("Checksum of repeated letters: $res1")

    // part 2
    val res2 = solver.getCommonLettersInTwoStringsDifferingByExactlyOneLetter(strings)
    println("Common letters in two strings found differing by exactly one letter: $res2")
}

fun main() {
    printResult("input.txt")
}
