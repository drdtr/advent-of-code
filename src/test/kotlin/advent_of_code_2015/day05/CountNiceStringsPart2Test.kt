package advent_of_code_2015.day05

import advent_of_code_2015.day02.PaperAndRibbonForPresent.*
import org.junit.Assert.*
import org.junit.Assume.*
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.*
import kotlin.test.Test

@RunWith(Parameterized::class)
class CountNiceStringsPart2Test(val expected: Int, val strings: List<String>) {

    @Test
    fun `test CountNiceStringsPart2`() = with(CountNiceStringsPart2()) {
        assertEquals(expected, countNiceStrings(strings))
    }

    companion object {
        @Parameters(name = "{index}: expected={0}, strings={1}")
        @JvmStatic
        fun testParams() = listOf(
            arrayOf(1, listOf("xxyxx")),
            arrayOf(1, listOf("xxyxx", "uurcxstgmygtbstg")),
            arrayOf(1, listOf("qjhvhtzxzqqjkmpb", "ieodomkazucvgmuy")),
            arrayOf(2, listOf("xxyxx", "qjhvhtzxzqqjkmpb", "uurcxstgmygtbstg", "ieodomkazucvgmuy")),
        )
    }
}