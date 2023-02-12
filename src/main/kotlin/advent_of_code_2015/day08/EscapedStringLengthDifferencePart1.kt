package advent_of_code_2015.day08

import Util.readInputLines

/**
 * [Matchsticks](https://adventofcode.com/2015/day/8)
 *
 * You're given strings that can contain escaped characters. Following escaped sequences are possible:
 * - `\\` representing `\`
 * - `\"` representing `"`
 * - `\xAB` representing the ASCII char with hexadecimal code `AB`, e.g. `\x41` representing `A`.
 *
 * For a given list of strings, return the difference The length in code and the length of resulting strings in memory.
 *
 * For example:
 * - `""`: 2 because the length in code is 2 and length in memory is 0.
 * - `"abc"`: 2 because the length in code is 5 and length in memory is 3.
 * - `"ab\"c"`: 3 because the length in code is 7 and length in memory is 4.
 * - `"\x41"`: 5 because the length in code is 6 and length in memory is 1.
 *
 */
class EscapedStringLengthDifferencePart1 {
    fun calcEscapedLenDiff(strings: List<String>) = strings.sumOf { calcEscapedLenDiff(it) }

    fun calcEscapedLenDiff(s: String): Int {
        var charCount = 0
        var i = 1 // start at 1, because we don't count the opening "
        while (i < s.length - 1) { // -1, because we don't count the closing "
            when (s[i]) {
                '\\' -> i += if (s[i + 1] == 'x') 4 else 2
                else -> i++
            }
            charCount++
        }

        return s.length - charCount
    }
}

private fun printResult(inputFileName: String) {
    val strings = readInputLines(2015, 8, inputFileName)
    val escapedLenDiff = EscapedStringLengthDifferencePart1().calcEscapedLenDiff(strings)
    println("Differences between string length in code and in memory: $escapedLenDiff")
}

fun main() {
    printResult("input.txt")
}