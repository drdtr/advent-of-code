package advent_of_code_2016.day07

import Util.readInputLines

/**
 * [Internet Protocol Version 7 - Part 1](https://adventofcode.com/2016/day/7)
 *
 * Given a string of the form `substring1[substring2]...` with substrings inside and outside square brackets,
 * check for the presence of a substring the form `ABBA`, e.g., `cddc`, where the first and second letter are different
 * and the second pair is the reverse of the first pair.
 *
 * A string is called an `ABBA` string, if it contains an `ABBA` substring outside square brackets
 * and doesn't contain an `ABBA` letter pair inside square brackets.
 *
 * For example:
 *
 * |String | `ABBA` | Explanation |
 * |-------|:--------:|-------------|
 * |`abba[mnop]qrst` | yes | contains `abba` outside brackets |
 * |`abcd[bddb]xyyx` | no | contains `bddb` inside brackets |
 * |`aaaa[qwer]tyui` | no | doesn't contain an `ABBA` substring outside brackets |
 * |`ioxxoj[asdfgh]zxcvbn` | yes | contains `oxxo` outside brackets |
 *
 *
 * Given a list of strings, count the number of `ABBA` strings.
 */
class CheckLetterPairs {
    fun countAbbaStrings(strings: Collection<String>): Int =
        strings.count { containsAbbaOutsideAndNoAbbaInsideBrackets(it) }

    fun containsAbbaOutsideAndNoAbbaInsideBrackets(s: String): Boolean {
        var isInsideBrackets = false
        var foundOutside = false
        for (i in 0 until s.length - 3) {
            when (s[i]) {
                '['  -> isInsideBrackets = true
                ']'  -> isInsideBrackets = false
                else -> if (s.hasAbbaAt(i)) {
                    if (isInsideBrackets)
                        return false
                    else
                        foundOutside = true
                }
            }
        }
        return foundOutside
    }

    private fun String.hasAbbaAt(pos: Int): Boolean =
        pos + 3 < length && this[pos] == this[pos + 3] && this[pos] != this[pos + 1] && this[pos + 1] == this[pos + 2]
}


private fun readInput(inputFileName: String): List<String> =
    readInputLines(2016, 7, inputFileName)

private fun printResult(inputFileName: String) {
    val messages = readInput(inputFileName)
    val solver = CheckLetterPairs()
    val res = solver.countAbbaStrings(messages)
    println("Count of strings with 'ABBA' outside and no 'ABBA' inside brackets: $res")
}

fun main() {
    printResult("input.txt")
}