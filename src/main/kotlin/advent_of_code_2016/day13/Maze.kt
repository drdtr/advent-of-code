package advent_of_code_2016.day13

import Util.readInputLines
import advent_of_code_2016.day13.Maze.*
import java.util.*

/**
 * [A Maze of Twisty Little Cubicles](https://adventofcode.com/2016/day/13)
 *
 * Given a 2D maze, starting at `(0, 0)` and extending indefinitely to positive `x` and `y`,
 * following rules define whether a given cell `(x, y)` is free or a wall:
 * - Calculate `N = x*x + 3*x + 2*x*y + y + y*`
 * - Add the puzzle input `k` to `N`
 * - Count the number of bits in the binary representation of `N + k`
 *    - the maze cell is free if the number of bits is **even**
 *    - otherwise, the cell is a wall.
 *
 * For a given target cell `(x, y)` find out the minimal number of steps to reach it.
 */
class Maze {
    data class Point(val x: Int, val y: Int)

    fun minStepsToReach(k: Int, startingPoint: Point, targetPoint: Point): Int? {
        val maze = CachingMaze(k)
        if (!maze.isFree(targetPoint)) return null // not reachable

        val visited = hashSetOf<Point>()
        val q = ArrayDeque<Pair<Point, Int>>()

        q.offer(startingPoint to 0)
        visited += startingPoint

        while (q.isNotEmpty()) {
            val (p, dist) = q.poll()
            for ((dx, dy) in moveDiffs) {
                val pNext = Point(p.x + dx, p.y + dy)
                if (pNext.x < 0 || pNext.y < 0 || pNext in visited || !maze.isFree(pNext)) continue
                val distNext = dist + 1
                if (pNext == targetPoint) return distNext
                q.offer(pNext to distNext)
                visited += pNext
            }
        }

        return null // not reachable
    }

    fun numberOfReachableCells(k: Int, startingPoint: Point, maxDist: Int): Int {
        var count = 0
        val maze = CachingMaze(k)

        val visited = hashSetOf<Point>()
        val q = ArrayDeque<Pair<Point, Int>>()

        q.offer(startingPoint to 0)
        visited += startingPoint
        count++

        while (q.isNotEmpty()) {
            val (p, dist) = q.poll()
            for ((dx, dy) in moveDiffs) {
                val pNext = Point(p.x + dx, p.y + dy)
                val distNext = dist + 1
                if (pNext.x < 0 || pNext.y < 0 || pNext in visited || !maze.isFree(pNext) || distNext > maxDist) continue
                q.offer(pNext to distNext)
                visited += pNext
                count++
            }
        }

        return count
    }

    class CachingMaze(val k: Int) {
        private val cells = hashMapOf<Point, Boolean>()

        fun isFree(point: Point): Boolean = cells.getOrPut(point) { calculateIfCellIsFree(point) }

        private fun calculateIfCellIsFree(point: Point): Boolean {
            val (x, y) = point
            val n = k.toLong() + x * x + 3 * x + 2 * x * y + y + y * y
            val numBits = hammingWeight(n)
            return numBits % 2 == 0
        }

        /**
         * Calculate the [Hamming weight](https://en.wikipedia.org/wiki/Hamming_weight) of [n],
         * which is equal to the number of `1` bits in a binary number.
         *
         * The algorithm has runtime `Theta(|num of 1 bits|)` and is thus on average more efficient
         * than just checking each bit, which takes `Theta(|num of all bits|)`
         */
        private fun hammingWeight(n: Long): Int {
            var num = n
            var count = 0

            // first, count and reset the sign bit if set
            if (n and Long.MIN_VALUE != 0L) {
                count++
                num = num and Long.MAX_VALUE
            }

            while (num > 0) {
                count++
                num = num and (num - 1) // reset the lowest bit
            }

            return count
        }
    }

    companion object {
        private val moveDiffs = listOf(
            0 to 1, // up
            1 to 0, // right
            0 to -1, // down
            -1 to 0, // left
        )
    }
}

private fun readInput(inputFileName: String): Int =
    readInputLines(2016, 13, inputFileName)[0].toInt()

private fun printResult(inputFileName: String) {
    val k = readInput(inputFileName)
    val solver = Maze()
    val startingPoint = Point(1, 1)
    // part 1
    val targetPoint = Point(31, 39)
    val res1 = solver.minStepsToReach(k, startingPoint, targetPoint)
    println("Min steps to reach $targetPoint from $startingPoint: $res1")
    // part 2
    val maxDist = 50
    val res2 = solver.numberOfReachableCells(k, startingPoint, maxDist)
    println("Number of free cells reachable in at most $maxDist steps: $res2")
}

fun main() {
    printResult("input.txt")
}