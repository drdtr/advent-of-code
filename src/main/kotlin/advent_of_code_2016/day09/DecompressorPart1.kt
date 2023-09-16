package advent_of_code_2016.day09

import Util.readInputLines

/**
 * [Explosives in Cyberspace - Part 1](https://adventofcode.com/2016/day/9)
 *
 * For a compressed string where a marker of the form `(mxn)` means repeat the next `m` characters for `n` times,
 * return the length of the decompressed string, ignoring whitespaces.
 *
 * Examples:
 * - `ADVENT` has no markers and has decompressed length of 6
 * - `A(1x5)BC` repeats B for 5 times, and decompresses to `ABBBBBC` with length 7
 * - `(3x3)XYZ` decompresses to `XYZXYZXYZ` with length 9
 * - `A(2x2)BCD(2x2)EFG` decompresses to `ABCBCDEFEFG` with length 11
 * - `(6x1)(1x3)A` becomes `(1x3)A` because it's the substring of length 6 that is repeated 1 time.
 * - `X(8x2)(3x3)ABCY` becomes `X(3x3)ABC(3x3)ABCY` with length 18
 */
class DecompressorPart1 {
    private data class RepetitionMarker(val len: Int, val times: Int)

    fun calcDecompressedLength(s: String): Int = DecompressedLengthCalculator(s).calcDecompressedLength()

    private class DecompressedLengthCalculator(val s: String) {
        private var pos = 0

        fun calcDecompressedLength(): Int {
            var decompressedLen = 0
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

        fun parseRepetitionMarker(): RepetitionMarker {
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
    val solver = DecompressorPart1()
    val res = solver.calcDecompressedLength(compressedString)
    println("Length of decompressed string: $res")
}

fun main() {
    printResult("input.txt")
}