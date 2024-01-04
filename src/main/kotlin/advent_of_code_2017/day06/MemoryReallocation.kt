package advent_of_code_2017.day06

import Util.readInputLines
import java.math.BigInteger

/**
 * [Memory Reallocation](https://adventofcode.com/2017/day/6)
 *
 * Given memory bank sizes as an integer array, the memory blocks are redistributed as follows:
 * in each cycle, the memory bank with the most blocks (ties broken by smallest memory bank number) is taken,
 * all blocks are removed, and then put one by one starting with the next block.
 *
 * How many redistribution cycles will it take to encounter an already reached distribution?
 */
class MemoryReallocation {
    fun minStepsUntilRepetition(memoryBankSizes: List<Int>): Int =
        redistributeUntilRepetition(memoryBankSizes).totalSteps

    fun lengthOfRepetitionLoop(memoryBankSizes: List<Int>): Int =
        redistributeUntilRepetition(memoryBankSizes).loopLength

    private data class StepsToRepetition(val totalSteps: Int, val loopLength: Int)

    private fun redistributeUntilRepetition(memoryBankSizes: List<Int>): StepsToRepetition {
        val a = memoryBankSizes.toIntArray()
        // calculate the base for folding memoryBankSizes into one BigInteger -
        // we're confident that we'll never have a bank size greater than max * 2
        val base = (a.max() * 2).toBigInteger()
        val visitedDistributions = hashMapOf<BigInteger, Int>()

        var count = 0
        var distribution = a.foldToBigInteger(base)
        do {
            visitedDistributions[distribution] = count

            a.redistribute()
            count++
            distribution = a.foldToBigInteger(base)
        } while (distribution !in visitedDistributions)
        val prevCount = visitedDistributions[distribution]!!
        return StepsToRepetition(totalSteps = count, loopLength = count - prevCount)
    }

    private fun IntArray.redistribute() {
        var maxIdx = 0
        for (i in indices) {
            if (this[maxIdx] < this[i]) maxIdx = i
        }

        var remaining = this[maxIdx]
        this[maxIdx] = 0
        var i = maxIdx
        while (remaining > 0) {
            i = (i + 1) % size
            this[i]++
            remaining--
        }
    }

    private fun IntArray.foldToBigInteger(base: BigInteger): BigInteger =
        fold(BigInteger.ZERO) { res, a ->
            res * base + a.toBigInteger().also { require(it < base) { "$it was bigger than base $base" } }
        }
}

private fun readInput(inputFileName: String): List<Int> =
    readInputLines(2017, 6, inputFileName)[0].split(Regex("\\s+")).map { it.toInt() }

private fun printResult(inputFileName: String) {
    val memoryBankSizes = readInput(inputFileName)
    val solver = MemoryReallocation()
    // part 1
    val res1 = solver.minStepsUntilRepetition(memoryBankSizes)
    println("Number of steps to encounter a visited distribution: $res1")
    // part 1
    val res2 = solver.lengthOfRepetitionLoop(memoryBankSizes)
    println("Length of the distribution repetition loop: $res2")
}

fun main() {
    printResult("input.txt")
}
