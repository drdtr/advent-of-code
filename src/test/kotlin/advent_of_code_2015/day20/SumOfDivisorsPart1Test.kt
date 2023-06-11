package advent_of_code_2015.day20

import advent_of_code_2015.day02.PaperAndRibbonForPresent.*
import org.junit.Assert.*
import org.junit.Assume.*
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.*
import kotlin.test.Test

@RunWith(Parameterized::class)
class SumOfDivisorsPart1Test(val expected: Int, val k: Int) {

    @Test
    fun `test SumOfDivisorsPart1`() = with(SumOfDivisorsPart1()) {
        assertEquals(expected, minNumberToReachSum(k))
    }


    companion object {
        @Parameters(name = "{index}: expected={0}, k={1}")
        @JvmStatic
        fun testParams() = listOf(
            arrayOf(1, 1),
            arrayOf(2, 2),
            arrayOf(2, 3),
            arrayOf(3, 4),
            arrayOf(4, 5),
            arrayOf(4, 6),
            arrayOf(4, 7),
            arrayOf(6, 8),
            arrayOf(6, 9),
            arrayOf(6, 10),
            arrayOf(6, 11),
            arrayOf(6, 12),
            arrayOf(8, 13),
            arrayOf(8, 14),
            arrayOf(8, 15),
            arrayOf(48, 100),
            arrayOf(360, 1000),
            arrayOf(3120, 10_000),
            arrayOf(27720, 100_000),
        )
    }
}