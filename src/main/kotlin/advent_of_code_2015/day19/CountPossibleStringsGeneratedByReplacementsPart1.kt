package advent_of_code_2015.day19

import Util.readInputLines
import advent_of_code_2015.day19.ReplacementsInputReader.readInput

/**
 * [Medicine for Rudolph](https://adventofcode.com/2015/day/19)
 *
 * Given a string `s` and a collection of possible letter replacement steps,
 * count the number of strings that can be obtained
 * by applying any of these replacements exactly once at any matching position of `s`.
 *
 * For example, given a string `HOH` and replacements
 * - `H => HO`
 * - `H => OH`
 * - `O => HH`
 *
 * following five strings could be generated
 * - `HOOH` (via H => HO on the first H).
 * - `HOHO` (via H => HO on the second H).
 * - `OHOH` (via H => OH on the first H).
 * - `HOOH` (via H => OH on the second H).
 * - `HHHH` (via O => HH).
 *
 */
class CountPossibleStringsGeneratedByReplacementsPart1 {
    fun countGeneratedByReplacement(s: String, replacements: Map<String, List<String>>): Int {
        val possibleStrings = hashSetOf<String>()

        val substringFrom = Array(s.length + 1) { i -> if (i < s.length) s.substring(i until s.length) else "" }

        for (i in s.indices) {
            val substringTill = s.substring(0 until i)
            for ((src, destList) in replacements) {
                if (substringFrom[i].startsWith(src)) {
                    for (dest in destList) {
                        possibleStrings.add(substringTill + dest + substringFrom[i + src.length])
                    }
                }
            }
        }

        return possibleStrings.size
    }
}

private fun printResult(inputFileName: String) {
    val (s, replacements) = readInput(inputFileName)
    val solver = CountPossibleStringsGeneratedByReplacementsPart1()
    val res = solver.countGeneratedByReplacement(s, replacements)
    println("Possible generated strings count: $res")
}

fun main() {
    printResult("input.txt")
}