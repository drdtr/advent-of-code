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
 */
class OneTimePad {
    fun getIndexProducingNthKey(salt: String, keyNumber: Int, numOfNextHashes: Int = 1000): Int {
        var keysFound = 0
        val nextHashes = CircularArrayList<Set<Char>>(numOfNextHashes)
        for (index in 0 until numOfNextHashes) {
            nextHashes += (salt + index).md5().getAllCharsFromSameCharQuintuplets()
        }

        for (index in 0..Int.MAX_VALUE) {
            nextHashes += (salt + (index + numOfNextHashes)).md5().getAllCharsFromSameCharQuintuplets()
            val hash = (salt + index).md5()
            val tripletChar = hash.getCharFromFirstSameCharTriplet() ?: continue
            if (nextHashes.anyContains(tripletChar)) {
                keysFound++
                if (keysFound == keyNumber) {
                    return index
                }
            }
        }
        error("Couldn't find an index producing key number $keyNumber.")
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
    // part 1
    val keyNumber = 64
    val res1 = solver.getIndexProducingNthKey(salt, keyNumber, numOfNextHashes)
    println("Index producing one-time pad key number $keyNumber: $res1")
}

fun main() {
    printResult("input.txt")
}