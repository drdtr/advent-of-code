package advent_of_code_2015.day13

import advent_of_code_2015.day13.OptimalSeating.*
import org.junit.jupiter.api.Assertions.*

import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.*
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(Parameterized::class)
class OptimalSeatingTest(val expected: Int, val happinessChanges: List<HappinessChange>) {

    @Test
    fun `test OptimalSeating`() = with(OptimalSeating()) {
        assertEquals(expected, maximumHappiness(happinessChanges))
    }

    companion object {
        @Parameters(name = "{index}: expected={0}, happinessChanges={1}")
        @JvmStatic
        fun testParams() = listOf(
            arrayOf(
                10, listOf(
                    HappinessChange("A", "B", 2),
                    HappinessChange("B", "A", 8),
                )
            ),
            arrayOf(
                26, listOf(
                    HappinessChange("A", "B", 2),
                    HappinessChange("A", "C", -1),
                    HappinessChange("B", "A", 8),
                    HappinessChange("B", "C", 20),
                    HappinessChange("C", "A", -1),
                    HappinessChange("C", "B", -2),
                )
            ),
            arrayOf(
                330, listOf(
                    HappinessChange("A", "B", 54),
                    HappinessChange("A", "C", -79),
                    HappinessChange("A", "D", -2),
                    HappinessChange("B", "A", 83),
                    HappinessChange("B", "C", -7),
                    HappinessChange("B", "D", -63),
                    HappinessChange("C", "A", -62),
                    HappinessChange("C", "B", 60),
                    HappinessChange("C", "D", 55),
                    HappinessChange("D", "A", 46),
                    HappinessChange("D", "B", -7),
                    HappinessChange("D", "C", 41),
                )
            ),
        )
    }
}
