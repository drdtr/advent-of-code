package advent_of_code_2018.day06

import Util.readInputLines
import advent_of_code_2018.day06.ManhattanDistanceArea.*
import kotlin.math.abs

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
 * # Part 2
 * Determine the size of the largest finite where for each cell the sum of Manhatten distances to all points
 * is less than the given limit.
 *
 */
class ManhattanDistanceArea {
    data class Point(val x: Int, val y: Int)

    // For clarity, we distinguish syntactically between the public class Point in the method signature
    // and the private class Cell denoting grid cells.
    private data class Cell(val x: Int, val y: Int)

    fun calcLargestFiniteArea(inputPoints: Collection<Point>): Int {
        val points = getDistinctNormalizedPoints(inputPoints)
        val xMax = points.maxOf { it.x }
        val yMax = points.maxOf { it.y }

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
                } else if (existingPointDist != equalPointDistMarker && existingPointDist.pointIndex != pointDist.pointIndex
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
                if (pointDist != null && pointDist != equalPointDistMarker && pointDist.pointIndex !in pointsWithInfiniteArea) {
                    areaByPoint[pointDist.pointIndex]++
                }
            }
        }
        return requireNotNull(areaByPoint.maxOrNull())
    }

    /**
     * For `p` points in a `m*n` grid, the runtime complexity of the brute-force implementation
     * is `O(p * m * n)`.
     */
    fun calcLargestAreaNearAllPointsBruteForce(maxDistSumExcl: Int, inputPoints: Collection<Point>): Int {
        val points = getDistinctNormalizedPoints(inputPoints)
        val xMax = points.maxOf { it.x }
        val yMax = points.maxOf { it.y }

        val grid = Array(yMax + 1) { y ->
            IntArray(xMax + 1) { x ->
                points.sumOf { abs(it.x - x) + abs(it.y - y) }
            }
        }

        return grid.sumOf { row -> row.count { it < maxDistSumExcl } }
    }

    /**
     * For the improved implementation, we utilize the fact that the Manhattan distance can be calculated
     * separately for `x` and `y` components: we will separately add the `x` distances for all cells
     * and `y` distances for all cells. This allows us to omit computing the distance for each point,
     * but instead only count the number of points in a row or column:
     * - For the `y` component, we will traverse the grid from top to bottom,
     * keep a running sum of the number of points above and below the current row,
     * and use them to add the `y` component of the Manhattan distance to each cell of the current row.
     * - Similarly for the `x` component, we will traverse the grid from left to right and add the `x` component
     * of the Manhattan distance to each cell of the current column.
     *
     *
     * For `p` points in a `m*n` grid, the runtime complexity is `O(p + m * n) = O(m * n)` because `p <= m * n`
     * since all points are inside the `m*n` grid.
     */
    fun calcLargestAreaNearAllPoints(maxDistSumExcl: Int, inputPoints: Collection<Point>): Int {
        val points = getDistinctNormalizedPoints(inputPoints)
        val xMax = points.maxOf { it.x }
        val yMax = points.maxOf { it.y }

        val grid = Array(yMax + 1) { IntArray(xMax + 1) }

        val pointsInRow = IntArray(yMax + 1).also { count -> for (p in points) count[p.y]++ }
        var pointsAbove = 0
        var pointsBelow = pointsInRow.sum() - pointsInRow[0]
        var yDistSumAbove = 0
        var yDistSumBelow = pointsInRow.mapIndexed { index, numOfPoints -> index * numOfPoints }.sum()
        for (y in 0..yMax) {
            for (x in 0..xMax) {
                grid[y][x] += yDistSumAbove + yDistSumBelow
            }
            yDistSumBelow -= pointsBelow
            pointsAbove += pointsInRow[y]
            pointsBelow -= if (y < yMax) pointsInRow[y + 1] else 0
            yDistSumAbove += pointsAbove
        }

        val pointsInCol = IntArray(xMax + 1).also { count -> for (p in points) count[p.x]++ }
        var pointsLeft = 0
        var pointsRight = pointsInCol.sum() - pointsInCol[0]
        var xDistSumLeft = 0
        var xDistSumRight = pointsInCol.mapIndexed { index, numOfPoints -> index * numOfPoints }.sum()
        for (x in 0..xMax) {
            for (y in 0..yMax) {
                grid[y][x] += xDistSumLeft + xDistSumRight
            }
            xDistSumRight -= pointsRight
            pointsLeft += pointsInCol[x]
            pointsRight -= if (x < xMax) pointsInCol[x + 1] else 0
            xDistSumLeft += pointsLeft
        }

        return grid.sumOf { row -> row.count { it < maxDistSumExcl } }
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

private fun readInput(inputFileName: String): Set<Point> = readInputLines(2018, 6, inputFileName).map { line ->
    line.split(",").map { it.trim().toInt() }.let { (x, y) -> Point(x, y) }
}.toSet()

private fun printResult(inputFileName: String) {
    val points = readInput(inputFileName)
    val solver = ManhattanDistanceArea()

    // part 1
    val res1 = solver.calcLargestFiniteArea(points)
    println("Largest finite area: $res1")

    // part 2
    val res2 = solver.calcLargestAreaNearAllPoints(maxDistSumExcl = 10_000, points)
    println("Largest finite area: $res2")
}

fun main() {
    printResult("input.txt")
}
