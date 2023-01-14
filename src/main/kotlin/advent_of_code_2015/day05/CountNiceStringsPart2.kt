package advent_of_code_2015.day05

import Util.readInputLines

/**
 * [Doesn't He Have Intern-Elves For This? - Part 2](https://adventofcode.com/2015/day/5#part2)
 *
 * For a given list of strings, consisting of lowercase English letters, count the number of _nice_ strings.
 *
 * A string is _nice_ when it fulfils all following conditions:
 * - it contains a pair of two letters that appear at leas twice without overlapping, e.g. `abab` or `aabcdaa`
 * - it contains at least one letter that repeates with exactly one letter between them, e.g. `aba` or `aaa`
 *
 * Example of nice strings:
 * - `qjhvhtzxzqqjkmpb`
 * - `xxyxx`
 *
 * Example of not nice strings:
 * - `uurcxstgmygtbstg`
 * - `ieodomkazucvgmuy`
 */
class CountNiceStringsPart2 {
    fun countNiceStrings(strings: Iterable<String>): Int = strings.count { it.isNice() }

    private fun String.isNice() =
        containsLetterOccurrencesWithLetterInBetween() && containsLetterPairTwiceWithoutOverlapping()

    private fun String.containsLetterOccurrencesWithLetterInBetween() =
        (2 until length).any { i -> get(i - 2) == get(i) }

    private fun String.containsLetterPairTwiceWithoutOverlapping(): Boolean {
        val letterPairIndices = hashMapOf<String, MutableList<Int>>()
        for (i in 0..length - 2) {
            val letterPair = substring(i, i + 2)
            letterPairIndices.getOrPut(letterPair) { mutableListOf() }.add(i)
        }

        for (indices in letterPairIndices.values) {
            if (indices.size > 2)
                return true // there are more than 2 occurrences, so the first and last one will not overlap
            if (indices.size == 2 && indices[0] + 2 <= indices[1])
                return true // there are 2 occurrences and the 2nd one starts after the end of the 1st one
        }

        return false
    }
}


private fun printResult(inputFileName: String) {
    val strings = readInputLines(2015, 5, inputFileName)
    println("Number of nice strings: ${CountNiceStringsPart2().countNiceStrings(strings)}")
}

fun main() {
    printResult("input.txt")
}