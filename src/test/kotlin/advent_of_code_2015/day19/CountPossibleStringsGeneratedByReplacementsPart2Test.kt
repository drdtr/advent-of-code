package advent_of_code_2015.day19

import org.junit.jupiter.api.Assertions.*

import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.*
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(Parameterized::class)
class CountPossibleStringsGeneratedByReplacementsPart2Test(
    val expected: Int,
    val s: String,
    val replacements: Map<String, List<String>>
) {

    @Test
    fun `test CountPossibleStringsGeneratedByReplacementsPart2`() =
        with(CountPossibleStringsGeneratedByReplacementsPart2()) {
            assertEquals(expected, countStepsToGenerateByReplacements(s, replacements))
        }

    companion object {
        @Parameters(name = "{index}: expected={0}, s={1}, replacements={2}")
        @JvmStatic
        fun testParams() = listOf(
            arrayOf(
                3, "HOH",
                mapOf(
                    "e" to listOf("H", "O"),
                    "H" to listOf("HO", "OH"),
                    "O" to listOf("HH"),
                ),
            ),
            arrayOf(
                6, "HOHOHO",
                mapOf(
                    "e" to listOf("H", "O"),
                    "H" to listOf("HO", "OH"),
                    "O" to listOf("HH"),
                ),
            ),
            arrayOf(
                6, "AABCACECDE",
                mapOf(
                    "e" to listOf("A", "BC"),
                    "A" to listOf("CC", "CD"),
                    "B" to listOf("AA", "CE"),
                    "C" to listOf("ABCDE"),
                    "CC" to listOf("ABC"),
                ),
            ),
        )
    }
}