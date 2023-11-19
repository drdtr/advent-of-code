package advent_of_code_2016.day21

import Util.readInputLines
import advent_of_code_2016.day21.StringScrambler.*
import kotlin.math.min
import kotlin.math.max

/**
 * [Scrambled Letters and Hash](https://adventofcode.com/2016/day/21)
 *
 * Perform a given sequence of operations on a string `s` and return the resulting string.
 * Possible operations are
 * - `swapAt(i, j)` - swap letters at positions `i` and `j`
 * - `swapLetters(x, y)` - swap letters `x` and `y`
 * - `rotateLeft(k)`, `rotateRight(k)` - rotate the string left/right by `k` positions
 * - `rotateRightByIndexOf(x)` - rotate the string right by the index of letter `x`, plus 1,
 * and additionally plus 1 if `indexOf(x) >= 4`.
 * - `reverseBetween(i, j)` - reverse the substring between indices `i` and `j`
 * - `move(i, j)` - move letter at index `i` to position `j`
 *
 * Example for `s = abcde`:
 * - `swapAt(0, 4)`: `ebcda`
 * - `swapLetters('b', 'd')`: `edcba`
 * - `reverseBetween(0, 4)`: `abcde`
 * - `rotateLeft(1)`: `bcdea`
 * - `move(1, 4)`: `bdeac`
 * - `move(3, 0)`: `abdec`
 * - `rotateRightByIndexOf('b')`: `ecabd`
 * - `rotateRightByIndexOf('d')`: `decab`
 *
 */
class StringScrambler {
    sealed interface ScrambleOp
    data class Swap(val i: Int, val j: Int) : ScrambleOp
    data class SwapLetters(val x: Char, val y: Char) : ScrambleOp
    data class RotateLeft(val k: Int) : ScrambleOp
    data class RotateRight(val k: Int) : ScrambleOp
    data class RotateRightByIndexOf(val x: Char) : ScrambleOp
    data class ReverseBetween(val i: Int, val j: Int) : ScrambleOp
    data class Move(val i: Int, val j: Int) : ScrambleOp

    fun scramble(s: String, scrambleOps: List<ScrambleOp>): String = with(s.toCharArray()) {
        for (op in scrambleOps) when (op) {
            is Swap                 -> swap(op.i, op.j)
            is SwapLetters          -> swapLetters(op.x, op.y)
            is RotateLeft           -> rotateLeft(op.k)
            is RotateRight          -> rotateRight(op.k)
            is RotateRightByIndexOf -> rotateRightByIndexOf(op.x)
            is ReverseBetween       -> reverseBetween(op.i, op.j)
            is Move                 -> move(op.i, op.j)
        }
        return String(this)
    }

    fun unscramble(s: String, scrambleOps: List<ScrambleOp>): String = with(s.toCharArray()) {
        for (op in scrambleOps.reversed()) when (op) {
            is Swap                 -> swap(op.i, op.j)
            is SwapLetters          -> swapLetters(op.x, op.y)
            is RotateLeft           -> rotateRight(op.k)
            is RotateRight          -> rotateLeft(op.k)
            is RotateRightByIndexOf -> revertRotateRightByIndexOf(op.x)
            is ReverseBetween       -> reverseBetween(op.i, op.j)
            is Move                 -> move(op.j, op.i)
        }
        return String(this)
    }

    private fun CharArray.swap(i: Int, j: Int) {
        val t = this[i]
        this[i] = this[j]
        this[j] = t
    }

    private fun CharArray.swapLetters(x: Char, y: Char) {
        swap(indexOf(x), indexOf(y))
    }

    private fun CharArray.rotateLeft(k: Int) {
        rotateRight(size - (k % size))
    }

    private fun CharArray.rotateRight(k: Int) {
        val n = size
        if (n <= 1) return

        val kMod = k % n
        if (kMod == 0) return

        val nMinusK = n - kMod
        if (kMod < n / 2) {
            // shift to the right by kMod
            val buf = copyOfRange(nMinusK, n)
            System.arraycopy(this, 0, this, kMod, nMinusK)
            System.arraycopy(buf, 0, this, 0, kMod)
        } else {
            // shift to the left by n - kMod
            val buf = copyOfRange(0, nMinusK)
            System.arraycopy(this, nMinusK, this, 0, kMod)
            System.arraycopy(buf, 0, this, kMod, nMinusK)
        }

    }

    private fun CharArray.rotateRightByIndexOf(x: Char) {
        val i = indexOf(x)
        val k = i + (if (i >= 4) 2 else 1)
        rotateRight(k)
    }

    private fun CharArray.revertRotateRightByIndexOf(x: Char) {
        val idx = indexOf(x)
        val originalIdx = indices.asSequence()
                .filter { i -> (2 * i + (if (i >= 4) 2 else 1)) % size == idx }
                .maxOrNull() // minOrNull was also thinkable, but oddly, the expected answer was the max possible index
            ?: error("Couldn't find the original index of '$x' to revert RotateRightByIndexOf")
        when {
            originalIdx < idx -> rotateLeft(idx - originalIdx)
            originalIdx > idx -> rotateRight(originalIdx - idx)
        }
    }

    private fun CharArray.reverseBetween(i: Int, j: Int) {
        var left = min(i, j)
        var right = max(i, j)
        while (left < right) {
            swap(left++, right--)
        }
    }

    private fun CharArray.move(i: Int, j: Int) {
        if (i == j) return

        val t = this[i]
        if (i < j) {
            for (p in i until j) {
                this[p] = this[p + 1]
            }
        } else { // i > j
            for (p in i downTo j + 1) {
                this[p] = this[p - 1]
            }
        }
        this[j] = t
    }
}

private fun readInput(inputFileName: String): List<ScrambleOp> {
    fun errorInvalidOp(invalidOp: String): ScrambleOp {
        error("Invalid scramble operation \"$invalidOp\"")
    }

    return readInputLines(2016, 21, inputFileName)
            .map { line ->
                val tokens = line.split(" ").filter { it.isNotEmpty() }
                when (tokens[0]) {
                    "rotate"  -> when (tokens[1]) {
                        "left"  -> RotateLeft(tokens[2].toInt())
                        "right" -> RotateRight(tokens[2].toInt())
                        "based" -> RotateRightByIndexOf(tokens[6][0])
                        else    -> errorInvalidOp(line)
                    }

                    "swap"    -> when (tokens[1]) {
                        "position" -> Swap(tokens[2].toInt(), tokens[5].toInt())
                        "letter"   -> SwapLetters(tokens[2][0], tokens[5][0])
                        else       -> errorInvalidOp(line)
                    }

                    "move"    -> Move(tokens[2].toInt(), tokens[5].toInt())
                    "reverse" -> ReverseBetween(tokens[2].toInt(), tokens[4].toInt())
                    else      -> errorInvalidOp(line)
                }
            }
}

private fun printResult(inputFileName: String) {
    val scrambleOps = readInput(inputFileName)
    val solver = StringScrambler()

    // part 1
    val s = "abcdefgh"
    val res1 = solver.scramble(s, scrambleOps)
    println("Result of scrambling $s: $res1")

    // part 2
    val scrambled = "fbgdceah"
    val res2 = solver.unscramble(scrambled, scrambleOps)
    println("Result of unscrambling $scrambled: $res2")
}

fun main() {
    printResult("input.txt")
}



