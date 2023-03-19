package advent_of_code_2015.day12

import org.junit.jupiter.api.Assertions.*

import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.*
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(Parameterized::class)
class SumOfParsedNumbersPart2Test(val expected: Int, val exclValues: Set<String>, val jsonStr: String) {

    @Test
    fun `test SumOfParsedNumbersPart2`() = with(SumOfParsedNumbersPart2()) {
        assertEquals(expected, sumOfParsedNumbers(jsonStr, exclValues))
    }

    companion object {
        private val red = setOf("red")
        private val blue = setOf("blue")
        private val redBlue = setOf("red", "blue")

        @Parameters(name = "{index}: expected={0}, jsonStr={1}")
        @JvmStatic
        fun testParams() = listOf(
            arrayOf(0, redBlue, "[]"),
            arrayOf(0, redBlue, "{}"),
            arrayOf(6, redBlue, "[1,2,3]"),
            arrayOf(10, red, """[1,2,3,"red",4]"""), // "red" is not a prop value, so no effect
            arrayOf(6, red, """{"a":2,"b":4}"""),
            arrayOf(3, red, "[[[3]]]"),
            arrayOf(123, red, "[[[123]]]"),
            arrayOf(123, red, """{"a":{"b":124},"c":-1}"""),
            arrayOf(-1, red, """{"a":{"b":124, "x":"red", },"c":-1}"""),
            arrayOf(0, red, """{"a":[-1,1]}"""),
            arrayOf(-10, red, """{"a":[-11,1]}"""),
            arrayOf(10, red, """{"a":[-1,11]}"""),
            arrayOf(0, red, """[-1,{"a":1}]"""),
            arrayOf(-10, red, """[-11,{"a":1}]"""),
            arrayOf(10, red, """[-1,{"a":11}]"""),

            arrayOf(4, red, """[1,{"c":"red","b":2},3]"""),
            arrayOf(4, redBlue, """[1,{"c":"red","b":2},3]"""),
            arrayOf(6, blue, """[1,{"c":"red","b":2},3]"""),
            arrayOf(0, red, """{"d":"red","e":[1,2,3,4],"f":5}"""),
            arrayOf(0, redBlue, """{"d":"red","e":[1,2,3,4],"f":5}"""),
            arrayOf(15, blue, """{"d":"red","e":[1,2,3,4],"f":5}"""),
        )
    }
}