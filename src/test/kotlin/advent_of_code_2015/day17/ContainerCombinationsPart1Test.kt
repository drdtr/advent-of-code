package advent_of_code_2015.day17

import org.junit.jupiter.api.Assertions.*

import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.*
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(Parameterized::class)
internal class ContainerCombinationsPart1Test(val expected: Int, val target: Int, val values: List<Int>) {

    @Test
    fun `test ContainerCombinationsPart1`() = with(ContainerCombinationsPart1()) {
        assertEquals(expected, countCombinations(values.toIntArray(), target))
    }

    companion object {
        @Parameters(name = "{index}: expected={0}, target={1}, values={2}")
        @JvmStatic
        fun testParams() = listOf(
            arrayOf(4, 25, listOf(20, 15, 10, 5, 5)),
        )
    }
}