package advent_of_code_2015.day01

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.*
import kotlin.test.Test

@RunWith(Parameterized::class)
class CalculateFloorTest(val expectedFloor: Int, val expectedIndexReachingBasement: Int, val s: String) {

    @Test
    fun `test NotQuiteLisp`() = with(CalculateFloor()) {
        val calcFloorRes = calcFloor(s)
        assertEquals(expectedFloor, calcFloorRes.lastFloor)
        assertEquals(expectedIndexReachingBasement, calcFloorRes.firstIndexReachingBasement)
    }


    companion object {
        @Parameters(name = "{index}: expected={0}, s={1}")
        @JvmStatic
        fun testParams() = listOf(
            arrayOf(0, -1, "(())"),
            arrayOf(0, -1, "()()"),
            arrayOf(3, -1, "((("),
            arrayOf(3, -1, "(()(()("),
            arrayOf(3, 0, "))((((("),
            arrayOf(-1, 2, "())"),
            arrayOf(-1, 0, "))("),
            arrayOf(-3, 0, ")))"),
            arrayOf(-3, 0, ")())())"),
        )
    }
}