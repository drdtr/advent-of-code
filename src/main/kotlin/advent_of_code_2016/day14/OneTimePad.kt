package advent_of_code_2016.day14

import Util.readInputLines
import com.twmacinta.util.MD5
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

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
        val hashCalculator = CachingConcurrentHashCalculator(salt, numOfMd5HashReps)
        for (index in 0 until numOfNextHashes) {
            val hashCharsProducingQuintuplets = hashCalculator.getHash(index).getAllCharsFromSameCharQuintuplets()
            nextHashes += hashCharsProducingQuintuplets
            hashCharsProducingQuintuplets.forEach { ch -> charCounts.incCount(ch) }
        }

        for (index in 0..Int.MAX_VALUE) {
            val hashCharsProducingQuintuplets =
                hashCalculator.getHash(index + numOfNextHashes).getAllCharsFromSameCharQuintuplets()
            nextHashes[0].forEach { ch -> charCounts.decCount(ch) }
            hashCharsProducingQuintuplets.forEach { ch -> charCounts.incCount(ch) }
            nextHashes += hashCharsProducingQuintuplets

            val hash = hashCalculator.getHash(index)
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

    /**
     * Utilizes cache and concurrency to speed up calculation of MD5 hashes.
     * Calculates hashes of strings of the form `<salt><index>`,
     * where `salt` is a fixed string and `index` is an integer.
     * Works best if most of the requested hashes are calculated for contiguous indices, e.g.,
     * `["salt0", "salt1", ..., "salt10000"]`.
     */
    private class CachingConcurrentHashCalculator(val salt: String, val numOfMd5HashReps: Int) {
        companion object {
            const val CACHE_PREFILL_SIZE = 256
        }

        private val hashCache = Collections.synchronizedMap(hashMapOf<Int, String>())
        private val currMaxCachedIndex = AtomicInteger(CACHE_PREFILL_SIZE - 1)
        private val threadPoolContextForPrefillingHashCache =
            Runtime.getRuntime().availableProcessors().let { numOfProcessors ->
                val numOfThreads = (numOfProcessors - 1).coerceAtLeast(2)
                newFixedThreadPoolContext(numOfThreads, "FixedThreadPool with $numOfThreads threads")
            }

        init {
            prefillHashCache(0 until CACHE_PREFILL_SIZE)
        }

        fun getHash(index: Int): String {
            if (index > currMaxCachedIndex.get()) {
                synchronized(this) {
                    val newMaxIndex = index + CACHE_PREFILL_SIZE
                    prefillHashCache(currMaxCachedIndex.get() + 1..newMaxIndex)
                    currMaxCachedIndex.set(newMaxIndex)
                }
            }
            return hashCache[index] ?: error(
                "Cache didn't contain element $index after prefilling. " +
                        "\nCache: ${hashCache.keys.sorted()}"
            )
        }

        private fun prefillHashCache(indices: IntRange) =
            runBlocking(threadPoolContextForPrefillingHashCache) {
                indices.forEach { launch { calcHash(it) } }
            }

        private fun calcHash(index: Int) {
            var hash = salt + index
            repeat(numOfMd5HashReps) {
                hash = MD5(hash).asHex()
            }
            hashCache[index] = hash
        }
    }

    private class AsciiCharCounts {
        private val charCounts = IntArray(128)

        fun incCount(ch: Char) = charCounts[ch.code]++

        fun decCount(ch: Char) = charCounts[ch.code]--

        fun getCount(ch: Char) = charCounts[ch.code]
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