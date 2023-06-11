package advent_of_code_2015.day20

import advent_of_code_2015.day02.PaperAndRibbonForPresent.*
import org.junit.Assert.*
import org.junit.Assume.*
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.*
import kotlin.test.Test

@RunWith(Parameterized::class)
class SumOfDivisorsPart2Test(val expected: Int, val k: Int, val m: Int) {

    @Test
    fun `test SumOfDivisorsPart2`() = with(SumOfDivisorsPart2()) {
        assertEquals(expected, minNumberToReachSum(k, m))
    }


    companion object {
        @Parameters(name = "{index}: expected={0}, k={1}, m={2}")
        @JvmStatic
        fun testParams() = listOf(
            arrayOf(1, 1, 3),
            arrayOf(2, 2, 3),
            arrayOf(2, 3, 3),
            arrayOf(3, 4, 3),
            arrayOf(4, 5, 3),
            arrayOf(4, 6, 3),
            arrayOf(6, 7, 3),
            arrayOf(6, 8, 3),
            arrayOf(6, 9, 3),
            arrayOf(6, 10, 3),
            arrayOf(6, 11, 3),
            arrayOf(8, 12, 3),
            arrayOf(6, 12, 6),
            arrayOf(8, 13, 6),
            arrayOf(8, 14, 6),
            arrayOf(10, 15, 6),
            arrayOf(8, 15, 8),
            arrayOf(48, 100, 10),
            arrayOf(360, 1000, 10),
            arrayOf(3240, 10_000, 50),
            arrayOf(27720, 100_000, 50),
        )
    }
}