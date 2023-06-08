package advent_of_code_2015.day19

import org.junit.jupiter.api.Assertions.*

import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.*
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(Parameterized::class)
class CountPossibleStringsGeneratedByReplacementsPart1Test(
    val expected: Int,
    val s: String,
    val replacements: Map<String, List<String>>
) {

    @Test
    fun `test CountPossibleStringsGeneratedByReplacementsPart1`() =
        with(CountPossibleStringsGeneratedByReplacementsPart1()) {
            assertEquals(expected, countGeneratedByReplacement(s, replacements))
        }

    companion object {
        @Parameters(name = "{index}: expected={0}, s={1}, replacements={2}")
        @JvmStatic
        fun testParams() = listOf(
            arrayOf(
                4, "HOH",
                mapOf(
                    "H" to listOf("HO", "OH"),
                    "O" to listOf("HH"),
                ),
            ),
            arrayOf(
                7, "HOHOHO",
                mapOf(
                    "H" to listOf("HO", "OH"),
                    "O" to listOf("HH"),
                ),
            ),
        )
    }
}