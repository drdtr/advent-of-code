package advent_of_code_2015.day08

import Util.readInputLines

/**
 * [Matchsticks](https://adventofcode.com/2015/day/8#part2)
 *
 * You're given strings that can contain the special characters `\` and `"`.
 * Escape all special characters by `\` and calculate the difference between the length of the initial
 * and the escaped string.
 *
 * For example:
 * - `"" -> "\"\""`: 4 because the length is increased from 2 to 6.
 * - `"abc" -> "\"abc\""`: 4 because the length is increased from 5 to 9.
 * - `"aaa\"aaa" -> "\"aaa\\\"aaa\""`: 6 because the length is increased from 10 to 16.
 * - `"\x41" -> "\"\\x41\""`: 5 because the length is increased from 6 to 11.
 *
 */
class EscapedStringLengthDifferencePart2 {
    fun calcEscapedLenDiff(strings: List<String>) = strings.sumOf { calcEscapedLenDiff(it) }

    fun calcEscapedLenDiff(s: String): Int {
        var charCount = 2 // add 2 for the opening and closing `"` for the encoded string
        for (ch in s) when (ch) {
            '\\', '"' -> charCount += 2
            else      -> charCount++
        }

        return charCount - s.length
    }
}

private fun printResult(inputFileName: String) {
    val strings = readInputLines(2015, 8, inputFileName)
    val escapedLenDiff = EscapedStringLengthDifferencePart2().calcEscapedLenDiff(strings)
    println("Differences between initial and escaped string length: $escapedLenDiff")
}

fun main() {
    printResult("input.txt")
}