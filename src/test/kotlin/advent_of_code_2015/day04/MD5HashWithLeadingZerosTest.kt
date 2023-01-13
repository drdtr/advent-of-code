package advent_of_code_2015.day04

import org.junit.Assert.*
import org.junit.Assume.*
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.*
import kotlin.test.Test

@RunWith(Parameterized::class)
class MD5HashWithLeadingZerosTest(val expected: Int, val s: String, val numOfZeros: Int) {

    @Test
    fun `test MD5HashWithLeadingZeros`() = with(MD5HashWithLeadingZeros()) {
        assertEquals(expected, findSmallestIntegerSuffixToProduceLeadingZeros(s, numOfZeros))
    }

    companion object {
        @Parameters(name = "{index}: expected={0}, s={1}, numOfZeros={2}")
        @JvmStatic
        fun testParams() = listOf(
            arrayOf(609043, "abcdef", 5),
            arrayOf(1048970, "pqrstuv", 5),
        )
    }
}