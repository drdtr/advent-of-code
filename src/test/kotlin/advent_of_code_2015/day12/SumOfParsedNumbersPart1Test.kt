package advent_of_code_2015.day12

import org.junit.jupiter.api.Assertions.*

import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.*
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(Parameterized::class)
class SumOfParsedNumbersPart1Test(val expected: Int, val jsonStr: String) {

    @Test
    fun `test SumOfParsedNumbersPart1`() = with(SumOfParsedNumbersPart1()) {
        assertEquals(expected, sumOfParsedNumbers(jsonStr))
    }

    companion object {
        @Parameters(name = "{index}: expected={0}, jsonStr={1}")
        @JvmStatic
        fun testParams() = listOf(
            arrayOf(0, "[]"),
            arrayOf(0, "{}"),
            arrayOf(6, "[1,2,3]"),
            arrayOf(6, """{"a":2,"b":4}"""),
            arrayOf(3, "[[[3]]]"),
            arrayOf(123, "[[[123]]]"),
            arrayOf(123, """{"a":{"b":124},"c":-1}"""),
            arrayOf(0, """{"a":[-1,1]}"""),
            arrayOf(-10, """{"a":[-11,1]}"""),
            arrayOf(10, """{"a":[-1,11]}"""),
            arrayOf(0, """[-1,{"a":1}]"""),
            arrayOf(-10, """[-11,{"a":1}]"""),
            arrayOf(10, """[-1,{"a":11}]"""),
        )
    }
}