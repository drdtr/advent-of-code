package advent_of_code_2015.day14

import kotlin.math.max
import kotlin.math.min

/**
 * [Reindeer Olympics - Part 2](https://adventofcode.com/2015/day/14#part2)
 *
 * Reindeers can only either fly with constant speed or rest: after a reindeer flies at speed `v` for `t_fly` seconds,
 * it must rest for `t_rest` seconds.
 *
 * Now, the scoring system is changed compared to [RaceWithRestingPart1]:
 * after each second, the reindeer in the lead gets one point.
 *
 * Find the maximum score a reindeer has collected in the given `racingTime`.
 *
 * Implementation note: this is a solution with `O(|reindeers| * racingTime)`,
 * which is hence exponential in the input size, because it's linear in the _value_ of `racingTime`.
 * A better solution on average would be to find points in time where the lead changes and
 * calculate the accumulated score for such points - however, in worst case, e.g., if the lead changes
 * regularly every few seconds, it would have the same runtime `O(|reindeers| * racingTime)`.
 */
class RaceWithRestingPart2 {
    fun maxScore(racingTime: Int, reindeers: Collection<Reindeer>): Int {
        val reindeers = reindeers.toTypedArray()
        if (reindeers.size == 0) return 0
        if (reindeers.size == 1) return racingTime

        val distances = IntArray(reindeers.size)
        val scores = IntArray(reindeers.size)
        for (t in 0 until racingTime) {
            for (i in reindeers.indices) with(reindeers[i]) {
                if (isMovingAtTime(t)) distances[i] += speed
            }

            val maxDist = distances.max()

            for (i in reindeers.indices) {
                if (distances[i] == maxDist) scores[i]++
            }
        }

        return scores.max()
    }

    private fun Reindeer.isMovingAtTime(t: Int): Boolean {
        val moveAndRestTime = tFly + tRest
        val tInCycle = t % moveAndRestTime
        return tInCycle < tFly
    }
}

private fun printResult(inputFileName: String) {
    val (racingTime, reindeers) = ReindeerInputReader.readInput(inputFileName)
    val solver = RaceWithRestingPart2()
    println("Maximum score: ${solver.maxScore(racingTime, reindeers)}")
}

fun main() {
    printResult("input.txt")
}