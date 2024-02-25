package advent_of_code_2017.day11

import Util.readInputLines
import advent_of_code_2017.day11.HexGridDistance.*
import advent_of_code_2017.day11.HexGridDistance.HexGridStep.*
import kotlin.math.abs

/**
 * [Hex Ed](https://adventofcode.com/2017/day/11)
 *
 * You're given a list of steps in a [hexagonal grid](https://en.wikipedia.org/wiki/Hexagonal_tiling).
 * Possible steps are: `north`, `south`, `north east`, `north west`, `south east`, `south west`:
 * ```
 *   \ n  /
 * nw +--+ ne
 *   /    \
 * -+      +-
 *   \    /
 * sw +--+ se
 *   / s  \
 * ```
 *
 * ### Part 1
 *
 * What is the fewest number of steps necessary to get to the end point, reached after performing all steps in the list?
 *
 * ### Part 2
 *
 * How many steps away is the furthest point ever reached while performing the steps from the list?
 */
class HexGridDistance {
    enum class HexGridStep(val dx: Int, val dy: Int) {
        Up(dx = 0, dy = 2), // north
        Down(dx = 0, dy = -2), // south
        UpLeft(dx = -1, dy = 1), // north west
        UpRight(dx = 1, dy = 1), // north east
        DownLeft(dx = -1, dy = -1), // south west
        DownRight(dx = 1, dy = -1), // south east
    }

    interface IPoint {
        val x: Int
        val y: Int
    }

    data class Point(override val x: Int, override val y: Int) : IPoint {
        init {
            require((x + y) % 2 == 0) { "The sum of point coordinates must be even but was $x + $y = ${x + y}" }
        }
    }

    private data class MutablePoint(override var x: Int, override var y: Int) : IPoint

    private operator fun MutablePoint.plusAssign(step: HexGridStep) {
        with(step) {
            x += dx
            y += dy
        }
    }

    private fun IPoint.toPoint() = Point(x, y)
    private fun IPoint.toMutablePoint() = MutablePoint(x, y)

    fun calcStepDistance(steps: Iterable<HexGridStep>): Int {
        val startingPoint = Point(0, 0)
        val endPoint = calcPoint(startingPoint, steps)
        return calcStepDistance(startingPoint, endPoint)
    }

    fun calcMaxStepDistanceReached(steps: Iterable<HexGridStep>): Int {
        val startingPoint = Point(0, 0)
        val currPoint = startingPoint.toMutablePoint()
        var maxDist = 0
        for (step in steps) {
            currPoint += step
            val dist = calcStepDistance(startingPoint, currPoint)
            if (maxDist < dist) maxDist = dist
        }
        return maxDist
    }

    private fun calcStepDistance(startingPoint: IPoint, endPoint: IPoint): Int {
        return (abs(endPoint.x - startingPoint.x) + abs(endPoint.y - startingPoint.y)) / 2
    }

    private fun calcPoint(startingPoint: IPoint, steps: Iterable<HexGridStep>): Point {
        val p = startingPoint.toMutablePoint()
        steps.forEach { p += it }
        return p.toPoint()
    }
}

private fun readInput(inputFileName: String): List<HexGridStep> =
    readInputLines(2017, 11, inputFileName)[0].split(",").map {
        when (it) {
            "n"  -> Up
            "s"  -> Down
            "nw" -> UpLeft
            "ne" -> UpRight
            "sw" -> DownLeft
            "se" -> DownRight
            else -> error("Invalid step direction $it")
        }
    }


private fun printResult(inputFileName: String) {
    val steps = readInput(inputFileName)
    val solver = HexGridDistance()

    // part 1
    val res1 = solver.calcStepDistance(steps)
    println("Minimum step distance: $res1")

    // part 2
    val res2 = solver.calcMaxStepDistanceReached(steps)
    println("Maximum step distance reached: $res2")
}

fun main() {
    printResult("input.txt")
}
