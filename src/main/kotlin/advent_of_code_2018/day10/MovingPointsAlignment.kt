package advent_of_code_2018.day10

import Util.readInputLines
import advent_of_code_2018.day10.MovingPointsAlignment.*
import java.util.regex.Pattern

/**
 * [The Stars Align](https://adventofcode.com/2018/day/10)
 *
 * You're given a list of points on a 2D grid, where each point has a starting positions and a velocity.
 * The points move one step per second. At some moment, they form a legible message.
 * Find this moment.
 *
 * ### Solution idea:
 * - Emulate the point movements and compute the area of the grid.
 * - When the grid is small enough, print it and look at the output to check whether the points form letters
 * (I don't think that we were expected to build an actual OCR 😅 and automatically determine the message).
 * - Implementation:
 *    - Run the point movement emulation in a loop
 *    - Print the grid when it's small enough
 *    - Ask the user whether to continue, check the user's response from the console input.
 *
 * ### Example:
 *
 * |Position <x, y>  |  Velocity <dx, dy>   |
 * |:-------- | :----------- |
 * |< 9,  1>  | < 0,  2>     |
 * |< 7,  0>  | <-1,  0>     |
 * |< 3, -2>  | <-1,  1>     |
 * |< 6, 10>  | <-2, -1>     |
 * |< 2, -4>  | < 2,  2>     |
 * |<-6, 10>  | < 2, -2>     |
 * |< 1,  8>  | < 1, -1>     |
 * |< 1,  7>  | < 1,  0>     |
 * |<-3, 11>  | < 1, -2>     |
 * |< 7,  6>  | <-1, -1>     |
 * |<-2,  3>  | < 1,  0>     |
 * |<-4,  3>  | < 2,  0>     |
 * |<10, -3>  | <-1,  1>     |
 * |< 5, 11>  | < 1, -2>     |
 * |< 4,  7>  | < 0, -1>     |
 * |< 8, -2>  | < 0,  1>     |
 * |<15,  0>  | <-2,  0>     |
 * |< 1,  6>  | < 1,  0>     |
 * |< 8,  9>  | < 0, -1>     |
 * |< 3,  3>  | <-1,  1>     |
 * |< 0,  5>  | < 0, -1>     |
 * |<-2,  2>  | < 2,  0>     |
 * |< 5, -2>  | < 1,  2>     |
 * |< 1,  4>  | < 2,  1>     |
 * |<-2,  7>  | < 2, -2>     |
 * |< 3,  6>  | <-1, -1>     |
 * |< 5,  0>  | < 1,  0>     |
 * |<-6,  0>  | < 2,  0>     |
 * |< 5,  9>  | < 1, -2>     |
 * |<14,  7>  | <-2,  0>     |
 * |<-3,  6>  | < 2, -1>     |
 *
 * After 3 seconds, the points align forming the word `HI`:
 *
 * - Initially:
 * ```
 * ........#.............
 * ................#.....
 * .........#.#..#.......
 * ......................
 * #..........#.#.......#
 * ...............#......
 * ....#.................
 * ..#.#....#............
 * .......#..............
 * ......#...............
 * ...#...#.#...#........
 * ....#..#..#.........#.
 * .......#..............
 * ...........#..#.......
 * #...........#.........
 * ...#.......#..........
 * ```
 *
 * - After 1 second:
 * ```
 * ......................
 * ......................
 * ..........#....#......
 * ........#.....#.......
 * ..#.........#......#..
 * ......................
 * ......#...............
 * ....##.........#......
 * ......#.#.............
 * .....##.##..#.........
 * ........#.#...........
 * ........#...#.....#...
 * ..#...........#.......
 * ....#.....#.#.........
 * ......................
 * ......................
 * ```
 *
 * - After 2 seconds:
 * ```
 * ......................
 * ......................
 * ......................
 * ..............#.......
 * ....#..#...####..#....
 * ......................
 * ........#....#........
 * ......#.#.............
 * .......#...#..........
 * .......#..#..#.#......
 * ....#....#.#..........
 * .....#...#...##.#.....
 * ........#.............
 * ......................
 * ......................
 * ......................
 * ```
 *
 * - After 3 seconds:
 * ```
 * ......................
 * ......................
 * ......................
 * ......................
 * ......#...#..###......
 * ......#...#...#.......
 * ......#...#...#.......
 * ......#####...#.......
 * ......#...#...#.......
 * ......#...#...#.......
 * ......#...#...#.......
 * ......#...#..###......
 * ......................
 * ......................
 * ......................
 * ......................
 * ```
 *
 */
