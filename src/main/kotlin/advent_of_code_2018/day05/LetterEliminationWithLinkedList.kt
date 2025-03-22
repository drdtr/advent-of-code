package advent_of_code_2018.day05

import Util.readInputLines
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.math.abs

/**
 * [Alchemical Reduction](https://adventofcode.com/2018/day/5)
 *
 * You're given a string `s` of upper-case and lower-case letters.
 *
 * A letter pair is eliminated if the letters are the same but have difference case, e.g. `aA` or `Bb`.
 *
 * ### Part 1
 *
 * How many letters remain in `s` after all letter pair are eliminated?
 */
class LetterEliminationWithLinkedList {
    fun eliminateLetterPairs(s: String): String {
        val chars = s.mapTo(LinkedList()) { it }
        val itr = chars.listIterator()

        fun isMatchingLetterPair(a: Char, b: Char) = abs(a - b) == 'a' - 'A'

        var prevChar: Char? = null
        while (itr.hasNext()) {
            val currChar = itr.next()
            if (prevChar != null && isMatchingLetterPair(prevChar, currChar)) {
                itr.remove()
                itr.previous()
                itr.remove()
                if (itr.hasPrevious()) {
                    itr.previous()
                }
                prevChar = null
            } else {
                prevChar = currChar

            }
        }
        return String(chars.toCharArray())
    }
}

/**
 * Solution with stack, as suggested in this [dev.to post](https://dev.to/steadbytes/aoc-2018-day-5-alchemical-reduction-10g0).
 */
class LetterEliminationWithStack {
    fun eliminateLetterPairs(s: String): String {
        fun isMatchingLetterPair(a: Char, b: Char) = abs(a - b) == 'a' - 'A'

        val stack = ArrayDeque<Char>()
        for (ch in s) {
            val prevChar: Char? = stack.peek()
            if (prevChar != null && isMatchingLetterPair(prevChar, ch)) {
                stack.pop()
            } else {
                stack.push(ch)
            }
        }
        return String(stack.toCharArray().apply { reverse() })
    }

    data class MaxEliminateLetterPairsResult(val droppedLetter: Char, val s: String)

    fun shortestAfterDroppingAllOfOneLetterAndEliminatingLetterPairs(s: String): MaxEliminateLetterPairsResult =
        runBlocking {
            fun String.withoutUpperAndLowercaseLetter(ch: Char) = filterNot { it.lowercaseChar() == ch }

            val uniqueLetters = s.lowercase().toSet()
            uniqueLetters
                    .map { ch ->
                        async {
                            MaxEliminateLetterPairsResult(
                                ch,
                                eliminateLetterPairs(s.withoutUpperAndLowercaseLetter(ch))
                            )
                        }
                    }
                    .awaitAll()
                    .minBy { it.s.length }
        }
}

private fun readInput(inputFileName: String): String = readInputLines(2018, 5, inputFileName)[0]

private fun printResult(inputFileName: String) {
    val str = readInput(inputFileName)
    val solver = LetterEliminationWithStack()

    // part 1
    val res1 = solver.eliminateLetterPairs(str).length
    println("Number of letters remaining after eliminating pairs: $res1")

    // part 2
    val res2 = solver.shortestAfterDroppingAllOfOneLetterAndEliminatingLetterPairs(str)
    println("Number of letters remaining after dropping ${res2.droppedLetter} and eliminating pairs: ${res2.s.length}")
}

fun main() {
    printResult("input.txt")
}
