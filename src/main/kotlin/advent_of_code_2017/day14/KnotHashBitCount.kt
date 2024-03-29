package advent_of_code_2017.day14

import Util.readInputLines
import advent_of_code_2017.day10.KnotHash
import java.math.BigInteger

/**
 * [Disk Defragmentation](https://adventofcode.com/2017/day/14)
 *
 * Given a key string `s`, calculate the knot hashes for strings `key-n` with `n` in the range `0..127`.
 *
 * ### Part 1
 *
 * Count the total number of `1`-bits in the 128 knot hashes.
 *
 *
 * ### Part 2
 *
 * Count the number of contiguous regions / connected components of `1`-bits, where two bits are connected
 * if they are directly to the left/right/up/down from each other.
 *
 * Example:
 * ```
 * 11.2.3..-->
 * .1.2.3.4
 * ....5.6.
 * 7.8.55.9
 * .88.5...
 * 88..5..8
 * .8...8..
 * 88.8.88.
 * 8888.888
 * 88.8.888
 * .8888888-->
 * |      |
 * V      V
 * ```
 * Here, we see 8 contiguous regions. In the complete 128x128 grid, there are 1242 contiguous regions.
 *
 */
class KnotHashBitCount {
    fun countBitsInKnotHashes(lengthsList: List<List<Int>>, initNums: List<Int>): Int {
        return lengthsList.sumOf { lengths -> countBitsInKnotHash(lengths, initNums) }
    }

    private fun countBitsInKnotHash(lengths: List<Int>, initNums: List<Int>): Int {
        val knotHash = calcHexadecimalKnotHash(initNums, lengths)
        return BigInteger(/* val = */ knotHash, /* radix = */ 16).bitCount()
    }

    private data class Point(val x: Int, val y: Int)

    fun countConnectedComponents(lengthsList: List<List<Int>>, initNums: List<Int>): Int {
        val grid: List<BooleanArray> = lengthsList
                .map { lengths -> calcHexadecimalKnotHash(initNums, lengths) }
                .map { hexStringToBitArray(it) }
        val visited = List(grid.size) { i -> BooleanArray(grid[i].size) }

        fun Point.isValid() = y in visited.indices && x in visited[y].indices
        fun Point.isSet() = grid[y][x]
        fun Point.isVisited() = visited[y][x]
        fun Point.markVisited() {
            visited[y][x] = true
        }

        val stepDiffs = listOf(1 to 0, -1 to 0, 0 to 1, 0 to -1)

        fun bfs(startingPoint: Point) {
            val q = java.util.ArrayDeque<Point>()
            q.offer(startingPoint)
            startingPoint.markVisited()
            while (q.isNotEmpty()) {
                val (x, y) = q.poll()
                for ((dx, dy) in stepDiffs) {
                    val nextPoint = Point(x + dx, y + dy)
                    if (nextPoint.isValid() && nextPoint.isSet() && !nextPoint.isVisited()) {
                        q.offer(nextPoint)
                        nextPoint.markVisited()
                    }
                }
            }
        }

        var count = 0
        for (y in grid.indices) for (x in grid[y].indices) {
            val p = Point(x, y)
            if (p.isValid() && p.isSet() && !p.isVisited()) {
                bfs(p)
                count++
            }
        }
        return count
    }

    private fun hexStringToBitArray(s: String): BooleanArray =
        hexToPaddedBinaryString(s).map { it == '1' }.toBooleanArray()

    private fun hexToPaddedBinaryString(hexString: String): String {
        val bits = hexString.length * 4
        val bigInt = BigInteger(/* val = */ hexString, /* radix = */ 16)
        return "%${bits}s".format(bigInt.toString(2)).replace(" ", "0")
    }

    fun toStringKnotHashBits(lengths: List<Int>, initNums: List<Int>): String {
        val knotHash = calcHexadecimalKnotHash(initNums, lengths)
        return "$knotHash, ${hexToPaddedBinaryString(knotHash)}"
    }

    private var knotHashCalculator = KnotHash()

    private fun calcHexadecimalKnotHash(initNums: List<Int>, lengths: List<Int>): String =
        knotHashCalculator.calcHexadecimalKnotHash(initNums, lengths, rounds = 64)
}

private fun readInput(inputFileName: String): String =
    readInputLines(2017, 14, inputFileName)[0].trim()

private fun printResult(inputFileName: String) {
    val inputString = readInput(inputFileName)
    val initNums = (0..255).toList()
    val numOfRows = 128
    val solver = KnotHashBitCount()

    val lengthsSuffix = listOf(17, 31, 73, 47, 23)
    val lengthsList =
        (0 until numOfRows).map { n -> "$inputString-$n".toByteArray().map { it.toInt() } + lengthsSuffix }
    lengthsList.forEach { lengths -> println(solver.toStringKnotHashBits(lengths, initNums)) }

    // part 1
    val res1 = solver.countBitsInKnotHashes(lengthsList, initNums)
    println("Total bit count in $numOfRows hexadecimal knot hashes: $res1")

    // part 2
    val res2 = solver.countConnectedComponents(lengthsList, initNums)
    println("Total number of contiguous regions (connected component) in $numOfRows hexadecimal knot hashes: $res2")
}

fun main() {
    printResult("input.txt")
}
