package advent_of_code_2015.day05

import Util.readInputLines

/**
 * [Doesn't He Have Intern-Elves For This? - Part 1](https://adventofcode.com/2015/day/5)
 *
 * For a given list of strings, consisting of lowercase English letters, count the number of _nice_ strings.
 *
 * A string is _nice_ when it fulfils all following conditions:
 * - it contains at least three vowels (e.g. `alice`)
 * - it contains at least one letter appearing twice in a row (e.g. `bee`)
 * - it does not contain any of the strings `ab`, `cd`, `pq`, `xy`
 *
 * Example of nice strings:
 * - `ugknbfddgicrmopn`
 * - `aaa`
 *
 * Example of not nice strings:
 * - `jchzalrnumimnmhp` because it has no double letters
 * - `haegwjzuvuyypxyu` because it contains `xy`
 * - `dvszwmarrgswjxmb` because it contains only one vowel
 *
 * See also [CountNiceStringsPart2]
 */
class CountNiceStringsPart1 {
    fun countNiceStrings(strings: Iterable<String>): Int = strings.count { it.isNice() }

    private fun String.isNice() = doesNotContainForbiddenStrings() && containsLetterPair() && containsThreeVowels()

    private fun String.containsThreeVowels(): Boolean {
        var count = 0
        for (ch in this) {
            if (ch.isVowel()) count++
            if (count == 3) return true
        }
        return false
    }

    private fun String.containsLetterPair() = (1 until length).any { i -> get(i) == get(i - 1) }

    private fun String.doesNotContainForbiddenStrings() =
        FORBIDDEN_SUBSTRINGS.none { forbiddenSub -> forbiddenSub in this }

    private fun Char.isVowel() = this == 'a' || this == 'e' || this == 'i' || this == 'o' || this == 'u'

    companion object {
        private val FORBIDDEN_SUBSTRINGS = setOf("ab", "cd", "pq", "xy")
    }
}


private fun printResult(inputFileName: String) {
    val strings = readInputLines(2015, 5, inputFileName)
    println("Number of nice strings: ${CountNiceStringsPart1().countNiceStrings(strings)}")
}

fun main() {
    printResult("input.txt")
}