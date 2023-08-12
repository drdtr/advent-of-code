package advent_of_code_2016.day07

import Util.readInputLines

/**
 * [Internet Protocol Version 7 - Part 2](https://adventofcode.com/2016/day/7)
 *
 * Given a string of the form `substring1[substring2]...` with substrings inside and outside square brackets,
 * check for the presence of substrings of the form `ABA` and `BAB`, e.g., `bcb` and `cbc`,
 * where the first and second letter are different
 *
 * A string is called `ABA[BAB]` string if there is an `ABA` substring outside square brackets,
 * and a `BAB` substring is inside square brackets (the order of substrings doesn't matter:
 * the `BAB` string inside brackets can precede the `ABA` string outside brackets).
 *
 * For example:
 *
 * |String | `ABA[BAB]` | Explanation |
 * |-------|:--------:|-------------|
 * |`aba[bab]xyz` | yes | contains `aba` outside brackets and `bab` inside brackets |
 * |`xyx[xyx]xyx` | no | contains `xyx` inside brackets but no corresponding `yxy` |
 * |`aaa[kek]eke` | yes | contains `eke` outside brackets and `kek` inside brackets |
 * |`zazbz[bzb]cdb` | yes | contains `zbz` outside brackets and `bzb` inside brackets |
 *
 *
 * Given a list of strings, count the number of `ABA[BAB]` strings.
 */
class CheckLetterTriplets {
    fun countAbaBabStrings(strings: Collection<String>): Int =
        strings.count { containsAbaOutsideAndBabInsideBrackets(it) }

    fun containsAbaOutsideAndBabInsideBrackets(s: String): Boolean {
        var isInsideBrackets = false
        val tripletsOutside = hashSetOf<String>()
        val reversedTripletsInside = hashSetOf<String>()
        for (i in 0 until s.length - 2) {
            when (s[i]) {
                '['  -> isInsideBrackets = true
                ']'  -> isInsideBrackets = false
                else -> if (s.isAbaAt(i)) {
                    if (isInsideBrackets)
                        reversedTripletsInside.add(s.reverseTripletAt(i))
                    else
                        tripletsOutside.add(s.substring(i, i + 3))
                }
            }
        }
        return tripletsOutside.any { it in reversedTripletsInside }
    }

    private fun String.isAbaAt(pos: Int): Boolean =
        pos + 2 < length && this[pos] == this[pos + 2] && this[pos] != this[pos + 1]

    private fun String.reverseTripletAt(pos: Int) = String(charArrayOf(this[pos + 1], this[pos], this[pos + 1]))
}


private fun readInput(inputFileName: String): List<String> =
    readInputLines(2016, 7, inputFileName)

private fun printResult(inputFileName: String) {
    val messages = readInput(inputFileName)
    val solver = CheckLetterTriplets()
    val res1 = solver.countAbaBabStrings(messages)
    println("Count of strings with 'ABA' outside and 'BAB' inside brackets: $res1")
}

fun main() {
    printResult("input.txt")
}