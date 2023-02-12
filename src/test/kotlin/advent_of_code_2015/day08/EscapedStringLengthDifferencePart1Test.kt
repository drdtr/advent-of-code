package advent_of_code_2015.day08

import advent_of_code_2015.day02.PaperAndRibbonForPresent.*
import org.junit.Assert.*
import org.junit.Assume.*
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.*
import kotlin.test.Test

@RunWith(Parameterized::class)
class EscapedStringLengthDifferencePart1Test(val expected: Int, val strings: List<String>) {

    @Test
    fun `test EscapedStringLengthDifferencePart1`() = with(EscapedStringLengthDifferencePart1()) {
        assertEquals(expected, calcEscapedLenDiff(strings))
    }


    companion object {
        @Parameters(name = "{index}: expected={0}, strings={1}")
        @JvmStatic
        fun testParams() = listOf(
            arrayOf(2, listOf("\"\"")),
            arrayOf(2, listOf("\"abc\"")),
            arrayOf(3, listOf("\"ab\\\"c\"")),
            arrayOf(3, listOf("\"aaa\\\"aaa\"")),
            arrayOf(5, listOf("\"\\x41\"")),
        )
    }
}