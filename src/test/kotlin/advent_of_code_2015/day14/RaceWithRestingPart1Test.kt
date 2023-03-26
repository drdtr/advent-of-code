package advent_of_code_2015.day14

import org.junit.jupiter.api.Assertions.*

import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.*
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(Parameterized::class)
class RaceWithRestingPart1Test(val expected: Int, val racingTime: Int, val reindeers: List<Reindeer>) {

    @Test
    fun `test RaceWithRestingPart1`() = with(RaceWithRestingPart1()) {
        assertEquals(expected, maxTravelledDistance(racingTime, reindeers))
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
                arrayOf(75, 5, reindeersWithSpeed10And15),
                arrayOf(150, 10, reindeersWithSpeed10And15),
                arrayOf(150, 15, reindeersWithSpeed10And15),
                arrayOf(160, 16, reindeersWithSpeed10And15),
                arrayOf(190, 19, reindeersWithSpeed10And15),
                arrayOf(200, 20, reindeersWithSpeed10And15),
                arrayOf(200, 21, reindeersWithSpeed10And15),
                arrayOf(200, 30, reindeersWithSpeed10And15),
                arrayOf(200, 33, reindeersWithSpeed10And15),
                arrayOf(210, 34, reindeersWithSpeed10And15),
                arrayOf(450, 90, reindeersWithSpeed10And15),
                arrayOf(600, 100, reindeersWithSpeed10And15),
                arrayOf(600, 120, reindeersWithSpeed10And15),
                arrayOf(1500, 300, reindeersWithSpeed10And15),
                arrayOf(
                    1120, 1000, listOf(
                        Reindeer("Comet", speed = 14, tFly = 10, tRest = 127),
                        Reindeer("Dancer", speed = 16, tFly = 11, tRest = 162),
                    )
                ),
            )
        }
    }
}