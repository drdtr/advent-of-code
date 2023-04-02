package advent_of_code_2015.day15

import org.junit.jupiter.api.Assertions.*

import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.*
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(Parameterized::class)
class MaximizeProductOfWeightedSumsTest(
    val expected: Long,
    val cSum: Int,
    val mat: List<List<Int>>,
    val expectedWithFixedSum: Long,
    val fixedSum: Long,
    val fixedSumParams: List<Int>
) {

    @Test
    fun `test MaximizeProductOfWeightedSums`() = with(MaximizeProductOfWeightedSums()) {
        assertEquals(expected, findMax(mat.map { it.toIntArray() }.toTypedArray(), cSum))
    }


    @Test
    fun `test MaximizeProductOfWeightedSums with fixed sum`() = with(MaximizeProductOfWeightedSums()) {
        val res =
            findMaxWithFixedSum(mat.map { it.toIntArray() }.toTypedArray(), cSum, fixedSumParams.toIntArray(), fixedSum)
        assertEquals(expectedWithFixedSum, res)
    }


    companion object {
        @Parameters(
            name = "{index}: " +
                    "expected1=={0}, cSum={1}, mat={2}, " +
                    "expectedWithFixedSum={3}, fixedSum={4}, fixedSumParams={5}"
        )
        @JvmStatic
        fun testParams() = listOf(
            arrayOf(
                62842880, 100,
                listOf(
                    listOf(-1, -2, 6, 3),
                    listOf(2, 3, -2, -1),
                ),
                57600000, 500, listOf(8, 3)
            )
        )
    }
}