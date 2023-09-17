package advent_of_code_2016.day09

import Util.readInputLines
import java.util.*

/**
 * [Explosives in Cyberspace - Part 1](https://adventofcode.com/2016/day/9)
 *
 * ## Part 1
 *
 * For a compressed string where a marker of the form `(mxn)` means repeat the next `m` characters for `n` times,
 * return the length of the decompressed string, ignoring whitespaces.
 *
 * **Examples**:
 * - `ADVENT` has no markers and has decompressed length of 6
 * - `A(1x5)BC` repeats B for 5 times, and decompresses to `ABBBBBC` with length 7
 * - `(3x3)XYZ` decompresses to `XYZXYZXYZ` with length 9
 * - `A(2x2)BCD(2x2)EFG` decompresses to `ABCBCDEFEFG` with length 11
 * - `(6x1)(1x3)A` becomes `(1x3)A` because it's the substring of length 6 that is repeated 1 time.
 * - `X(8x2)(3x3)ABCY` becomes `X(3x3)ABC(3x3)ABCY` with length 18
 *
 *
 * ## Part 2
 *
 * Now, the markers are recursive, i.e., a marker in a decompressed fragment must also be decompressed.
 *
 * **Examples**:
 * - `(3x3)XYZ` will still become `XYZXYZXYZ` with length 9
 * - `X(8x2)(3x3)ABCY` decompresses first to `X(3x3)ABC(3x3)ABCY` and then to `XABCABCABCABCABCABCY`
 * with total of 6 `ABC` substrings and length 20
 * - `(27x12)(20x12)(13x14)(7x10)(1x12)A` decompresses into a string of `A` repeated 241920 times
 * - `(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN` decompressed into a string of length 445
 */
class Decompressor {
    private data class RepetitionMarker(val len: Int, val times: Int)
    private data class RepetitionMarkerEndIndex(val marker: RepetitionMarker, val endIndexExcl: Int)

    fun calcDecompressedLength(s: String): Long =
        DecompressedLengthCalculator(s).calcDecompressedLength()

    fun calcDecompressedLengthForRecursiveMarkers(s: String): Long =
        DecompressedLengthCalculator(s).calcDecompressedLengthForRecursiveMarkers()

    private class DecompressedLengthCalculator(val s: String) {
        private var pos = 0

        fun calcDecompressedLength(): Long {
            var decompressedLen = 0L
            while (pos < s.length) {
                if (s[pos] == '(') {
                    val marker = parseRepetitionMarker()
                    val substr = s.substring(pos, pos + marker.len)
                    val decompressedSubstrLen = substr.count { !it.isWhitespace() }
                    decompressedLen += decompressedSubstrLen * marker.times
                    pos += marker.len
                } else {
                    decompressedLen++
                    pos++
                }
            }
            return decompressedLen
        }

        fun calcDecompressedLengthForRecursiveMarkers(): Long {
            var decompressedLen = 0L
            // note we assume during parsing that a repetition substring doesn't end within a marker
            // because it could produce a marker like e.g. `(10x(10x20)`, which is either malformed,
            // or would require are more elaborate parsing that would be able to disregard a substring
            // that looks like a beginning of a marker and would notice that the 2nd opening parentheses
            // is the beginning of a well-formed marker.
            val activeMarkers = PriorityQueue<RepetitionMarkerEndIndex>(compareBy { it.endIndexExcl })
            var currentFactor = 1L
            while (pos < s.length) {
                if (s[pos] == '(') {
                    val marker = parseRepetitionMarker()
                    val endIndexExcl = pos + marker.len
                    activeMarkers.add(RepetitionMarkerEndIndex(marker, endIndexExcl))
                    currentFactor *= marker.times
                } else {
                    if (!s[pos].isWhitespace()) {
                        decompressedLen += currentFactor
                    }
                    pos++
                }
                while (activeMarkers.isNotEmpty() && activeMarkers.peek().endIndexExcl <= pos) {
                    currentFactor /= activeMarkers.poll()!!.marker.times
                }
            }
            return decompressedLen
        }


        private fun parseRepetitionMarker(): RepetitionMarker {
            val openingParenPos = pos
            val xPos = s.indexOf('x', openingParenPos + 1)
            val closingParenPos = s.indexOf(')', xPos + 1)
            pos = closingParenPos + 1
            return RepetitionMarker(
                len = s.substring(openingParenPos + 1, xPos).toInt(),
                times = s.substring(xPos + 1, closingParenPos).toInt()
            )
        }
    }
}


private fun readInput(inputFileName: String): String =
    readInputLines(2016, 9, inputFileName)[0]

private fun printResult(inputFileName: String) {
    val compressedString = readInput(inputFileName)
    val solver = Decompressor()
    val res1 = solver.calcDecompressedLength(compressedString)
    println("Length of decompressed string: $res1")
    val res2 = solver.calcDecompressedLengthForRecursiveMarkers(compressedString)
    println("Length of decompressed string for recursive markers: $res2")
}

fun main() {
    printResult("input.txt")
}