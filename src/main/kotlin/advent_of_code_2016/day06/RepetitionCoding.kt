package advent_of_code_2016.day06

import Util.readInputLines
import advent_of_code_2016.day06.RepetitionCoding.CharacterRestoringMode.*

/**
 * [Signals and Noise](https://adventofcode.com/2016/day/6)
 *
 * Restore the message, consisting of lowercase ASCII letters, that's transmitted through a noisy channel
 * using [https://en.wikipedia.org/wiki/Repetition_code](repetition code) for error correction.
 *
 * ### Part 1
 *
 * Given a list of repeated messages, restore the correct message by finding the most frequent character
 * on each position of the message.
 *
 * For example, for the repeated messages
 * ```
 *   eedadn
 *   drvtee
 *   eandsr
 *   raavrd
 *   atevrs
 *   tsrnev
 *   sdttsa
 *   rasrtv
 *   nssdts
 *   ntnada
 *   svetve
 *   tesnvt
 *   vntsnd
 *   vrdear
 *   dvrsen
 *   enarar
 * ```
 * the restored messages is `easter`.
 *
 * ### Part 2
 *
 * Now, the correct message can be restored by finding the _least_ frequent character for each message position.
 * In the example above, this would be `advent`.
 */
class RepetitionCoding {
    enum class CharacterRestoringMode {
        MostFrequentCharacter, LeastFrequentCharacter;
    }

    fun restoreUsingRepetitionCode(
        messages: Collection<String>,
        characterRestoringMode: CharacterRestoringMode
    ): String {
        val messageLength = messages.first().length
        val restoredChars = when (characterRestoringMode) {
            MostFrequentCharacter  -> CharArray(messageLength) { pos -> mostFrequentCharAtPosition(pos, messages) }
            LeastFrequentCharacter -> CharArray(messageLength) { pos -> leastFrequentCharAtPosition(pos, messages) }
        }
        return String(restoredChars)
    }

    private fun mostFrequentCharAtPosition(pos: Int, messages: Collection<String>): Char {
        val frequencies = getCharFrequenciesAtPos(pos, messages)
        return 'a' + frequencies.indices.maxBy { i -> frequencies[i] }
    }

    private fun leastFrequentCharAtPosition(pos: Int, messages: Collection<String>): Char {
        val frequencies = getCharFrequenciesAtPos(pos, messages)
        // we need to ignore frequencies[i] == 0, because the corresponding character didn't occur
        return 'a' + frequencies.indices.minBy { i -> frequencies[i].let { if (it > 0) it else Int.MAX_VALUE } }
    }

    private fun getCharFrequenciesAtPos(pos: Int, messages: Collection<String>): IntArray = IntArray(26).apply {
        for (m in messages) {
            this[m[pos] - 'a']++
        }
    }
}

private fun readInput(inputFileName: String): List<String> =
    readInputLines(2016, 6, inputFileName)

private fun printResult(inputFileName: String) {
    val messages = readInput(inputFileName)
    val solver = RepetitionCoding()
    val res1 = solver.restoreUsingRepetitionCode(messages, MostFrequentCharacter)
    println("Restored message using most frequent characters: $res1")
    val res2 = solver.restoreUsingRepetitionCode(messages, LeastFrequentCharacter)
    println("Restored message using least frequent characters: $res2")
}

fun main() {
    printResult("input.txt")
}