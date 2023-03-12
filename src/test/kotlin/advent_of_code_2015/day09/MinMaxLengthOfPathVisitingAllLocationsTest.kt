package advent_of_code_2015.day09

import advent_of_code_2015.day02.PaperAndRibbonForPresent.*
import advent_of_code_2015.day09.MinMaxLengthOfPathVisitingAllLocations.*
import org.junit.Assert.*
import org.junit.Assume.*
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.*
import kotlin.test.Test

@RunWith(Parameterized::class)
class MinMaxLengthOfPathVisitingAllLocationsTest(
    val expectedMinLen: Int?,
    val expectedMaxLen: Int?,
    val edges: List<WeightedEdge>
) {

    @Test
    fun `test MinMaxLengthOfPathVisitingAllLocations`() {
        val (minLen, maxLen) = MinMaxLengthOfPathVisitingAllLocations().minMaxPathLen(edges)
        expectedMinLen?.let { assertEquals(it, minLen) }
        expectedMaxLen?.let { assertEquals(it, maxLen) }
    }


    companion object {
        private fun edgeOf(src: String, dest: String, weight: Int) = WeightedEdge(src, dest, weight)

        @Parameters(name = "{index}: expectedMinLen={0}, expectedMaxLen={1}, edges={2}")
        @JvmStatic
        fun testParams() = listOf(
            arrayOf(
                605, 982, listOf(
                    edgeOf("London", "Dublin", 464),
                    edgeOf("London", "Belfast", 518),
                    edgeOf("Dublin", "Belfast", 141),
                )
            ),
        )
    }
}