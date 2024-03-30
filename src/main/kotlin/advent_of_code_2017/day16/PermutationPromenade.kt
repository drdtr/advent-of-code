package advent_of_code_2017.day16

import Util.readInputLines
import advent_of_code_2017.day16.PermutationPromenade.*
import java.util.regex.Pattern

/**
 * [Permutation Promenade](https://adventofcode.com/2017/day/16)
 *
 * For an array `abc...` of `n` characters, following operations can be performed:
 * - `sk` - *Spin*, makes `k` elements move from the end to the front maintaining their order, e.g.,
 * `s3` on `abcde` produces `cdeab`.
 * - `xi/j` - *Exchange*, makes the elements at positions `i` and `j` swap places.
 * - `px/y` - *Partner*, makes the elements `x` and `y` swap places.
 *
 * Example:
 * - Start with `abcde`
 * - `s1` - spin of size results in `eabcd`.
 * - `x3/4` - swap of elements at positions 3 and 4 results in `eabdc`.
 * - `pe/b` - swapping elements `e` and `b` results in `baedc`.
 *
 *
 * ### Part 1
 * Given a sequence of operations, return the resulting order of elements.
 *
 * ### Part 2
 * Repeat the given sequence of operations 1_000_000_000 times and return the resulting order of elements.
 *
 */
class PermutationPromenade {
    sealed interface PermutationOp

    data class Spin(val size: Int) : PermutationOp
    data class Exchange(val i: Int, val j: Int) : PermutationOp
    data class Partner(val x: Char, val y: Char) : PermutationOp

    fun permute(elements: String, ops: Iterable<PermutationOp>): String =
        with(elements.toCharArray()) {
            permute(ops)
            String(this)
        }

    private fun CharArray.permute(ops: Iterable<PermutationOp>) {
        for (op in ops) when (op) {
            is Spin     -> spin(op.size)
            is Exchange -> swap(op.i, op.j)
            is Partner  -> swap(indexOf(op.x), indexOf(op.y))
        }
    }

    fun permuteRepeatedWithCycleDetection(elements: String, ops: Iterable<PermutationOp>, numOfReps: Int = 1): String {
        val charArray = elements.toCharArray()
        val encounteredResultsToRepsMap = hashMapOf<String, Int>()
                .apply { this[elements] = 0 }
        var cycleStartAndLen: Pair<Int, Int>? = null
        for (rep in 1..numOfReps) {
            charArray.permute(ops)
            val currRes = String(charArray)
            val prevRep = encounteredResultsToRepsMap[currRes]
            if (prevRep != null) {
                val cycleLen = rep - prevRep
//                println("Encountered cycle with start $prevRep and length $cycleLen")
                cycleStartAndLen = prevRep to cycleLen
                break
            } else {
                encounteredResultsToRepsMap[currRes] = rep
            }
        }

        if (cycleStartAndLen != null) {
            val (cycleStart, cycleLen) = cycleStartAndLen
            val remainingReps = (numOfReps - cycleStart) % cycleLen
            repeat(remainingReps) {
                charArray.permute(ops)
            }
        }

        return String(charArray)
    }

    private fun CharArray.spin(spinSize: Int) {
        if (spinSize <= 0 || spinSize % size == 0) return
        val len = spinSize % size
        val rightPart = copyOfRange(size - len, size)
        System.arraycopy(this, 0, this, len, size - len)
        System.arraycopy(rightPart, 0, this, 0, len)
    }

    private fun CharArray.swap(i: Int, j: Int) {
        val t = this[i]
        this[i] = this[j]
        this[j] = t
    }
}

private val spinPattern = Pattern.compile("s(\\d+)")
private val exchangePattern = Pattern.compile("x(\\d+)/(\\d+)")
private val partnerPattern = Pattern.compile("p(\\w)/(\\w)")

private fun parsePermutationOp(s: String): PermutationOp {
    with(spinPattern.matcher(s)) {
        if (matches()) return Spin(group(1).toInt())
    }
    with(exchangePattern.matcher(s)) {
        if (matches()) return Exchange(group(1).toInt(), group(2).toInt())
    }
    with(partnerPattern.matcher(s)) {
        if (matches()) return Partner(group(1)[0], group(2)[0])
    }
    error("Invalid permutation operation $s")
}

private fun readInput(inputFileName: String): List<PermutationOp> =
    readInputLines(2017, 16, inputFileName)[0].split(",").map(::parsePermutationOp)

private fun printResult(inputFileName: String) {
    val solver = PermutationPromenade()
    val ops = readInput(inputFileName)
    val chars = ('a'..'p').joinToString(separator = "")
//    println(ops.joinToString(separator = "\n"))

    // part 1
    val res1 = solver.permute(chars, ops)
    println("Permutation result: $res1")

    // part 2
    val numOfReps = 1_000_000_000
    val res2 = solver.permuteRepeatedWithCycleDetection(chars, ops, numOfReps)
    println("Permutation result after $numOfReps repetitions: $res2")
}

fun main() {
    printResult("input.txt")
}