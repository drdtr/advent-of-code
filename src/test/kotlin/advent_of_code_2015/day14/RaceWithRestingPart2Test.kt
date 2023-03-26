package advent_of_code_2015.day14

import advent_of_code_2015.day14.RaceWithRestingPart2.*
import org.junit.jupiter.api.Assertions.*

import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.*
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(Parameterized::class)
class RaceWithRestingPart2Test(val expected: Int, val racingTime: Int, val reindeers: List<Reindeer>) {

    @Test
    fun `test RaceWithRestingPart2`() = with(RaceWithRestingPart2()) {
        assertEquals(expected, maxScore(racingTime, reindeers))
    }

    companion object {
        @Parameters(name = "{index}: expected={0}, racingTime={1}, reindeers={2}")
        @JvmStatic
        fun testParams(): List<Array<Any>> {
            val reindeersWithSpeed10And15 = listOf(
                Reindeer("A", speed = 10, tFly = 20, tRest = 100),
                Reindeer("B", speed = 15, tFly = 10, tRest = 20),
            )
            return listOf(
                arrayOf(5, 5, reindeersWithSpeed10And15),
                arrayOf(10, 10, reindeersWithSpeed10And15),
                arrayOf(15, 15, reindeersWithSpeed10And15),
                arrayOf(15, 16, reindeersWithSpeed10And15),
                arrayOf(15, 19, reindeersWithSpeed10And15),
                arrayOf(15, 20, reindeersWithSpeed10And15),
                arrayOf(15, 21, reindeersWithSpeed10And15),
                arrayOf(16, 30, reindeersWithSpeed10And15),
                arrayOf(19, 33, reindeersWithSpeed10And15),
                arrayOf(19, 34, reindeersWithSpeed10And15),
                arrayOf(72, 90, reindeersWithSpeed10And15),
                arrayOf(82, 100, reindeersWithSpeed10And15),
                arrayOf(102, 120, reindeersWithSpeed10And15),
                arrayOf(282, 300, reindeersWithSpeed10And15),
                arrayOf(
                    139, 139, listOf(
                        Reindeer("Comet", speed = 14, tFly = 10, tRest = 127),
                        Reindeer("Dancer", speed = 16, tFly = 11, tRest = 162),
                    )
                ),
                arrayOf(
                    139, 140, listOf(
                        Reindeer("Comet", speed = 14, tFly = 10, tRest = 127),
                        Reindeer("Dancer", speed = 16, tFly = 11, tRest = 162),
                    )
                ),
                arrayOf(
                    689, 1000, listOf(
                        Reindeer("Comet", speed = 14, tFly = 10, tRest = 127),
                        Reindeer("Dancer", speed = 16, tFly = 11, tRest = 162),
                    )
                ),
            )
        }
    }
}