class MovingPointsAlignment {
    data class Point(val x: Int, val y: Int)
    data class Velocity(val dx: Int, val dy: Int)

    private data class MovingPoint(var x: Int, var y: Int, val dx: Int, val dy: Int)

    fun emulatePointMovements(pointsWithVelocities: List<Pair<Point, Velocity>>) {
        val movingPoints = pointsWithVelocities.map { (p, v) ->
            MovingPoint(x = p.x, y = p.y, dx = v.dx, dy = v.dy)
        }
        val emulator = PointMovementEmulator(movingPoints)
        fun printGridStateAndCheckContinue(step: Int): Boolean {
            val gridSize = emulator.calcNormalizedGridSize()
            val gridArea = gridSize.first.toLong() * gridSize.second
            println("Step $step")
            println("Grid size: ${gridSize.first} * ${gridSize.second} = $gridArea")
            if (gridArea <= 10_000) {
                println(gridToString(emulator.toNormalizedGrid()))
                println("The grid was small. Continue (Y=Yes/AnythingElse=No)?...")
                return (readln() == "Y")
            } else {
                println("Grid too large to print")
                return true
            }
        }
        var step = 0
        var shouldContinue = printGridStateAndCheckContinue(step)
        while(shouldContinue) {
            emulator.movePoints()
            step++
            shouldContinue = printGridStateAndCheckContinue(step)
        }

    }

    private class PointMovementEmulator(val movingPoints: List<MovingPoint>) {

        fun movePoints() {
            for (p in movingPoints) with(p) {
                x += dx
                y += dy
            }
        }

        fun calcNormalizedGridSize(): Pair<Int, Int> = with(calcMinMaxCoordinates()) {
            maxX - minX to maxY - minY
        }

        /**
         * Normalizes the point coordinates to `(0, 0)`, i.e. the minimum coordinates are 0.
         */
        fun toNormalizedGrid(): Array<BooleanArray> {
            val (minX, maxX, minY, maxY) = calcMinMaxCoordinates()
            val normalizedMaxX = maxX - minX
            val normalizedMaxY = maxY - minY
            val grid = Array(normalizedMaxY + 1) { BooleanArray(normalizedMaxX + 1) }
            for (p in movingPoints) {
                grid[p.y - minY][p.x - minX] = true
            }
            return grid
        }

        private data class MinMaxCoordinates(
            val minX: Int,
            val maxX: Int,
            val minY: Int,
            val maxY: Int,
        )

        private fun calcMinMaxCoordinates(): MinMaxCoordinates {
            val p0 = movingPoints.first()
            var minX = p0.x
            var maxX = p0.x
            var minY = p0.y
            var maxY = p0.y
            for (i in 1 until movingPoints.size) with(movingPoints[i]) {
                if (minX > x) minX = x
                if (maxX < x) maxX = x
                if (minY > y) minY = y
                if (maxY < y) maxY = y
            }
            return MinMaxCoordinates(
                minX = minX,
                maxX = maxX,
                minY = minY,
                maxY = maxY,
            )
        }
    }
}

private fun gridToString(grid: Array<BooleanArray>): String {
    val gridCellSet = '#'
    val gridCellFree = '.'
    return grid.joinToString("\n") { row ->
        buildString {
            for (i in 0 until row.size) {
                append(if (row[i]) gridCellSet else gridCellFree)
            }
        }
    }
}

private val pointPattern =
    Pattern.compile("position=<\\s*(-?\\d+),\\s*(-?\\d+)> velocity=<\\s*(-?\\d+),\\s*(-?\\d+)>")

private fun parsePointWithVelocity(s: String): Pair<Point, Velocity> {
    with(pointPattern.matcher(s)) {
        require(matches()) { "Cannot parse $s" }
        return Point(
            x = group(1).toInt(),
            y = group(2).toInt(),
        ) to Velocity(
            dx = group(3).toInt(),
            dy = group(4).toInt(),
        )
    }
}

private fun readInput(inputFileName: String): List<Pair<Point, Velocity>> =
    readInputLines(2018, 10, inputFileName).map { parsePointWithVelocity(it) }

private fun printResult(inputFileName: String) {
    val pointsWithVelocities = readInput(inputFileName)
    println(pointsWithVelocities.joinToString("\n"))
    val solver = MovingPointsAlignment()
    solver.emulatePointMovements(pointsWithVelocities)
}

fun main() {
//    printResult("inputExample.txt")
    printResult("input.txt")
}


