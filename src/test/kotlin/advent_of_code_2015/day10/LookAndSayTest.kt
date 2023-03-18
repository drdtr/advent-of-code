package advent_of_code_2015.day10

import org.junit.jupiter.api.Assertions.*

import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.*
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(Parameterized::class)
class LookAndSayTest(val n0: Int, val numReps: Int, val expected: String) {

    @Test
    fun `test LookAndSay`() = with(LookAndSay()) {
        assertEquals(expected, lookAndSay(n0, numReps))
    }

    companion object {
        @Parameters(name = "{index}: n0={0}, numReps={1}, expected={2}")
        @JvmStatic
        fun testParams() = listOf(
            arrayOf(1, 0, "1"),
            arrayOf(1, 1, "11"),
            arrayOf(1, 2, "21"),
            arrayOf(1, 3, "1211"),
            arrayOf(1, 4, "111221"),
            arrayOf(1, 5, "312211"),
            arrayOf(21, 0, "21"),
            arrayOf(21, 1, "1211"),
            arrayOf(21, 2, "111221"),
            arrayOf(21, 3, "312211"),
        )
    }
}