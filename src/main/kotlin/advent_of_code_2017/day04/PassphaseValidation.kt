package advent_of_code_2017.day04

import Util.readInputLines

/**
 * [High-Entropy Passphrases](https://adventofcode.com/2017/day/4)
 *
 * Count valid passphrases in the input.
 *
 * ### Part 1
 *
 * A passphrase is valid if it contains no duplicate words.
 *
 * Example:
 * - `aa bb cc dd ee` is valid.
 * - `aa bb cc dd aa` is not valid
 * - `aa bb cc dd aaa` is valid
 *
 * ### Part 2
 *
 * A passphrase is valid if it contains no words that are anagrams of each other,
 * i.e. there are no two words whose letters can be rearranged to make them equal.
 *
 * Example:
 * - `abcde fghij` is valid
 * - `abcde xyz ecdab` is not valid - `abcde` and `ecdab` are anagrams
 * - `a ab abc abd abf abj` is valid
 * - `iiii oiii ooii oooi oooo` is valid
 * - `oiii ioii iioi iiio` is not valid - all words are anagrams of each other
 *
 *
 */
class PassphaseValidation {
    fun countValidPassphrasesContainingNoDuplicates(passphrases: Collection<String>) =
        passphrases.count(::containsNoDuplicates)

    fun countValidPassphrasesContainingNoAnagrams(passphrases: Collection<String>) =
        passphrases.count(::containsNoAnagrams)

    fun containsNoDuplicates(s: String): Boolean {
        val words = s.split(" ")
        return words.size == words.toSet().size
    }

    fun containsNoAnagrams(s: String): Boolean {
        val words = s.split(" ")
        val letterCountsSet = hashSetOf<LowercaseLetterCounts>()
        for (word in words) {
            val letterCounts = LowercaseLetterCounts.of(word)
            if (letterCounts in letterCountsSet) return false
            letterCountsSet += letterCounts
        }
        return true
    }

    private class LowercaseLetterCounts {
        private val charCounts = IntArray(26)

        fun incCount(ch: Char) = charCounts[ch - 'a']++

        override fun equals(other: Any?): Boolean =
            other is LowercaseLetterCounts && charCounts.contentEquals(other.charCounts)

        override fun hashCode(): Int = charCounts.contentHashCode()

        companion object {
            fun of(s: String) = LowercaseLetterCounts().apply { s.forEach { ch -> incCount(ch) } }
        }
    }
}

private fun readInput(inputFileName: String): List<String> =
    readInputLines(2017, 4, inputFileName)

private fun printResult(inputFileName: String) {
    val passphrases = readInput(inputFileName)
    val solver = PassphaseValidation()

    // part 1
    val res1 = solver.countValidPassphrasesContainingNoDuplicates(passphrases)
    println("Number of valid passphrases containing no duplicates: $res1")

    // part 2
    val res2 = solver.countValidPassphrasesContainingNoAnagrams(passphrases)
    println("Number of valid passphrases containing no anagrams: $res2")
}

fun main() {
    printResult("input.txt")
}
