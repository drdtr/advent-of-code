package advent_of_code_2015.day03

import org.junit.Assert.*
import org.junit.Assume.*
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.*
import kotlin.test.Test

@RunWith(Parameterized::class)
class CountVisitedCellsTest(val expected: Int, val numOfActors: Int, val path: String) {

    @Test
    fun `test PerfectlySphericalHousesInVacuum`() = with(CountVisitedCells()) {
        assumeTrue("Only applicable for 1 actor", numOfActors == 1)
        assertEquals(expected, countVisitedCells(path))
    }

    @Test
    fun `test PerfectlySphericalHousesInVacuum, interleaving actors`() = with(CountVisitedCells()) {
        assertEquals(expected, countVisitedCellsInterleavingActors(path, numOfActors))
    }


    companion object {
        @Parameters(name = "{index}: expected={0}, numOfActors={1}, path={2}")
        @JvmStatic
        fun testParams() = listOf(
            arrayOf(1, 1, ""),
            arrayOf(2, 1, ">"),
            arrayOf(2, 1, "^"),
            arrayOf(2, 1, "<>"),
            arrayOf(2, 1, "^v"),
            arrayOf(4, 1, "^>v<"),
            arrayOf(2, 1, "^v^v^v^v^v"),
            arrayOf(4, 1, "^>v<^>v<>^<v"),

            arrayOf(3, 2, "^v"),
            arrayOf(3, 2, "^>v<"),
            arrayOf(11, 2, "^v^v^v^v^v"),
        )
    }
}