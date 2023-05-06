package advent_of_code_2015.day18

import org.junit.jupiter.api.Assertions.*

import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.*
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(Parameterized::class)
internal class ToggleGridInStepsTest(
    val expected: Int,
    val steps: Int,
    val gridStrings: List<String>,
    val cellsAlwaysOn: List<Pair<Int, Int>>,
) {

    @Test
    fun `test ToggleGridInSteps`() = with(ToggleGridInSteps()) {
        val grid = gridStrings
                .map { line -> line.toCharArray().map { ch -> ch == '#' }.toBooleanArray() }
                .toTypedArray()
        assertEquals(expected, toggleAndCount(grid, steps, cellsAlwaysOn))
    }

    companion object {
        @Parameters(name = "{index}: expected={0}, steps={1}, gridStrings={2}, cellsAlwaysOn={3}")
        @JvmStatic
        fun testParams() = listOf(
            arrayOf(count0, 0, gridStrings0, emptyListOfCells),
            arrayOf(count1, 1, gridStrings0, emptyListOfCells),
            arrayOf(count2, 2, gridStrings0, emptyListOfCells),
            arrayOf(count3, 3, gridStrings0, emptyListOfCells),
            arrayOf(count4, 4, gridStrings0, emptyListOfCells),

            arrayOf(count1, 0, gridStrings1, emptyListOfCells),
            arrayOf(count2, 1, gridStrings1, emptyListOfCells),
            arrayOf(count3, 2, gridStrings1, emptyListOfCells),
            arrayOf(count4, 3, gridStrings1, emptyListOfCells),

            arrayOf(count2, 0, gridStrings2, emptyListOfCells),
            arrayOf(count3, 1, gridStrings2, emptyListOfCells),
            arrayOf(count4, 2, gridStrings2, emptyListOfCells),

            arrayOf(count3, 0, gridStrings3, emptyListOfCells),
            arrayOf(count4, 1, gridStrings4, emptyListOfCells),

            arrayOf(count4, 0, gridStrings4, emptyListOfCells),

            arrayOf(countAlwaysOn0, 0, gridStringsAlwaysOn0, listOfCornerCells),
            arrayOf(countAlwaysOn1, 1, gridStringsAlwaysOn0, listOfCornerCells),
            arrayOf(countAlwaysOn2, 2, gridStringsAlwaysOn0, listOfCornerCells),
            arrayOf(countAlwaysOn3, 3, gridStringsAlwaysOn0, listOfCornerCells),
            arrayOf(countAlwaysOn4, 4, gridStringsAlwaysOn0, listOfCornerCells),
            arrayOf(countAlwaysOn5, 5, gridStringsAlwaysOn0, listOfCornerCells),

            arrayOf(countAlwaysOn1, 0, gridStringsAlwaysOn1, listOfCornerCells),
            arrayOf(countAlwaysOn2, 1, gridStringsAlwaysOn1, listOfCornerCells),
            arrayOf(countAlwaysOn3, 2, gridStringsAlwaysOn1, listOfCornerCells),
            arrayOf(countAlwaysOn4, 3, gridStringsAlwaysOn1, listOfCornerCells),
            arrayOf(countAlwaysOn5, 4, gridStringsAlwaysOn1, listOfCornerCells),

            arrayOf(countAlwaysOn2, 0, gridStringsAlwaysOn2, listOfCornerCells),
            arrayOf(countAlwaysOn3, 1, gridStringsAlwaysOn2, listOfCornerCells),
            arrayOf(countAlwaysOn4, 2, gridStringsAlwaysOn2, listOfCornerCells),
            arrayOf(countAlwaysOn5, 3, gridStringsAlwaysOn2, listOfCornerCells),

            arrayOf(countAlwaysOn3, 0, gridStringsAlwaysOn3, listOfCornerCells),
            arrayOf(countAlwaysOn4, 1, gridStringsAlwaysOn3, listOfCornerCells),
            arrayOf(countAlwaysOn5, 2, gridStringsAlwaysOn3, listOfCornerCells),

            arrayOf(countAlwaysOn4, 0, gridStringsAlwaysOn4, listOfCornerCells),
            arrayOf(countAlwaysOn5, 1, gridStringsAlwaysOn4, listOfCornerCells),

            arrayOf(countAlwaysOn5, 0, gridStringsAlwaysOn5, listOfCornerCells),
        )

        private val gridStrings0 = listOf(
            ".#.#.#",
            "...##.",
            "#....#",
            "..#...",
            "#.#..#",
            "####..",
        )
        private val gridStrings1 = listOf(
            "..##..",
            "..##.#",
            "...##.",
            "......",
            "#.....",
            "#.##..",
        )
        private val gridStrings2 = listOf(
            "..###.",
            "......",
            "..###.",
            "......",
            ".#....",
            ".#....",
        )
        private val gridStrings3 = listOf(
            "...#..",
            "......",
            "...#..",
            "..##..",
            "......",
            "......",
        )
        private val gridStrings4 = listOf(
            "......",
            "......",
            "..##..",
            "..##..",
            "......",
            "......",
        )

        private val gridStringsAlwaysOn0 = listOf(
            "##.#.#",
            "...##.",
            "#....#",
            "..#...",
            "#.#..#",
            "####.#",
        )
        private val gridStringsAlwaysOn1 = listOf(
            "#.##.#",
            "####.#",
            "...##.",
            "......",
            "#...#.",
            "#.####",
        )
        private val gridStringsAlwaysOn2 = listOf(
            "#..#.#",
            "#....#",
            ".#.##.",
            "...##.",
            ".#..##",
            "##.###",
        )
        private val gridStringsAlwaysOn3 = listOf(
            "#...##",
            "####.#",
            "..##.#",
            "......",
            "##....",
            "####.#",
        )
        private val gridStringsAlwaysOn4 = listOf(
            "#.####",
            "#....#",
            "...#..",
            ".##...",
            "#.....",
            "#.#..#",
        )
        private val gridStringsAlwaysOn5 = listOf(
            "##.###",
            ".##..#",
            ".##...",
            ".##...",
            "#.#...",
            "##...#",
        )

        private fun List<String>.countCells() = sumOf { row -> row.count { it == '#' } }

        private val count0 = gridStrings0.countCells()
        private val count1 = gridStrings1.countCells()
        private val count2 = gridStrings2.countCells()
        private val count3 = gridStrings3.countCells()
        private val count4 = gridStrings4.countCells()

        private val countAlwaysOn0 = gridStringsAlwaysOn0.countCells()
        private val countAlwaysOn1 = gridStringsAlwaysOn1.countCells()
        private val countAlwaysOn2 = gridStringsAlwaysOn2.countCells()
        private val countAlwaysOn3 = gridStringsAlwaysOn3.countCells()
        private val countAlwaysOn4 = gridStringsAlwaysOn4.countCells()
        private val countAlwaysOn5 = gridStringsAlwaysOn5.countCells()

        private val emptyListOfCells = emptyList<Pair<Int, Int>>()
        private val listOfCornerCells = with(gridStringsAlwaysOn0) {
            val m = size
            val n = this[0].length
            listOf(0 to 0, 0 to n - 1, m - 1 to 0, m - 1 to n - 1)
        }
    }
}