package advent_of_code_2015.day02

import advent_of_code_2015.day02.PaperAndRibbonForPresent.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.*
import kotlin.test.Test

@RunWith(Parameterized::class)
class PaperAndRibbonForPresentTest(val expectedPaper: Int, val expectedRibbon: Int, val boxes: List<Box>) {

    @Test
    fun `test IWasToldThereWouldBeNoMath`() = with(PaperAndRibbonForPresent()) {
        assertEquals(expectedPaper, calcPaperArea(boxes))
        assertEquals(expectedRibbon, calcRibbonLength(boxes))
    }


    companion object {
        @Parameters(name = "{index}: expectedPaper={0}, expectedRibbon={1}, boxes={2}")
        @JvmStatic
        fun testParams() = listOf(
            arrayOf(7, 5, listOf(Box(1, 1, 1))),
            arrayOf(43, 14, listOf(Box(1, 1, 10))),
            arrayOf(58, 34, listOf(Box(2, 3, 4))),
            arrayOf(
                108, 53, listOf(
                    Box(1, 1, 1),
                    Box(1, 1, 10),
                    Box(2, 3, 4),
                )
            ),
        )
    }
}