package advent_of_code_2018.day03

import Util.readInputLines
import advent_of_code_2018.day03.RectangleIntersection.*
import java.util.regex.Pattern
import kotlin.math.max
import kotlin.math.min

/**
 * [No Matter How You Slice It](https://adventofcode.com/2018/day/3)
 *
 * You're given a list of rectangles on a 2D-grid.
 *
 * ### Part 1
 *
 * Calculate how many grid cells that are covered by more than one rectangle.
 *
 * ### Part 2
 *
 * Find the id's of rectangles that don't intersect with any other rectangles.
 */
class RectangleIntersection {
    data class Rectangle(val x: Int, val y: Int, val width: Int, val height: Int, val id: Int = 0) {
        val xRight = x + width - 1
        val yBottom = y + height - 1
    }

    fun calcIntersectionArea(rectangles: Collection<Rectangle>): Int {
        // A set representing each point with one Long
        val intersectingCells = hashSetOf<Long>()
        val rectSortedByX = rectangles.sortedBy { it.x }
        for (i in rectSortedByX.indices) {
            val r1 = rectSortedByX[i]
            var j = i + 1
            while (j < rectSortedByX.size && rectSortedByX[j].x <= r1.xRight) {
                val (xIntersection, yIntersection) = calcIntersection(r1, rectSortedByX[j])
                for (x in xIntersection) {
                    for (y in yIntersection) {
                        intersectingCells += longFrom(x, y)
                    }
                }
                j++
            }
        }
        return intersectingCells.size
    }

    fun findNonIntersectingRectangles(rectangles: Collection<Rectangle>): List<Rectangle> {
        val intersectingRectangles = hashSetOf<Rectangle>()
        val rectSortedByX = rectangles.sortedBy { it.x }
        for (i in rectSortedByX.indices) {
            val r1 = rectSortedByX[i]
            var j = i + 1
            while (j < rectSortedByX.size && rectSortedByX[j].x <= r1.xRight) {
                if (r1.intersects(rectSortedByX[j])) {
                    intersectingRectangles += r1
                    intersectingRectangles += rectSortedByX[j]
                }
                j++
            }
        }
        return rectangles.filter { it !in intersectingRectangles }
    }


    private data class XAndYRange(val xRange: IntRange, val yRange: IntRange)

    private fun calcIntersection(r1: Rectangle, r2: Rectangle): XAndYRange {
        val xLeftIntersect = max(r1.x, r2.x)
        val xRightIntersect = min(r1.xRight, r2.xRight)
        val yTopIntersect = max(r1.y, r2.y)
        val yBottomIntersect = min(r1.yBottom, r2.yBottom)
        return XAndYRange(xLeftIntersect..xRightIntersect, yTopIntersect..yBottomIntersect)
    }

    private fun Rectangle.intersects(other: Rectangle): Boolean =
        max(x, other.x) <= min(xRight, other.xRight) && max(y, other.y) <= min(yBottom, other.yBottom)

    private fun longFrom(i1: Int, i2: Int) = i1.toLong() shl 32 or i2.toLong()
}

private val rectanglePattern = Pattern.compile("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)")

private fun parseRectangle(s: String): Rectangle {
    with(rectanglePattern.matcher(s)) {
        require(matches()) { "Cannot parse $s" }
        return Rectangle(
            id = group(1).toInt(),
            x = group(2).toInt(),
            y = group(3).toInt(),
            width = group(4).toInt(),
            height = group(5).toInt(),
        )
    }
}

private fun readInput(inputFileName: String): List<Rectangle> =
    readInputLines(2018, 3, inputFileName).map { parseRectangle(it) }

private fun printResult(inputFileName: String) {
    val rectangles = readInput(inputFileName)
    val solver = RectangleIntersection()

    // part 1
    val res1 = solver.calcIntersectionArea(rectangles)
    println("Intersection area: $res1")

    // part 2
    val res2 = solver.findNonIntersectingRectangles(rectangles)
    println("Non-intersecting rectangles: $res2")
}

fun main() {
    printResult("input.txt")
}

