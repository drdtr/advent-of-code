package advent_of_code_2017.day10

import Util.readInputLines

/**
 * [Knot Hash](https://adventofcode.com/2017/day/10)
 *
 * Calculate the knot hash of an integer list, given a list of integer lengths.
 * Start at position `0` with `skip` 0 and then
 * for each `len` in the array of lengths, "knot" the list as follows:
 * - pick the range `R` starting at `pos` with length `len`, cyclically wrapping around the end of the list
 * - reverse the elements in the range `R`
 * - advance `pos` by `len + skip`, wrapping around the end of the list
 * - increase `skip`
 *
 * Example with integer list `[0, 1, 2, 3, 4]` and lengths `[3, 4, 1, 5]`.
 * - start with `pos = 0`, `skip = 0`
 * - step 0: `len = 3`
 *    - reverse the range `0..2` obtaining `[2, 1, 0, 3, 4]`
 *    - pos = 3
 *    - skip = 1
 * - step 1: `len = 4`
 *    - reverse the range `3,4,0,1`, which wraps around the end of the list, obtaining `[4, 3, 0, 1, 2]`
 *    - pos = 3
 *    - skip = 2
 * - step 2: `len = 1`
 *    - reversing of a range of length 1 doesn't change the list
 *    - pos = 1
 *    - skip = 3
 * - step 3: `len = 5`
 *    - reverse the range `1,2,3,4,0`, which wraps around the end of the list, obtaining `[3, 4, 2, 1, 0]`
 *    - pos = 4
 *    - skip = 4
 *
 *
 * ### Part 1
 * Calculate the knot hash as product of the first two numbers in the after knotting the list.
 *
 * ### Part 2
 * - To obtain the `lengths`, interpret the input as string, convert it to bytes, and use each byte as length.
 * Then append suffix `[17, 31, 73, 47, 23]`.
 * - Perform 64 rounds of knotting the list for the given `lengths`.
 * - Split the resulting knotted list into 16 blocks and calculate the `XOR` of the bytes in each block.
 * - Returns the resulting `XOR` values of the blocks as hexadecimal string.
 *
 */
class KnotHash {
    fun calcKnotHash(initNums: List<Int>, lengths: List<Int>, rounds: Int = 1): Int =
        knotList(initNums, lengths, rounds).let { it[0] * it[1] }

    fun calcHexadecimalKnotHash(
        initNums: List<Int>,
        lengths: List<Int>,
        rounds: Int = 1,
        numOfBlocks: Int = 16,
    ): String {
        val knotList = knotList(initNums, lengths, rounds)

        val blockLen = knotList.size / numOfBlocks
        val lastBlockLen = knotList.size - blockLen * (numOfBlocks - 1)
        val xorBlocks = IntArray(numOfBlocks) { i ->
            val currBlockLen = if (i < numOfBlocks - 1) blockLen else lastBlockLen
            knotList.xorRange(i * blockLen, currBlockLen)
        }
        return xorBlocks.joinToString(separator = "") { "%02x".format(it) }
    }

    fun knotList(initNums: List<Int>, lengths: List<Int>, rounds: Int = 1): List<Int> {
        val nums = initNums.toIntArray()
        var pos = 0
        var skip = 0
        repeat(rounds) {
            for (len in lengths) {
                nums.reverseRangeCyclically(beginIndex = pos, rangeLength = len)
                pos = (pos + len + skip) % nums.size
                skip++
            }
        }
        return nums.toList()
    }

    private fun IntArray.reverseRangeCyclically(beginIndex: Int, rangeLength: Int) {
        var i = beginIndex
        var j = (beginIndex + rangeLength - 1) % size
        repeat(rangeLength / 2) {
            swap(i++, j--)
            if (i == size) i = 0
            if (j == -1) j = size - 1
        }
    }

    private fun IntArray.swap(i: Int, j: Int) {
        val t = this[i]
        this[i] = this[j]
        this[j] = t
    }

    private fun List<Int>.xorRange(beginIndex: Int, rangeLength: Int): Int {
        var res = 0
        for (i in beginIndex until beginIndex + rangeLength) {
            res = res xor this[i]
        }
        return res
    }
}

private fun readInput(inputFileName: String): String =
    readInputLines(2017, 10, inputFileName)[0].trim()

private fun printResult(inputFileName: String) {
    val inputString = readInput(inputFileName)
    val nums = (0..255).toList()
    val solver = KnotHash()

    // part 1
    val lengths1 = inputString.split(",").map { it.toInt() }
    val res1 = solver.calcKnotHash(nums, lengths1)
    println("Knot hash: $res1")

    // part 1
    val lengthsSuffix = listOf(17, 31, 73, 47, 23)
    val lengths2 = inputString.toByteArray().map { it.toInt() } + lengthsSuffix
    val res2 = solver.calcHexadecimalKnotHash(nums, lengths2, rounds = 64, numOfBlocks = 16)
    println("Hexadecimal knot hash: $res2")
}

fun main() {
    printResult("input.txt")
}
