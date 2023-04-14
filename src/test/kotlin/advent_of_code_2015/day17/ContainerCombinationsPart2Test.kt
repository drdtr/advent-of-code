package advent_of_code_2015.day17

import org.junit.jupiter.api.Assertions.*

import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.*
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(Parameterized::class)
internal class ContainerCombinationsPart2Test(
    val expectedMin: Int,
    val expectedCount: Int,
    val target: Int,
    val values: List<Int>
) {

    @Test
    fun `test ContainerCombinationsPart2`() = with(ContainerCombinationsPart2()) {
        val actual = countCombinations(values.toIntArray(), target)
        assertEquals(expectedCount, actual.combinationsCount)
        assertEquals(expectedMin, actual.minUsed)
    }

    companion object {
        @Parameters(name = "{index}: expectedMin={0}, expectedCount={1}, target={2}, values={3}")
        @JvmStatic
        fun testParams() = listOf(
            arrayOf(2, 3, 25, listOf(20, 15, 10, 5, 5)),
        )
    }
}