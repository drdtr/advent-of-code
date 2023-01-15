package advent_of_code_2015.day06

import org.junit.Assert.*
import org.junit.Assume.*
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.*
import kotlin.test.Test

@RunWith(Parameterized::class)
class ToggleGridRangesPart2Test(val expected: Int, val m: Int, val n: Int, val ops: List<BrightnessOp>) {

    @Test
    fun `test ToggleGridRangesPart2Naive`() = with(ToggleGridRangesPart2Naive()) {
        assertEquals(expected, toggleAndCount(ops, m, n))
    }


    companion object {
        @Parameters(name = "{index}: expected={0}, m={1}, n={2}, ops={3}")
        @JvmStatic
        fun testParams() = listOf(
            arrayOf(400, 10, 20, listOf(BrightnessOp(2, 0, 0, 9, 19))),
            arrayOf(600, 10, 20, listOf(BrightnessOp(2, 0, 0, 9, 19), BrightnessOp(1, 0, 0, 9, 19))),
            arrayOf(10, 10, 20, listOf(BrightnessOp(1, 0, 0, 9, 0))),
            arrayOf(0, 10, 20, listOf(BrightnessOp(-1, 0, 0, 9, 0))),
            arrayOf(15, 10, 20, listOf(BrightnessOp(2, 0, 0, 9, 0), BrightnessOp(-1, 0, 0, 4, 0))),
            arrayOf(4, 10, 20, listOf(BrightnessOp(1, 5, 5, 6, 6))),
            arrayOf(175, 10, 20, listOf(
                BrightnessOp(1, 0, 0, 9, 19),
                BrightnessOp(-1, 1, 11, 5, 15),
            )),
            arrayOf(215, 10, 20, listOf(
                BrightnessOp(1, 0, 0, 9, 19),
                BrightnessOp(-1, 1, 11, 5, 15),
                BrightnessOp(2, 2, 0, 2, 19),
            )),
        )
    }
}