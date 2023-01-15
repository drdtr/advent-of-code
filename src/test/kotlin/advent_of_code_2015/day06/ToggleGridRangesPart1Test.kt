package advent_of_code_2015.day06

import advent_of_code_2015.day06.ToggleOpType.*
import org.junit.Assert.*
import org.junit.Assume.*
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.*
import kotlin.test.Test

@RunWith(Parameterized::class)
class ToggleGridRangesPart1Test(val expected: Int, val m: Int, val n: Int, val ops: List<ToggleOp>) {

    @Test
    fun `test ToggleGridRangesPart1Naive`() = with(ToggleGridRangesPart1Naive()) {
        assertEquals(expected, toggleAndCount(ops, m, n))
    }


    companion object {
        @Parameters(name = "{index}: expected={0}, m={1}, n={2}, ops={3}")
        @JvmStatic
        fun testParams() = listOf(
            arrayOf(200, 10, 20, listOf(ToggleOp(TurnOn, 0, 0, 9, 19))),
            arrayOf(200, 10, 20, listOf(ToggleOp(Toggle, 0, 0, 9, 19))),
            arrayOf(10, 10, 20, listOf(ToggleOp(TurnOn, 0, 0, 9, 0))),
            arrayOf(0, 10, 20, listOf(ToggleOp(TurnOff, 0, 0, 9, 0))),
            arrayOf(4, 10, 20, listOf(ToggleOp(TurnOn, 5, 5, 6, 6))),
            arrayOf(
                175, 10, 20, listOf(
                    ToggleOp(TurnOn, 0, 0, 9, 19),
                    ToggleOp(TurnOff, 1, 11, 5, 15),
                )
            ),
            arrayOf(
                165, 10, 20, listOf(
                    ToggleOp(TurnOn, 0, 0, 9, 19),
                    ToggleOp(TurnOff, 1, 11, 5, 15),
                    ToggleOp(Toggle, 2, 0, 2, 19),
                )
            ),
        )
    }
}