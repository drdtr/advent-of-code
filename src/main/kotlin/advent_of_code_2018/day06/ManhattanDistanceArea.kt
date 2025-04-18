package advent_of_code_2018.day06

import Util.readInputLines
import advent_of_code_2018.day06.ManhattanDistanceArea.*

/**
 * [Chronal Coordinates](https://adventofcode.com/2018/day/6)
 *
 * You're given a list of 2-D coordinates on an infinite plane.
 * Around each point `A`, we determine the size of the area
 * where each cell `(x, y)`  is closer to `A` than to any other point `B`.
 * If `(x, y)` has equal distance from two points `A` and `B` then it doesn't count for any of the areas.
 * We discard points where the areas extend infinitely and consider
 * only those points whose area is finite because it's delimited by the areas of other points.
 *
 * # Part 1
 * Determine the size of the largest finite area around a point.
 *
 */
class ManhattanDistanceArea {
    data class Point(val x: Int, val y: Int)

    fun calcLargestFiniteArea(inputPoints: Collection<Point>): Int {
        val points = getDistinctNormalizedPoints(inputPoints)
        println(points.mapIndexed { index, point -> "$index: $point" }.joinToString(separator = "\n"))
        val xMax = points.maxOf { it.x }
        val yMax = points.maxOf { it.y }

        // For clarity, we distinguish syntactically between the public class Point in the method signature
        // and the local class Cell denoting grid cells.
        data class Cell(val x: Int, val y: Int)
        data class PointDist(val pointIndex: Int, val dist: Int)

        val equalPointDistMarker = PointDist(-1, -1)
        val grid = Array(yMax + 1) { Array<PointDist?>(xMax + 1) { null } }
        val pointsWithInfiniteArea = hashSetOf<Int>()

        // Perform a multi-source BFS.
        val q = java.util.ArrayDeque<Pair<Cell, PointDist>>()
        points.forEachIndexed { index, point -> q.offer(Cell(point.x, point.y) to PointDist(index, 0)) }

        fun isInGrid(x: Int, y: Int) = x in 0..xMax && y in 0..yMax

        while (q.isNotEmpty()) {
            val (cell, pointDist) = q.poll()
            for ((dx, dy) in moveDiffs) {
                val x = cell.x + dx
                val y = cell.y + dy
                if (!isInGrid(x, y)) {
                    pointsWithInfiniteArea += pointDist.pointIndex
                    continue
                }
                val pointDistNext = PointDist(pointDist.pointIndex, dist = pointDist.dist + 1)
                val existingPointDist = grid[y][x]
                if (existingPointDist == null) {
                    grid[y][x] = pointDistNext
                    q.offer(Cell(x, y) to pointDistNext)
                } else if (existingPointDist != equalPointDistMarker
                    && existingPointDist.pointIndex != pointDist.pointIndex
                    // Since it's a multi-source BFS, the already stored distance can only be smaller or equal.
                    // This is why we only need to distinguish the two cases:
                    // - if it's smaller, then we keep it because it's closer to the other point
                    // - if it's equal, then we replace it with the "equal point distance" marker.
                    && existingPointDist.dist == pointDistNext.dist
                ) {
                    grid[y][x] = equalPointDistMarker
                }
            }
        }

        val areaByPoint = IntArray(points.size)
        grid.forEach { row ->
            row.forEach { pointDist ->
                if (pointDist != null
                    && pointDist != equalPointDistMarker
                    && pointDist.pointIndex !in pointsWithInfiniteArea
                ) {
                    areaByPoint[pointDist.pointIndex]++
                }
            }
        }
        return requireNotNull(areaByPoint.maxOrNull())
    }

    /**
     * Returns a list of points where each point is unique and all points are normalized such that
     * the x-coordinate of the left-most point is 0 and the y-coordinate of the bottom point is 0.
     */
    private fun getDistinctNormalizedPoints(points: Collection<Point>): List<Point> {
        val xMin = points.minOf { it.x }
        val yMin = points.minOf { it.y }
        return points.distinct().map { Point(it.x - xMin, it.y - yMin) }
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

private fun readInput(inputFileName: String): Set<Point> =
    readInputLines(2018, 6, inputFileName)
            .map { line -> line.split(",").map { it.trim().toInt() }.let { (x, y) -> Point(x, y) } }
            .toSet()

private fun printResult(inputFileName: String) {
    val points = readInput(inputFileName)
    val solver = ManhattanDistanceArea()

    // part 1
    val res1 = solver.calcLargestFiniteArea(points)
    println("Largest finite area: $res1")
}

fun main() {
    printResult("input.txt")
}
