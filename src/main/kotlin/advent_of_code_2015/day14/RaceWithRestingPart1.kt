package advent_of_code_2015.day14

import Util.readInputLines
import advent_of_code_2015.day14.RaceWithRestingPart1.*
import kotlin.math.min

/**
 * [Reindeer Olympics - Part 1](https://adventofcode.com/2015/day/14)
 *
 * Reindeers can only either fly with constant speed or rest: after a reindeer flies at speed `v` for `t_fly` seconds,
 * it must rest for `t_rest` seconds.
 *
 * Given the speed, flying time, and resting time for each reindeer,
 * find the maximum distance travelled by a reindeer in the given `racingTime`.
 */
class RaceWithRestingPart1 {
    fun maxTravelledDistance(racingTime: Int, reindeers: Collection<Reindeer>): Int =
        reindeers.maxOf { it.calcTravelledDistance(racingTime) }

    private fun Reindeer.calcTravelledDistance(racingTime: Int): Int {
        val moveAndRestTime = tFly + tRest
        val numOfCompleteMoveAndRestCycles = racingTime / moveAndRestTime
        val distanceInCompleteCycles = speed * tFly * numOfCompleteMoveAndRestCycles

        val timeInLastCycle = racingTime % moveAndRestTime
        val distanceInLastCycle = speed * min(tFly, timeInLastCycle)

        return distanceInCompleteCycles + distanceInLastCycle
    }
}
private fun printResult(inputFileName: String) {
    val (racingTime, reindeers) = ReindeerInputReader.readInput(inputFileName)
    val solver = RaceWithRestingPart1()
    println("Maximum travelled distance: ${solver.maxTravelledDistance(racingTime, reindeers)}")
}

fun main() {
    printResult("input.txt")
}