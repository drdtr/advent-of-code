package advent_of_code_2016.day03

import Util.readInputLines

/**
 * [Squares With Three Sides](https://adventofcode.com/2016/day/3)
 *
 * For a given list of number triplets, count the ones than can be sides of a triangle, i.e.,
 * all triplets `(a, b, c)` such that `a + b > c` and `a + c > b` and `b + c > a`.
 *
 * ### Part 1
 * Each input line contains a triplet
 *
 * ### Part 2
 * Each group of three input lines contains three triplets, one in each column
 */
class TriangleChecker {
    fun countPossibleTriangles(sideTriplets: List<Triple<Int, Int, Int>>): Int =
        sideTriplets.count { (a, b, c) -> a + b > c && a + c > b && b + c > a }
}

private fun readInput(inputFileName: String): List<Triple<Int, Int, Int>> =
    readInputLines(2016, 3, inputFileName)
            .map { it.split(" ").filterNot { it.isBlank() }.map { it.toInt() } }
            .map { (a, b, c) -> Triple(a, b, c) }


private fun printResult(inputFileName: String) {
    val sideTriplets = readInput(inputFileName)
    val solver = TriangleChecker()
    val res1 = solver.countPossibleTriangles(sideTriplets)
    println("Number of valid triangles: $res1")

    val sideTripletsInGroupsOfThreeVertically = sideTriplets.chunked(3).map { (t1, t2, t3) ->
        val (a1, b1, c1) = t1
        val (a2, b2, c2) = t2
        val (a3, b3, c3) = t3
        listOf(Triple(a1, a2, a3), Triple(b1, b2, b3), Triple(c1, c2, c3))
    }.flatten()
    val res2 = solver.countPossibleTriangles(sideTripletsInGroupsOfThreeVertically)
    println("Number of valid triangles, for sides in vertical groups of three: $res2")
}

fun main() {
    printResult("input.txt")
}