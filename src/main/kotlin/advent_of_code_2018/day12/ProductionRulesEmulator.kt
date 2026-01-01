package advent_of_code_2018.day12

import Util.readInputLines
import advent_of_code_2018.day12.ProductionRulesEmulator.*
import advent_of_code_2018.day12.ProductionRulesEmulator.Companion.CELL_EMPTY
import advent_of_code_2018.day12.ProductionRulesEmulator.Companion.CELL_FILLED
import java.util.regex.Pattern

/**
 * [Subterranean Sustainability](https://adventofcode.com/2018/day/12)
 *
 * You're given a boolean array and a set of production rules of the form `abcde => true/false`.
 * - The initial array starts at 0, each production cycle can also produce cells at negative position.
 * - In each production cycle, each cell is checked whether it matches a rule of the form `abcde => true`.
 *    - If there is such rule, the cell is set to `true`.
 *    - Otherwise, the cell is set to `false`.
 * - For cells that are not filled yet, `false` is assumed, e.g. for cell -1 at the beginning
 * when the initial starts with values at 0.
 *
 * ### Part 1
 * Simulate `n = 20` production cycles and count the number of cells with value `true` in each cycle,
 * i.e., if a cell was `true` in `k` cycles, then it adds `k` to the sum.
 *
 * ### Part 2
 * Same as part 1, but for `n = 50_000_000_000` (50 bln) production cycles.
 *
 * Example:
 * - initial state: `#..#.#..##......###...###`
 * - production rules (for brevity, only those producing a filled cell)
 * ```
 * ...## => #
 * ..#.. => #
 * .#... => #
 * .#.#. => #
 * .#.## => #
 * .##.. => #
 * .#### => #
 * #.#.# => #
 * #.### => #
 * ##.#. => #
 * ##.## => #
 * ###.. => #
 * ###.# => #
 * ####. => #
 * ```
 * - 20 cycles:
 * ```
 *                  1         2         3
 *        0         0         0         0
 *  0: ...#..#.#..##......###...###...........
 *  1: ...#...#....#.....#..#..#..#...........
 *  2: ...##..##...##....#..#..#..##..........
 *  3: ..#.#...#..#.#....#..#..#...#..........
 *  4: ...#.#..#...#.#...#..#..##..##.........
 *  5: ....#...##...#.#..#..#...#...#.........
 *  6: ....##.#.#....#...#..##..##..##........
 *  7: ...#..###.#...##..#...#...#...#........
 *  8: ...#....##.#.#.#..##..##..##..##.......
 *  9: ...##..#..#####....#...#...#...#.......
 * 10: ..#.#..#...#.##....##..##..##..##......
 * 11: ...#...##...#.#...#.#...#...#...#......
 * 12: ...##.#.#....#.#...#.#..##..##..##.....
 * 13: ..#..###.#....#.#...#....#...#...#.....
 * 14: ..#....##.#....#.#..##...##..##..##....
 * 15: ..##..#..#.#....#....#..#.#...#...#....
 * 16: .#.#..#...#.#...##...#...#.#..##..##...
 * 17: ..#...##...#.#.#.#...##...#....#...#...
 * 18: ..##.#.#....#####.#.#.#...##...##..##..
 * 19: .#..###.#..#.#.#######.#.#.#..#.#...#..
 * 20: .#....##....#####...#######....#.#..##.
 * ```
 *
 */
class ProductionRulesEmulator {
    data class ProductionRule(val lhs: String, val rhs: Char) {
        override fun toString() = "$lhs => $rhs"
    }

    fun sumOfFilledCellPositions(
        initialState: String,
        productionRules: List<ProductionRule>,
        numOfCycles: Int = 1,
    ): Int = with(EmulatorBruteForce(initialState, productionRules)) {
        repeat(numOfCycles) {
            emulate()
        }
        println("Num of cells: " + currentState.cells.length)
        currentState.cells.withIndex().sumOf { (i, ch) -> if (ch == CELL_FILLED) i + currentState.offset else 0 }
    }


    internal class EmulatorBruteForce(initialState: String, val productionRules: List<ProductionRule>) {
        private var cells = initialState.toCharArray()

