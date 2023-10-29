package advent_of_code_2016.day14

import Util.md5
import Util.readInputLines

/**
 * [One-Time Pad](https://adventofcode.com/2016/day/14)
 *
 * You're using a [One-time pad](https://en.wikipedia.org/wiki/One-time_pad) to generate encryption keys.
 *
 * ### Part 1
 *
 * You get a stream of random data as follows:
 * - take the [MD5](https://en.wikipedia.org/wiki/MD5)
 * of a pre-arranged [salt](https://en.wikipedia.org/wiki/Salt_(cryptography))
 * - use an increasing integer index, starting with 0, in decimal representation
 * - take the resulting MD5 hash as lowercase hexadecimal string
 * - an MD5 hash is a key *only if*:
 *    - it contains *three* equal characters in a row, e.g. `777` (we consider only the furst such triplet)
 *    one of the next 1000 hashes contains the same character *five* times in a row, e.g. `77777`.
 *
 *
 * Examples:
 * - Pre-arranged salt `abc`
 * - the first index to produce a triplet is 18, because the MD5 hash of `abc18` contains `...cc38887a5....`.
 * However, the index 18 doesn't count as key for the one-time pad, because none of the next thousand hashes
 * (i.e., for indices `19..1018`) contains `88888`.
 * - the next index to produce a triplet is `39` because the has of `abc39` contains `eee`.
 * It's also the first key because the hash with index `816`, i.e. on of the next 1000 hashes, contains `eeeee`.
 * - the next index is `92`
 * - the index `22728` is the one that generates the 64th key
 *
 *
 * For a given salt input, what index will produce your 64th one-time pad key?
 *
 *
 * ### Part 3
 *
 * You add [key stretching](https://en.wikipedia.org/wiki/Key_stretching):
 * instead of applying the `MD5` hash function once, you apply 2016 additional hashing, i.e.,
 * the original string will be hashed 2017 times.
 *
 *
 * Examples:
 * - Pre-arranged salt `abc`
 * - the index `22551` will produce the 64th key
 *
 *
 */
class OneTimePad {
    fun getIndexProducingNthKey(
        salt: String,
        keyNumber: Int,
        numOfNextHashes: Int = 1000,
        numOfMd5HashReps: Int = 1,
    ): Int {
        var keysFound = 0
        // the next hashes, stored in a circular list, so adding a hash automatically removes the oldest hash
        val nextHashes = CircularArrayList<Set<Char>>(numOfNextHashes)
        // tracks whether a given char is contained in any of the char sets in nextHashes
        val charCounts = AsciiCharCounts()
        for (index in 0 until numOfNextHashes) {
            val hashCharsProducingQuintuplets =
                calcHash(salt + index, numOfMd5HashReps).getAllCharsFromSameCharQuintuplets()
            nextHashes += hashCharsProducingQuintuplets
            hashCharsProducingQuintuplets.forEach { ch -> charCounts.incCount(ch) }
        }

        for (index in 0..Int.MAX_VALUE) {
            val hashCharsProducingQuintuplets =
                calcHash(salt + (index + numOfNextHashes), numOfMd5HashReps).getAllCharsFromSameCharQuintuplets()
            nextHashes[0].forEach { ch -> charCounts.decCount(ch) }
            hashCharsProducingQuintuplets.forEach { ch -> charCounts.incCount(ch) }
            nextHashes += hashCharsProducingQuintuplets

            val hash = calcHash(salt + index, numOfMd5HashReps)
            val tripletChar = hash.getCharFromFirstSameCharTriplet() ?: continue
            if (charCounts.getCount(tripletChar) > 0) {
                keysFound++
                if (keysFound == keyNumber) {
                    return index
                }
            }
        }
        error("Couldn't find an index producing key number $keyNumber.")
    }

    private class AsciiCharCounts() {
        private val charCounts = IntArray(128)

        fun incCount(ch: Char) = charCounts[ch.code]++

        fun decCount(ch: Char) = charCounts[ch.code]--

        fun getCount(ch: Char) = charCounts[ch.code]
    }

    private fun calcHash(s: String, numOfMd5HashReps: Int): String {
        var hash = s
        repeat(numOfMd5HashReps) {
            hash = hash.md5()
        }
        return hash
    }

    private fun CircularArrayList<Set<Char>>.anyContains(char: Char): Boolean {
        for (i in 0 until size) {
            if (char in this[i]) return true
        }
        return false
    }

    private fun String.getCharFromFirstSameCharTriplet(): Char? {
        var currChar = this[0]
        var subLen = 1
        for (i in 1 until length) {
            if (this[i] == currChar) {
                subLen++
                if (subLen == 3) return currChar
            } else {
                currChar = this[i]
                subLen = 1
            }
        }
        return null
    }

    private fun String.getAllCharsFromSameCharQuintuplets(): Set<Char> {
        val chars = hashSetOf<Char>()
        var currChar = this[0]
        var subLen = 1
        for (i in 1 until length) {
            if (this[i] == currChar) {
                subLen++
                if (subLen == 5) {
                    chars += currChar
                }
            } else {
                currChar = this[i]
                subLen = 1
            }
        }
        return chars
    }
}

private fun readInput(inputFileName: String): String =
    readInputLines(2016, 14, inputFileName)[0]

private fun printResult(inputFileName: String) {
    val salt = readInput(inputFileName)
    val solver = OneTimePad()
    val numOfNextHashes = 1000
    val keyNumber = 64
    // part 1
    val res1 = solver.getIndexProducingNthKey(salt, keyNumber, numOfNextHashes)
    println("Index producing one-time pad key number $keyNumber: $res1")
    // part 2
    val numOfMd5HashReps = 2017
    val res2 = solver.getIndexProducingNthKey(salt, keyNumber, numOfNextHashes, numOfMd5HashReps)
    println("Index producing one-time pad key number $keyNumber with key stretching: $res2")
}

fun main() {
    printResult("input.txt")
}