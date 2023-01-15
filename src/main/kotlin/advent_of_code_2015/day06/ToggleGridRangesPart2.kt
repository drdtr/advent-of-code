package advent_of_code_2015.day06

import Util.readInputLines
import java.util.regex.Pattern
import kotlin.math.max
import kotlin.system.measureNanoTime

/**
 * [Probably a Fire Hazard - Part 2](https://adventofcode.com/2015/day/6)
 *
 * Given a boolean grid of size `1000x1000`, perform a sequence of cell value change operations
 * and return the total sum of all cells. Each operation can be:
 * - `+d for i1,j1 through i2,j2` - increase the value of all cells in the rectangle `(i1, j1, i2, j2)` by `d`
 * - `+d for i1,j1 through i2,j2` - decrease the value of all cells in the rectangle `(i1, j1, i2, j2)` by `d`,
 * however not below 0 (i.e. if the value becomes negative, then set to 0)
 */
class ToggleGridRangesPart2Naive {
    fun toggleAndCount(ops: Iterable<BrightnessOp>, m: Int = 1000, n: Int = 1000): Int {
        val grid = Array(m) { IntArray(n) }
        ops.forEach { it.applyTo(grid) }
        return grid.sumOf { row -> row.sumOf { it } }
    }

    private fun BrightnessOp.applyTo(grid: Array<IntArray>) {
        for (i in i1..i2) for (j in j1..j2) {
            grid[i][j] = max(grid[i][j] + brightnessChange, 0)
        }
    }
}

private val solver = ToggleGridRangesPart2Naive()

data class BrightnessOp(val brightnessChange: Int, val i1: Int, val j1: Int, val i2: Int, val j2: Int)

private val toggleOpInputMatcher = Pattern.compile("([a-z ]+) (\\d+),(\\d+) \\w+ (\\d+),(\\d+)")

/**
 * The problem statements says that the names of operations in the text input remain the same as in
 * [Part 1](https://adventofcode.com/2015/day/6#part1), but the interpretation is changed:
 * - `turn off`: decrease by 1
 * - `turn on`: increase by 1
 * - `toggle`: increase by 2
 */
private fun parseBrightnessOp(s: String): BrightnessOp {
    with(toggleOpInputMatcher.matcher(s)) {
        matches()
        val brightnessChange = when (val opTypeStr = group(1)) {
            "turn off" -> -1
            "turn on"  -> 1
            "toggle"   -> 2
            else       -> error("Invalid operation type $opTypeStr")
        }
        return BrightnessOp(brightnessChange, group(2).toInt(), group(3).toInt(), group(4).toInt(), group(5).toInt())
    }
}

private fun readInput(inputFileName: String): List<BrightnessOp> =
    readInputLines(2015, 6, inputFileName).map { parseBrightnessOp(it) }

private fun printResult(inputFileName: String) {
    val ops = readInput(inputFileName)
    println("Total sum of cells: ${solver.toggleAndCount(ops)}")
}

private fun benchmarkCurrentSolver(inputFileName: String) {
    val ops = readInput(inputFileName)
    val reps = 100
    val durationNanos = measureNanoTime { repeat(reps) { solver.toggleAndCount(ops) } }
    println("Average duration: %.1f ms".format(durationNanos / 1_000_000.0 / reps))
}

fun main() {
    printResult("input.txt")
    benchmarkCurrentSolver("input.txt")
}
