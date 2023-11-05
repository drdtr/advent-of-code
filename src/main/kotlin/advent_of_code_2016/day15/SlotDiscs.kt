package advent_of_code_2016.day15

import Util.readInputLines
import advent_of_code_2016.day15.SlotDiscs.*
import java.util.regex.Pattern

/**
 * [Timing is Everything](https://adventofcode.com/2016/day/15)
 *
 * There are `n` vertically arranged rotating discs. Each disc `i` has `k_i` positions.
 * Each disc rotates `1` position per second.
 * In position `0`, each disc has a slot, through which a capsule can fall through.
 *
 * You can drop a capsule by pressing a button - the capsule falls one disc per second,
 * i.e. the time offset for disc `i`, starting at disc `0`, is `i + 1` (1 second for disc 0, etc.)
 * It only falls through a disc if it passes through the slot at position `0`.
 *
 * What is the earliest time you can drop the capsule so that it passes through all discs.
 *
 * ### Idea
 *
 * Let's assume that we drop the capsule at time `t`. Then it can fall through all discs iff, for each disc `i`:
 * ```
 * (1)  (t + offset_i + initPos_i) % k_i == 0
 * ```
 * or, the other way around
 * ```
 * (2)  t = k_i * d_i - offset_i - initPos_i
 * ```
 * for some integer `d_i`.
 *
 * We can take the disc with the largest `k_i` for the equation `(2)` and use equation `(1)`
 * to check whether the other discs would also be in the right position for the capsule to fall through the slot.
 */
class SlotDiscs {
    data class Disc(val timeOffset: Int, val numPositions: Int, val initialPosition: Int)

    fun findEarliestTimeToDropCapsule(discs: Iterable<Disc>): Int {
        val sortedDiscs = discs.sortedByDescending { it.numPositions }
        val maxDisc = sortedDiscs[0]
        val remainingDiscs = sortedDiscs.subList(1, sortedDiscs.size)
        for (d in 1 until Int.MAX_VALUE) {
            val t = maxDisc.numPositions * d - maxDisc.timeOffset - maxDisc.initialPosition
            if (remainingDiscs.all { (t + it.timeOffset + it.initialPosition) % it.numPositions == 0 }) {
                return t
            }
        }
        error("Couldn't find a time to drop the capsule so it passes through the slots of all discs.")
    }
}

private val discParsingPattern =
    Pattern.compile("Disc #(\\d+) has (\\d+) positions; at time=0, it is at position (\\d+).*")

private fun parseDisc(s: String): Disc = with(discParsingPattern.matcher(s)) {
    require(matches()) { "Not a valid disc string: $s" }
    return Disc(
        timeOffset = group(1).toInt(),
        numPositions = group(2).toInt(),
        initialPosition = group(3).toInt(),
    )
}

private fun readInput(inputFileName: String): List<Disc> =
    readInputLines(2016, 15, inputFileName).map { s -> parseDisc(s) }

private fun printResult(inputFileName: String) {
    val solver = SlotDiscs()
    val discs = readInput(inputFileName)
    println(discs.joinToString(separator = "\n"))
    val res = solver.findEarliestTimeToDropCapsule(discs)
    println("Earliest time to drop the capsule for input $inputFileName: $res")
}

fun main() {
    printResult("input1.txt")
    printResult("input2.txt")
}