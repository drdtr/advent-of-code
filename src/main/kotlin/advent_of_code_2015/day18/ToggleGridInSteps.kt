package advent_of_code_2015.day18

import Util.readInputLines
import kotlin.system.measureNanoTime

/**
 * [Like a GIF For Your Yard](https://adventofcode.com/2015/day/18)
 *
 * Given a boolean grid of size `100x100`, perform a sequence of steps,
 * where in each step, all lights are turned on/off depending on the states of their surrounding lights,
 * and return the number of lights that are on
 * (see also [Conway's Game of Life](https://en.wikipedia.org/wiki/Conway's_Game_of_Life)).
 *
 * In each step, each light `(i, j)` is updated as follows:
 * - if `(i, j)` is on and has 2 or 3 neighbors that are on, then it stays on, otherwise it's turned off
 * - if `(i, j)` is off and has 3 neighbors that are on, then it's turned on, otherwise it stays off
 * - We consider all 8 neighbors. If `(i, j)` is on the edge, then the non-existent neighbors are considered off
 *
 * Part II: additionally consider a set of cells, where the light is _always_ on.
 */
class ToggleGridInSteps {
    fun toggleAndCount(
        initialGrid: Array<BooleanArray>,
        steps: Int = 0,
        cellsAlwaysOn: List<Pair<Int, Int>> = emptyList()
    ): Int = ToggleAndCounterSolver(initialGrid, cellsAlwaysOn).countCellsAfterSteps(steps)

    private class ToggleAndCounterSolver(initialGrid: Array<BooleanArray>, val cellsAlwaysOn: List<Pair<Int, Int>>) {
        private val m = initialGrid.size
        private val n = initialGrid[0].size

        private var currGrid = initialGrid.setAlwaysOnCells()

        private var nextGrid = Array(m) { BooleanArray(n) }

        fun countCellsAfterSteps(steps: Int): Int {
            repeat(steps) {
                computeNewCellValues()
                swapGrids()
            }
            return currGrid.sumOf { row -> row.count { isOn -> isOn } }
        }

        private fun computeNewCellValues() {
            for (i in 0 until m) for (j in 0 until n) {
                val countOn = currGrid.countNeighborsThatAreOnAround(i, j)
                nextGrid[i][j] = if (currGrid[i][j]) countOn in 2..3 else countOn == 3
            }
            nextGrid.setAlwaysOnCells()
        }

        private fun Array<BooleanArray>.setAlwaysOnCells() = apply {
            for ((i, j) in cellsAlwaysOn) {
                this[i][j] = true
            }
        }

        private fun swapGrids() {
            val t = currGrid
            currGrid = nextGrid
            nextGrid = t
        }

        private fun isValidCell(i: Int, j: Int) = i in 0 until m && j in 0 until n

        private fun Array<BooleanArray>.countNeighborsThatAreOnAround(i0: Int, j0: Int): Int =
            (i0 - 1..i0 + 1).sumOf { i ->
                (j0 - 1..j0 + 1).count { j -> !(i == i0 && j == j0) && isValidCell(i, j) && this[i][j] }
            }
    }
}

private val solver = ToggleGridInSteps()

private fun readInput(inputFileName: String): Array<BooleanArray> =
    readInputLines(2015, 18, inputFileName)
            .map { line -> line.toCharArray().map { ch -> ch == '#' }.toBooleanArray() }
            .toTypedArray()

private fun printResultPart1(inputFileName: String) {
    val grid = readInput(inputFileName)
    val steps = 100
    println("Number of cells: ${solver.toggleAndCount(grid, steps)}")
}

private fun printResultPart2(inputFileName: String) {
    val grid = readInput(inputFileName)
    val steps = 100
    val m = grid.size
    val n = grid[0].size
    val cellsAlwaysOn = listOf(0 to 0, 0 to n - 1, m - 1 to 0, m - 1 to n - 1)
    println("Number of cells with corner cells always on: ${solver.toggleAndCount(grid, steps, cellsAlwaysOn)}")
}

private fun benchmarkCurrentSolver(inputFileName: String) {
    val initialGrid = readInput(inputFileName)
    val steps = 100
    val reps = 10
    val durationNanos = measureNanoTime { repeat(reps) { solver.toggleAndCount(initialGrid, steps) } }
    println("Average duration: %.1f ms".format(durationNanos / 1_000_000.0 / reps))
}

fun main() {
    printResultPart1("input.txt")
    printResultPart2("input.txt")
    benchmarkCurrentSolver("input.txt")
}
