package advent_of_code_2015.day24

import advent_of_code_2015.day02.PaperAndRibbonForPresent.*
import org.junit.Assert.*
import org.junit.Assume.*
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.*
import kotlin.test.Test

@RunWith(Parameterized::class)
internal class MinimizeProductForEqualSumsTest(val expected: Long, val k: Int, val nums: List<Int>) {

    @Test
    fun `test MinimizeProductForEqualSums`() = with(MinimizeProductForEqualSums()) {
        assertEquals(expected, findMinimumProductForEqualSumsAndSmallestGroup(nums, k))
    }

    companion object {
        @Parameters(name = "{index}: expected={0}, k={1}, nums={2}")
        @JvmStatic
        fun testParams() = listOf(
            arrayOf(99, 3, listOf(1, 2, 3, 4, 5, 7, 8, 9, 10, 11)),
            arrayOf(44, 4, listOf(1, 2, 3, 4, 5, 7, 8, 9, 10, 11)),
        )
    }
}