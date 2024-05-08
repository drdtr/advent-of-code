package advent_of_code_2017.day19

import Util.readInputLines
import advent_of_code_2017.day19.RouteWithLetters.Direction.*

/**
 * [A Series of Tubes](https://adventofcode.com/2017/day/19)
 *
 * You're give a line diagram of a route, where each line element denotes how a packet can move across it:
 * * `|`: vertically
 * * `-`: horizontally
 * * `+`: turn left or right
 * * <letter>: keep the same direction
 *
 * Starting from the only element in the first input row, traverse the complete route,
 * and return all encountered letters, in the order they were encountered.
 */
class RouteWithLetters {
    private enum class Direction(var dx: Int, var dy: Int) {
        UP(dx = 0, dy = -1),
        DOWN(dx = 0, dy = 1),
        LEFT(dx = -1, dy = 0),
        RIGHT(dx = 1, dy = 0)
    }

    private data class RoutePoint(val x: Int, val y: Int, val direction: Direction)

    data class RouteTraversalResult(val length: Int, val letters: String)

    fun traverseRouteAndCollectLetters(route: List<String>): RouteTraversalResult {
        var currPoint: RoutePoint? = RoutePoint(
            x = route[0].indexOf(VERTICAL)
                    .also { require(it >= 0) { "Couldn't find starting point '$VERTICAL' in top row." } },
            y = 0,
            direction = DOWN
        )

        val encounteredLetters = StringBuilder()
        var routeLen = -1 // starting point doesn't count
        while (currPoint != null) {
            routeLen++
            val currCell = route[currPoint.y][currPoint.x]
            if (currCell in LETTERS) encounteredLetters.append(currCell)
            currPoint = nextPointFrom(currPoint, route)
        }
        return RouteTraversalResult(routeLen, encounteredLetters.toString())
    }

    /**
     * Returns the next point on the route, or `null` if there's no next point,
     * i.e., the traversal of the route has been finished.
     */
    private fun nextPointFrom(currentRoutePoint: RoutePoint, route: List<String>): RoutePoint? {
        val (x, y, dir) = currentRoutePoint
        val currCell = route[y][x]
        return when (currCell) {
            VERTICAL,
            HORIZONTAL,
            in LETTERS -> RoutePoint(x + dir.dx, y + dir.dy, dir) // just keep going in the same direction

            CROSSING   -> when (dir) {
                UP, DOWN    -> // turn left or right
                    if (x > 0 && route[y][x - 1] != EMPTY) // non-empty cell to the left
                        RoutePoint(x - 1, y, LEFT)
                    else
                        RoutePoint(x + 1, y, RIGHT)

                LEFT, RIGHT -> // turn up or down
                    if (y > 0 && route[y - 1][x] != EMPTY) // non-empty cell upwards
                        RoutePoint(x, y - 1, UP)
                    else
                        RoutePoint(x, y + 1, DOWN)
            }

            EMPTY      -> return null
            else       -> error("Unexpected route cell $currCell")
        }
    }


    companion object {
        private const val VERTICAL = '|'
        private const val HORIZONTAL = '-'
        private const val CROSSING = '+'
        private const val EMPTY = ' '
        private val LETTERS = ('A'..'Z').toSet() + ('a'..'z').toSet()
    }
}

private fun readInput(inputFileName: String): List<String> =
    readInputLines(2017, 19, inputFileName)

private fun printResult(inputFileName: String) {
    val lineRoute = readInput(inputFileName)
    val solver = RouteWithLetters()

    val res = solver.traverseRouteAndCollectLetters(lineRoute)
    // part 1
    println("Letters collected along the route: ${res.letters}")
    // part 2
    println("Route length: ${res.length}")
}

fun main() {
    printResult("input.txt")
}
