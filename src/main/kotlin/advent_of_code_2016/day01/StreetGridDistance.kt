package advent_of_code_2016.day01

import Util.readInputLines
import advent_of_code_2016.day01.StreetGridDistance.*
import kotlin.math.abs

/**
 * [No Time for a Taxicab](https://adventofcode.com/2016/day/1)
 *
 * You're given instructions of the form `Lx` and `Ry` meaning to turn left or right and go `x` or `y` units, resp.
 * You start at `(0, 0)` facing North. For example:
 * - `R2, L3` leads 2 blocks East and 3 blocks North, i.e. 5 blocks away
 * - `R2, R2, R2` leads you 2 blocks South, i.e. 2 blocks away.
 * - `R5, L5, R5, R3` leads you 12 blocks away
 *
 * ### Part 1
 * Find the shortest distance to the destination.
 *
 * ### Part 2
 * Find the shortest distance to the first point visited twice.
 *
 */
class StreetGridDistance {
    sealed class Step(val d: Int)
    class Left(d: Int) : Step(d)
    class Right(d: Int) : Step(d)

    fun shortestDistanceToDestination(steps: List<Step>): Int = with(GridWalker()) {
        for (step in steps) {
            when (step) {
                is Left  -> turnLeft()
                is Right -> turnRight()
            }
            move(step.d)
        }

        return abs(x) + abs(y)
    }

    fun shortestDistanceToFirstPointVisitedTwice(steps: List<Step>): Int = with(GridWalker()) {
        val visited = hashSetOf<Pair<Int, Int>>()
        for (step in steps) {
            when (step) {
                is Left  -> turnLeft()
                is Right -> turnRight()
            }
            repeat(step.d) {
                move(1)
                val p = x to y
                if (p in visited) return abs(x) + abs(y)
                visited.add(p)
            }
        }
        error("No point has been visited twice")
    }

    private class GridWalker {
        var x: Int = 0
            private set
        var y: Int = 0
            private set

        private var directionIndex = 0
        private val moveDiffs = listOf(
            0 to 1, // up (North)
            1 to 0, // right (East)
            0 to -1, // down (South)
            -1 to 0, // left (West)
        )

        fun move(steps: Int) {
            val (dx, dy) = moveDiffs[directionIndex]
            x += steps * dx
            y += steps * dy
        }

        fun turnLeft() {
            directionIndex--
            if (directionIndex < 0) directionIndex = moveDiffs.size - 1
        }

        fun turnRight() {
            directionIndex++
            if (directionIndex == moveDiffs.size) directionIndex = 0
        }
    }
}

private fun readInput(inputFileName: String): List<Step> =
    readInputLines(2016, 1, inputFileName)[0].split(",")
            .map { it.trim() }
            .map {
                when (it[0]) {
                    'L'  -> Left(it.substring(1).toInt())
                    'R'  -> Right(it.substring(1).toInt())
                    else -> error("Invalid step: $it")
                }
            }

private fun printResult(inputFileName: String) {
    val steps = readInput(inputFileName)
    val solver = StreetGridDistance()
    println("Shortest distance to destination: ${solver.shortestDistanceToDestination(steps)}")
    println("Shortest distance to first point visited twice: ${solver.shortestDistanceToFirstPointVisitedTwice(steps)}")
}

fun main() {
    printResult("input.txt")
}