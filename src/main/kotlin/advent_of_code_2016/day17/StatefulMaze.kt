package advent_of_code_2016.day17

import Util.md5
import Util.readInputLines
import java.util.ArrayDeque

/**
 * [https://adventofcode.com/2016/day/17](Two Steps Forward)
 *
 *  You're given a 4x4 grid of small rooms protected by doors
 *  - `#` denotes a wall
 *  - `-` and `|` denote doors.
 *  - the grid looks as follows
 * ```
 * #########
 * #S| | | #
 * #-#-#-#-#
 * # | | | #
 * #-#-#-#-#
 * # | | | #
 * #-#-#-#-#
 * # | | |T#
 * #########
 * ```

 *
 * You start in the top left corner `(0, 0)` and need to reach the bottom right corner (3, 3).
 * The doors states depend on the `passCode` (your puzzle input) and the sequence of uppercase characters
 * `U, D, L, R`, representing the path so far. You can find out which doors are open as follows:
 * - calculate the [MD5](https://en.wikipedia.org/wiki/MD5) hash of `passCode + path`
 * and take the first 4 characters of the result
 * - for the doors `up`, `down`, `left`, `right` from the current position,
 * the door is open iff the respective character in the hash is in the range `b..f`, and closed otherwise.
 *
 *
 * ### Part 1
 *
 * Find the shortest path from starting points `S` to target point `T`.
 *
 * Example:
 * - For `passcode = hijkl`, in the first step where the path is empty, the MD5 has of `passcode + path`
 * are `ced9` indicating that the door up is open (`c`), down is open (`e`), left is open (`d`),
 * and right is closed (`9`). Because in the top left corner, there are only doors right and down,
 * the only choice is down.
 * - If you move down and back up, then you're again at the starting point `(0, 0)` but different doors are open
 * because the first hash chars of `hijklDU` are `528e` so the right door is open (`9`) while all others are closed.
 * - For `passcode = ihgpwlah`, the shortest path is `DDRRRD`
 * - For `passcode = kglvqrro`, the shortest path is `DDUDRLRRUDRD`
 * - For `passcode = ulqzkmiv`, the shortest path is `DRURDRUDDLLDLUURRDULRLDUUDDDRR`
 *
 *
 * ### Part 2
 *
 * Find the length of a longest path from starting points `S` to target point `T`.
 *
 * Example:
 * - For `passcode = ihgpwlah`, the longest path has 370 steps.
 * - For `passcode = kglvqrro`, the longest path has 492 steps.
 * - For `passcode = ulqzkmiv`, the longest path has 830 steps.
 *
 */
class StatefulMaze {
    data class Point(val x: Int, val y: Int)
    private data class DirectionAndDiff(val dir: Char, val dx: Int, val dy: Int)
    private data class PointAndPath(val point: Point, val path: String)

    fun shortestPath(
        passcode: String,
        width: Int = 4,
        height: Int = 4,
        startingPoint: Point = Point(0, 0),
        targetPoint: Point = Point(3, 3),
    ): String {
        val q = ArrayDeque<PointAndPath>()

        q.offer(PointAndPath(startingPoint, ""))

        while (q.isNotEmpty()) {
            val (p, path) = q.poll()
            val openDoors = (passcode + path).md5().toOpenDoors()
            for ((dir, dx, dy) in openDoors) {
                val pnpNext = PointAndPath(Point(p.x + dx, p.y + dy), path + dir)
                // no need to check if the state has already been visited: since it consists of the coordinates
                // and the path to it, we'll never encounter the same state because whenever we visit the same point
                // again the path must have been different.
                //
                if (pnpNext.point.x !in 0 until width || pnpNext.point.y !in 0 until height) continue
                if (pnpNext.point == targetPoint) return pnpNext.path
                q.offer(pnpNext)
            }
        }

        error("Couldn't find a path from starting point $startingPoint to the target point $targetPoint")
    }

    fun maxPathLength(
        passcode: String,
        width: Int = 4,
        height: Int = 4,
        startingPoint: Point = Point(0, 0),
        targetPoint: Point = Point(3, 3),
    ): Any {
        var maxPathLen = 0
        val q = ArrayDeque<PointAndPath>()

        q.offer(PointAndPath(startingPoint, ""))

        while (q.isNotEmpty()) {
            val (p, path) = q.poll()
            val openDoors = (passcode + path).md5().toOpenDoors()
            for ((dir, dx, dy) in openDoors) {
                val pnpNext = PointAndPath(Point(p.x + dx, p.y + dy), path + dir)
                // no need to check if the state has already been visited: since it consists of the coordinates
                // and the path to it, we'll never encounter the same state because whenever we visit the same point
                // again the path must have been different.
                //
                if (pnpNext.point.x !in 0 until width || pnpNext.point.y !in 0 until height) continue
                if (pnpNext.point == targetPoint) {
                    val pathLen = pnpNext.path.length
                    if (maxPathLen < pathLen) maxPathLen = pathLen
                } else {
                    q.offer(pnpNext)
                }
            }
        }

        return maxPathLen
    }

    companion object {
        private val directionsAndDiffs = listOf(
            DirectionAndDiff('U', 0, -1),
            DirectionAndDiff('D', 0, 1),
            DirectionAndDiff('L', -1, 0),
            DirectionAndDiff('R', 1, 0),
        )

        private fun Char.indicatesOpenDoor() = this in 'b'..'f'

        private fun String.toOpenDoors(): List<DirectionAndDiff> = buildList {
            substring(0..3).map { it.indicatesOpenDoor() }.forEachIndexed { index, isOpen ->
                if (isOpen) add(directionsAndDiffs[index])
            }
        }
    }
}


private fun readInput(inputFileName: String): String =
    readInputLines(2016, 17, inputFileName)[0]

private fun printResult(inputFileName: String) {
    val passcode = readInput(inputFileName)
    val solver = StatefulMaze()
    // part 1
    val res1 = solver.shortestPath(passcode)
    println("The shortest path for passcode $passcode: $res1")
    // part 2
    val res2 = solver.maxPathLength(passcode)
    println("The maximum path length for passcode $passcode: $res2")
}

fun main() {
    printResult("input.txt")
}