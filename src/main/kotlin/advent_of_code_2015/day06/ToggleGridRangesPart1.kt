package advent_of_code_2015.day06

import Util.readInputLines
import advent_of_code_2015.day06.ToggleOpType.*
import java.util.regex.Pattern
import kotlin.system.measureNanoTime

/**
 * [Probably a Fire Hazard - Part 1](https://adventofcode.com/2015/day/6)
 *
 * Given a boolean grid of size `1000x1000`, perform a sequence of toggling operations and return the number
 * cells that contain `true`. Each operation can be:
 * - `turn off i1,j1 through i2,j2` - set all cells in the rectangle `(i1, j1, i2, j2)` to `false`
 * - `turn on i1,j1 through i2,j2` - set all cells in the rectangle `(i1, j1, i2, j2)` to `true`
 * - `toggle i1,j1 through i2,j2` - toggle all cells in the rectangle `(i1, j1, i2, j2)`
 */
class ToggleGridRangesPart1Naive {
    fun toggleAndCount(ops: Iterable<ToggleOp>, m: Int = 1000, n: Int = 1000): Int {
        val grid = Array(m) { BooleanArray(n) }
        ops.forEach { it.applyTo(grid) }
        return grid.sumOf { row -> row.count { it } }
    }

    private fun ToggleOp.applyTo(grid: Array<BooleanArray>) {
        when (opType) {
            Toggle  -> for (i in i1..i2) for (j in j1..j2) grid[i][j] = !grid[i][j]
            TurnOff -> for (i in i1..i2) for (j in j1..j2) grid[i][j] = false
            TurnOn  -> for (i in i1..i2) for (j in j1..j2) grid[i][j] = true
        }
    }
}

private val solver = ToggleGridRangesPart1Naive()

enum class ToggleOpType {
    Toggle,
    TurnOff,
    TurnOn,
}

data class ToggleOp(val opType: ToggleOpType, val i1: Int, val j1: Int, val i2: Int, val j2: Int)

private val toggleOpInputMatcher = Pattern.compile("([a-z ]+) (\\d+),(\\d+) \\w+ (\\d+),(\\d+)")

private fun parseToggleOp(s: String): ToggleOp {
    with(toggleOpInputMatcher.matcher(s)) {
        matches()
        val opType = when (val opTypeStr = group(1)) {
            "turn off" -> TurnOff
            "turn on"  -> TurnOn
            "toggle"   -> Toggle
            else       -> error("Invalid operation type $opTypeStr")
        }
        return ToggleOp(opType, group(2).toInt(), group(3).toInt(), group(4).toInt(), group(5).toInt())
    }
}

private fun readInput(inputFileName: String): List<ToggleOp> =
    readInputLines(2015, 6, inputFileName).map { parseToggleOp(it) }

private fun printResult(inputFileName: String) {
    val ops = readInput(inputFileName)
    println("Number of cells: ${solver.toggleAndCount(ops)}")
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