        // Offset from 0 in the initial state to 0 in the current cells,
        // where cells[i - offset] corresponds to initialState[i].
        private var offset = 0

        data class EmulationState(val cells: String, val offset: Int)

        val currentState: EmulationState
            get() = EmulationState(String(cells), offset)

        fun emulate() {
            // We extend the new cells by 2 beyond left and right margin
            // so a rule could be applied and fill a cell to the left or to the right of the current cells.
            //   cells:        0 1 2 3
            //   newCells: . . . . . . . .
            // This also means that the new cells are shifted by 2 relative to the current cells
            val newCellsShift = 2
            val newCells = CharArray(cells.size + newCellsShift * 2)
            for (i in newCells.indices) {
                val matchingRule = findMatchingRule(i - newCellsShift)
                newCells[i] = matchingRule?.rhs ?: CELL_EMPTY
            }
            // Trim empty cells on the left and right.
            val fromIndex = newCells.indexOf(CELL_FILLED).coerceAtLeast(0)
            val toIndexIncl = newCells.lastIndexOf(CELL_FILLED).coerceAtLeast(1)
            cells = newCells.copyOfRange(fromIndex, toIndexIncl + 1)
            offset += (fromIndex - newCellsShift)
        }

        private fun cellAt(i: Int) = if (i in cells.indices) cells[i] else CELL_EMPTY

        private fun findMatchingRule(cellIndex: Int): ProductionRule? {
            // the left-most char of a rule's LHS must match the cell that's 2 steps to the left
            val startOffset = -2
            fun ProductionRule.matches(): Boolean {
                lhs.forEachIndexed { i, ch -> if (cellAt(cellIndex + startOffset + i) != ch) return false }
                return true
            }
            return productionRules.find { it.matches() }
        }
    }

    companion object {
        const val CELL_FILLED = '#'
        const val CELL_EMPTY = '.'
    }
}

private val rulePattern = Pattern.compile("([.#]{5}) => ([.#])")

private fun parseRule(s: String): ProductionRule {
    with(rulePattern.matcher(s)) {
        require(matches()) { "Cannot parse $s" }
        val lhs = group(1).trim()
        require(lhs.length == 5) {
            "LHS of a rule must have length 5 but was $lhs with length ${lhs.length}."
        }
        require(lhs.all { it == CELL_FILLED || it == CELL_EMPTY }) {
            "LHS of a rule must contain only characters '$CELL_FILLED' and '$CELL_EMPTY' but was \"$lhs\""
        }
        val rhs = group(2).trim()
        require(rhs == "$CELL_FILLED" || rhs == "$CELL_EMPTY") {
            "RHS of a rule must be \"$CELL_FILLED\" or \"$CELL_EMPTY\" but was \"$rhs\""
        }
        return ProductionRule(lhs = lhs, rhs = rhs[0])
    }
}

private data class InitialStateAndProductionRules(
    val initialState: String,
    val productionRules: List<ProductionRule>,
)

private fun readInput(inputFileName: String): InitialStateAndProductionRules {
    val inputLines = readInputLines(2018, 12, inputFileName)
    val initialState = inputLines[0].split(' ').last()
    val productionRules = inputLines.subList(2, inputLines.size).map { parseRule(it) }
    return InitialStateAndProductionRules(initialState, productionRules)
}

private fun printResult(inputFileName: String) {
    val input = readInput(inputFileName)
//    println("Initial state:")
//    println("  " + input.initialState)
//    println("Rules:")
//    println("  " + input.productionRules.joinToString("\n  "))

    val solver = ProductionRulesEmulator()

    // part 1
    var numOfCycles = 20
    val res1 = solver.sumOfFilledCellPositions(input.initialState, input.productionRules, numOfCycles)
    println("Sum of pot numbers with plants after $numOfCycles cycles: $res1")

    // part 2
//    numOfCycles = 200_000
//    val durationNanos = measureNanoTime {
//        val res2 = solver.sumOfFilledCellPositions(input.initialState, input.productionRules, numOfCycles)
//        println("Sum of pot numbers with plants after $numOfCycles cycles: $res2")
//    }
//    println("Duration: %d ms".format(durationNanos / 1_000_000))

}

fun main() {
    printResult("input.txt")
}


