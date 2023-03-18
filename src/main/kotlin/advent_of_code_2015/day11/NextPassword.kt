package advent_of_code_2015.day11

import Util.readInputLines

/**
 * [Corporate Policy](https://adventofcode.com/2015/day/11)
 *
 * A password is valid if it meets all following conditions:
 * - it consists of 8 lower-case letters
 * - it includes at least one increasing straight of at least three letters without skipping letters,
 * e.g. `abc`, `cde`, but not `abd`.
 * - it does not contain letters `i`, `o`, `l`
 * - it contains at least two different, non-overlapping pairs of letters, e.g. `aa`, `bb`
 *
 * For a given current password, compute the lexicographically smallest next valid password.
 */
class NextPassword {
    fun nextValidPassword(currPwd: String): String {
        val chars = currPwd.toCharArray()
        do {
            incPassword(chars)
        } while (!isValidPassword(chars))
        return String(chars)
    }

    private fun incPassword(chars: CharArray) {
        for (i in chars.size - 1 downTo 0) {
            if (++chars[i] <= 'z') break // no overflow
            chars[i] = 'a' // overflow, reset the position to the smallest allowed char `a` and proceed
        }
    }

    fun isValidPassword(chars: CharArray): Boolean =
        doesNotContainAnyOfIOL(chars) && hasIncreasingThreeLetters(chars) && hasTwoNonOverlappingPairs(chars)

    private fun hasIncreasingThreeLetters(chars: CharArray): Boolean {
        for (i in 0 until chars.size - 2) {
            if (chars[i] + 1 == chars[i + 1] && chars[i] + 2 == chars[i + 2]) return true
        }
        return false
    }

    private fun doesNotContainAnyOfIOL(chars: CharArray): Boolean =
        chars.none { it == 'i' || it == 'l' || it == 'o' }

    private fun hasTwoNonOverlappingPairs(chars: CharArray): Boolean {
        var pairCount = 0
        var i = 1
        while (i < chars.size) {
            if (chars[i - 1] == chars[i]) {
                pairCount++
                if (pairCount == 2) return true
                i += 2
            } else {
                i++
            }
        }

        return false
    }
}

private fun readInput(inputFileName: String): String {
    return readInputLines(2015, 11, inputFileName)[0]
}

private fun printResult(inputFileName: String) {
    val s = readInput(inputFileName)
    val solver = NextPassword()
    var currPwd = s
    repeat(2) {
        val nextPwd = solver.nextValidPassword(currPwd)
        println("Next valid password after $currPwd: $nextPwd")
        currPwd = nextPwd
    }
}

fun main() {
    printResult("input.txt")
}