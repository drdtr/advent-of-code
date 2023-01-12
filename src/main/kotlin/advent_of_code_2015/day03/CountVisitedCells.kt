package advent_of_code_2015.day03

import Util.readInputLines

/**
 * [Perfectly Spherical Houses in a Vacuum](https://adventofcode.com/2015/day/3)
 *
 * On a 2-D grid, following steps are possible:
 * - `^`: up
 * - `v`: down
 * - `<`: left
 * - `>`: right
 *
 * Given a string consisting only of these four characters, returned the number of visited cells
 * (some might have been visited multiple times).
 */
class CountVisitedCells {
    fun countVisitedCells(path: String) = countVisitedCellsInterleavingActors(path, 1)

    fun countVisitedCellsInterleavingActors(path: String, numOfActors: Int): Int {
        val visited = hashSetOf<Point>()
        visited.add(Point(0, 0))

        val actors = Array(numOfActors) { MovingActor() }
        var actorIndex = 0
        for (direction in path) {
            actors[actorIndex].apply {
                step(direction)
                visited.add(position)
            }
            actorIndex = (actorIndex + 1) % numOfActors
        }

        return visited.size
    }

    private data class Point(val x: Int, val y: Int)

    private data class MovingActor(private var x: Int = 0, private var y: Int = 0) {
        val position get() = Point(x, y)

        fun step(direction: Char) = when (direction) {
            UP    -> y++
            DOWN  -> y--
            LEFT  -> x--
            RIGHT -> x++
            else  -> error("Unexpected direction $direction")
        }
    }

    companion object {
        const val UP = '^'
        const val DOWN = 'v'
        const val LEFT = '<'
        const val RIGHT = '>'
    }
}

private fun printResult(inputFileName: String) {
    val s = readInputLines(2015, 3, inputFileName).first()
    val solver = CountVisitedCells()
    println("Visited houses: ${solver.countVisitedCells(s)}")
    println("Visited houses with 2 actors: ${solver.countVisitedCellsInterleavingActors(s, 2)}")
}

fun main() {
    printResult("input.txt")
}