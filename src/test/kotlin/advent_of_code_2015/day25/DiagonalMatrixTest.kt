package advent_of_code_2015.day25

import advent_of_code_2015.day02.PaperAndRibbonForPresent.*
import org.junit.Assert.*
import org.junit.Assume.*
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.*
import kotlin.test.Test

@RunWith(Parameterized::class)
internal class DiagonalMatrixTest(val expected: Int, val row: Int, val col: Int) {

    @Test
    fun `test DiagonalMatrix`() = with(DiagonalMatrix()) {
        assertEquals(expected, calcDiagonally(row, col))
    }


    companion object {
        @Parameters(name = "{index}: expected={0}, row={1}, col={2}")
        @JvmStatic
        fun testParams() = listOf(
            arrayOf(20151125, 1, 1),
            arrayOf(31916031, 2, 1),
            arrayOf(16080970, 3, 1),
            arrayOf(24592653, 4, 1),
            arrayOf(77061, 5, 1),
            arrayOf(33071741, 6, 1),
            arrayOf(18749137, 1, 2),
            arrayOf(21629792, 2, 2),
            arrayOf(8057251, 3, 2),
            arrayOf(32451966, 4, 2),
            arrayOf(17552253, 5, 2),
            arrayOf(6796745, 6, 2),
            arrayOf(17289845, 1, 3),
            arrayOf(16929656, 2, 3),
            arrayOf(1601130, 3, 3),
            arrayOf(21345942, 4, 3),
            arrayOf(28094349, 5, 3),
            arrayOf(25397450, 6, 3),
            arrayOf(30943339, 1, 4),
            arrayOf(7726640, 2, 4),
            arrayOf(7981243, 3, 4),
            arrayOf(9380097, 4, 4),
            arrayOf(6899651, 5, 4),
            arrayOf(24659492, 6, 4),
            arrayOf(10071777, 1, 5),
            arrayOf(15514188, 2, 5),
            arrayOf(11661866, 3, 5),
            arrayOf(10600672, 4, 5),
            arrayOf(9250759, 5, 5),
            arrayOf(1534922, 6, 5),
            arrayOf(33511524, 1, 6),
            arrayOf(4041754, 2, 6),
            arrayOf(16474243, 3, 6),
            arrayOf(31527494, 4, 6),
            arrayOf(31663883, 5, 6),
            arrayOf(27995004, 6, 6),
        )
    }
}
